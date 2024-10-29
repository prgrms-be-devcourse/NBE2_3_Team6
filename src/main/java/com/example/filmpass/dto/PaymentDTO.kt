package com.example.filmpass.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import lombok.extern.log4j.Log4j2

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Log4j2
class PaymentDTO {
    private val ReserveId: Long? = null //예매번호
    private val apiKey: String? = null //가맹점 Key
    private val productDesc: String? = null //상품 설명
    private val retUrl: String? = null //구매자 인증 완료 후 연결할 웹페이지
    private val retCancelUrl: String? = null //결제를 중단할 때 이동시킬 취소 페이지
    private val autoExecute = false //자동 승인 여부(true = 바로 결제 / false = 판매자 승인 후 결제)
    private val resultCallback: String? = null //성공 결과 전송할 URL
    private val amount: Int? = null //총 결제 금액
    private val amountTaxFree: Int? = null //결제 금액 중 비과세 금액
}
