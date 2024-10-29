package com.example.filmpass.entity

import com.example.filmpass.util.RefundStatus
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(
    AuditingEntityListener::class
)
class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val PaymentId: Long? = null

    @Column(name = "paymentStatus")
    @Enumerated(EnumType.STRING)
    var status: PayStatus? = null

    var amount: Int? = null

    var payToken: String? = null //토스 결제하면 생성되는 토큰

    @Column(name = "paymentType")
    @Enumerated(EnumType.STRING)
    var payType: PayType? = null

    var paidTs: LocalDateTime? = null //결제 일시

    var refundAmount = 0 // 환불된 금액 (부분 환불을 위해)

    var availableRefundAmount: Int? = null // 결제금액 - 누적된 횐불 금액 값이 들어감

    @Enumerated(EnumType.STRING) // Enum을 String으로 저장
    var refundStatus: RefundStatus? = null

    var refundRequestDate: LocalDateTime? = null // 환불 요청 날짜

    var refundDate: LocalDateTime? = null // 환불 처리 날짜


    var createdTs: LocalDateTime? = null

    @OneToOne //예매랑 연결되면 추가하기
    @JoinColumn(name = "reserve_id")
    var reservation: Reservation? = null
}
