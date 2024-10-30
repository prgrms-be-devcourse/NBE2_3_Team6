package com.example.filmpass.service

import com.example.filmpass.dto.PaymentDTO
import com.example.filmpass.dto.RefundDTO
import com.example.filmpass.entity.PayStatus
import com.example.filmpass.entity.PayType
import com.example.filmpass.entity.Payment
import com.example.filmpass.entity.Reservation
import com.example.filmpass.repository.PaymentRepository
import com.example.filmpass.repository.ReservationRepository
import com.example.filmpass.util.RefundStatus
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.AllArgsConstructor
import lombok.RequiredArgsConstructor
import org.json.simple.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Transactional
@Service
class PaymentService (
    private val paymentRepository: PaymentRepository,
    private val reservationRepository: ReservationRepository,
    private val objectMapper: ObjectMapper
    ){
    //결제 요청 메서드 - 결제 요청하는 토스 API이용
    fun payment(paymentDTO: PaymentDTO): ResponseEntity<String?>? {
        val url: URL
        val responseBody = StringBuilder()
        try {
            url = URL("https://pay.toss.im/api/v2/payments")
            val connection = url.openConnection() as HttpURLConnection
            connection.addRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            connection.doInput = true

            //예매 정보 있는지 확인
            val reservation = reservationRepository.findByReserveId(paymentDTO.reserveId)

            //결제 정보 있는지 확인
            val payment1 = paymentRepository.findByReservation(reservation)


            //예매 정보 없을 경우
            if (reservation == null) {
                val body = "예매 정보가 없습니다"
                return ResponseEntity(body, HttpStatus.OK)
            } else if (payment1 != null) {
                val body = "이미 결제한 예매입니다."
                return ResponseEntity(body, HttpStatus.OK)
            } else if (reservation != null) {
                val jsonBody = JSONObject()
                val random = Random()
                jsonBody["orderNo"] = reservation.reserveId.toString() + "movie" + random.nextInt(50000) + 1500

                //연령대에 따른 가격 설정
                if (reservation.adult == 1) {
                    jsonBody["amount"] = "15000"
                } else if (reservation.youth == 1) {
                    jsonBody["amount"] = "10000"
                } else {
                    jsonBody["amount"] = "5000"
                }
                jsonBody["amountTaxFree"] = 0
                jsonBody["productDesc"] = "영화 예매"
                jsonBody["apiKey"] = "sk_test_w5lNQylNqa5lNQe013Nq"
                jsonBody["autoExecute"] = true
                jsonBody["resultCallback"] = ""
                jsonBody["retUrl"] = "http://localhost:8080/pay/return"
                jsonBody["retCancelUrl"] = "http://localhost:8080/pay/cancel"

                val bos = BufferedOutputStream(connection.outputStream)

                bos.write(jsonBody.toJSONString().toByteArray(StandardCharsets.UTF_8))
                bos.flush()
                bos.close()

                val br = BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8))
                var line: String? = null
                while ((br.readLine().also { line = it }) != null) {
                    responseBody.append(line)
                }
                br.close()

                //<---- DB에 저장하기 위해 entity에 넣기
                val payment = Payment()
                payment.reservation = reservation
                // JSON 파싱: 요청 후 응답으로 받은 payToken 가져오기
                val jsonNode = objectMapper!!.readTree(responseBody.toString())
                val token = jsonNode["payToken"].asText()
                payment.payToken = token
                payment.createdTs = LocalDateTime.now()
                //결제 완료 전이므로 PAY_STANDBY로 상태 설정
                payment.status = PayStatus.PAY_STANDBY

                //---->

                //Payment DB에 저장
                paymentRepository.save(payment)

                //사용자에게 결제 진행할 수 있는 URL 돌려주기
                val checkoutPage = jsonNode["checkoutPage"].asText()
                return ResponseEntity.ok(checkoutPage)
            }
        } catch (e: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message) // 오류 메시지 반환
        }
        return null
    }


    //결제 정보 저장 메소드 - 결제 정보 불러오는 토스 API이용
    fun payComplete(reserveId: String, apiKey: String?) {
        var url: URL? = null
        var connection: URLConnection? = null
        val responseBody = StringBuilder()
        try {
            url = URL("https://pay.toss.im/api/v2/status")
            connection = url.openConnection()
            connection.addRequestProperty("Content-Type", "application/json")
            connection.doOutput = true
            connection.doInput = true
            //API 사용할 때 필요한 정보 넘겨주기
            val jsonBody = JSONObject()
            jsonBody["orderNo"] = reserveId
            jsonBody["apiKey"] = apiKey

            val bos = BufferedOutputStream(connection.getOutputStream())

            bos.write(jsonBody.toJSONString().toByteArray(StandardCharsets.UTF_8))
            bos.flush()
            bos.close()


            val br = BufferedReader(InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
            var line: String? = null
            while ((br.readLine().also { line = it }) != null) {
                responseBody.append(line)
            }

            br.close()

            //<--- 응답받은 정보는 필요한 정보 파싱해서 entity에 저장
            val jsonNode2 = objectMapper!!.readTree(responseBody.toString())
            val time = jsonNode2["paidTs"].asText()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val paidTs = LocalDateTime.parse(time, formatter)

            val amount = jsonNode2["amount"].asText()
            //랜덤 값 만들었으므로 예매 번호만 뽑아내기
            val substring = reserveId.substring(0, reserveId.indexOf("m"))

            val reservation = reservationRepository.findByReserveId(substring.toLong())
            val payment = paymentRepository.findByReservation(reservation)
            if (payment != null) {
                payment.status = PayStatus.PAY_COMPLETE
                payment.paidTs = paidTs
                payment.amount = amount.toInt()
                payment.availableRefundAmount = amount.toInt() // 초기화 추가
                //결제 정보가 TOSS_MOENY면 현금 , CARD면 카드
                if (jsonNode2["payMethod"].asText() === "TOSS_MONEY") {
                    val payType = PayType.CASH
                    payment.payType = payType
                } else {
                    val payType = PayType.CARD
                    payment.payType = payType
                }
                paymentRepository.save<Payment>(payment)
            }



            //---->

            //Payment DB에 저장
        } catch (e: Exception) {
            responseBody.append(e)
        }
        println(responseBody.toString())
    }


    //결제 취소 메서드 - 상태 결제 취소로 바뀌도록
    fun payCancle() {
        val reservation = lastOrder
        val payment = paymentRepository.findByReservation(reservation)
        if (payment != null) {
            payment.status = PayStatus.PAY_CANCEL
            println("Payment status updated to: ${payment.status}") // 로그 추가

        }
    }

    val lastOrder: Reservation?
        get() {
            val payment = paymentRepository.findTopByOrderByCreatedTsDesc()?.orElse(null)
            return payment?.reservation
        }

    // 환불 처리
    fun refund(refundDTO: RefundDTO): ResponseEntity<String?> {
        try {
            // reserveId로 예매 정보 조회

            val reservation = reservationRepository.findByReserveId(refundDTO.reserveId)
            //결제 정보 조회
            val payment = paymentRepository.findByReservation(reservation)


            //예매 정보가 존재하지 않을 경우
            if (reservation == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예매 정보를 찾을 수 없습니다.")
            }
            // 결제 정보가 존재하지 않을 때 해당 메시지 반환함
            if (payment == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 정보를 찾을 수 없습니다.")
            }
            val payToken: String = payment.payToken
                ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).body<String?>("결제 토큰이 없습니다.")

            // 조회한 결제의 payToken 가져오기 없다면 오류 메시지 반환

            // 환불 가능 금액 계산
            val availableRefundAmount: Int? = payment.availableRefundAmount // 수정된 필드 사용

            // 환불 금액 초과 여부 확인
            val amount = refundDTO.amount ?: 0 // null일 경우 0으로 기본값 설정
            val availableRefundAmounts = availableRefundAmount ?: 0 //
            if (amount > availableRefundAmounts) {
                // 상태를 FAILED로 업데이트(금액이 문제인 경우, 사용자 요청이 문제인 경우임)
                payment.refundStatus = RefundStatus.FAILED
                paymentRepository.save(payment)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("환불 금액이 환불 가능한 금액을 초과합니다.")
            }


            // 환불 요청 날짜 설정
            refundDTO.refundRequestDate = LocalDateTime.now() // 현재 시간으로 설정

            // 환불 요청 처리
            val url = URL("https://pay.toss.im/api/v2/refunds")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            // 요청 본문을 작성하기 위해 출력 가능하도록 설정함 아래 형식으로 작성하면 됨
            connection.doOutput = true

            // JSON 요청 본문 작성
            val jsonBody = JSONObject()
            jsonBody["payToken"] = payToken // 결제 시 받은 토큰 사용
            jsonBody["amount"] = refundDTO.amount
            jsonBody["apiKey"] = "sk_test_w5lNQylNqa5lNQe013Nq"

            // 요청 본문 전송
            connection.outputStream.write(jsonBody.toJSONString().toByteArray(StandardCharsets.UTF_8))

            // API 응답 처리
            val responseCode = connection.responseCode
            val responseBody = StringBuilder()

            val br = if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader(InputStreamReader(connection.inputStream, StandardCharsets.UTF_8))
            } else {
                BufferedReader(InputStreamReader(connection.errorStream, StandardCharsets.UTF_8))
            }

            var line: String?
            while ((br.readLine().also { line = it }) != null) {
                responseBody.append(line)
            }
            br.close()

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 환불 성공 시 Payment 엔티티 업데이트
                payment.refundAmount = payment.refundAmount + refundDTO.amount!! // 누적 환불 금액 업데이트
                payment.availableRefundAmount = payment.amount?.minus(payment.refundAmount) // 환불 가능 금액 재계산
                payment.refundStatus = RefundStatus.REFUNDED
                payment.refundRequestDate = refundDTO.refundRequestDate // 요청된 날짜 저장
                payment.refundDate = LocalDateTime.now() // 처리된 날짜를 현재 시간으로 설정

                paymentRepository.save(payment) //업뎃된 내용 저장
                // db에는 정상적으로 작동되지만 출력화면에서는 남은 환불 가능 금액이 (결제금액 - 초기설정:0)으로 뜸 따라서 아래 기능 추가
                val showavailableRefundAmount: Int? = payment.availableRefundAmount
                // Toss API 응답과 남은 환불 가능 금액을 함께 반환
                val combinedResponse = """
                    $responseBody
                    남은 환불 가능 금액: ${showavailableRefundAmount}원
                    """.trimIndent()

                return ResponseEntity.ok(combinedResponse) // 환불 성공 응답 반환
            } else {
                // 환불 실패 시 상태를 FAILED로 업데이트(서버, api가 문제인 경우, 요청 자체는 유효함)
                payment.refundStatus = RefundStatus.FAILED
                paymentRepository.save(payment)

                return ResponseEntity.status(responseCode).body(responseBody.toString())
            }
        } catch (e: Exception) {
            // 예외 발생 시, INTERNAL SERVER ERROR 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
        }
    }
}

