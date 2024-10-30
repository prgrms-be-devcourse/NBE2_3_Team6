package com.example.filmpass.controller

import com.example.filmpass.dto.PaymentDTO
import com.example.filmpass.dto.RefundDTO
import com.example.filmpass.service.PaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pay")
class PaymentController(
    private val paymentService: PaymentService
){

    //결제 페이지
    //test JSON 예시
    // {
    //  "reserveId": 예매 테이블에 있는 값 넣기 }
    @PostMapping("/payments")
    fun payment(@RequestBody paymentDTO: PaymentDTO?): ResponseEntity<String?>? {
        if (paymentDTO == null) {
            return ResponseEntity.badRequest().body("예매 번호를 입력해주세요")
        }
        return paymentService.payment(paymentDTO)
    }

    //결제완료 후 돌아갈 페이지
    @GetMapping("/return")
    fun returnPage(@RequestParam("orderNo") reserveId: String?): String {
        //결제 정보 저장하는 메소드 호출
        val apiKey = "sk_test_w5lNQylNqa5lNQe013Nq"
        paymentService.payComplete(reserveId!!, apiKey)
        return "결제 완료" //나중에 예매 정보 등 추가
    }

    @GetMapping("/cancel")
    fun canclePage(): String {
        paymentService.payCancle()
        return "결제 취소"
    }


    //
    //    {
    //        "reserveId": 환불하고 싶은 예매번호 넣기,
    //            "amount": 200(예시)     // 환불할 금액
    //    }
    @PostMapping("/refunds")
    fun refund(@RequestBody refundDTO: RefundDTO?): ResponseEntity<String?> {
        return paymentService.refund(refundDTO!!)
    }
}
