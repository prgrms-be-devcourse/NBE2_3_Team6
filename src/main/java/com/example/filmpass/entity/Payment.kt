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
    private val PaymentId: Long? = null

    @Column(name = "paymentStatus")
    @Enumerated(EnumType.STRING)
    private var status: PayStatus? = null

    private val amount: Int? = null

    private val payToken: String? = null //토스 결제하면 생성되는 토큰

    @Column(name = "paymentType")
    @Enumerated(EnumType.STRING)
    private var payType: PayType? = null

    private val paidTs: LocalDateTime? = null //결제 일시

    private val refundAmount = 0 // 환불된 금액 (부분 환불을 위해)

    private val availableRefundAmount: Int? = null // 결제금액 - 누적된 횐불 금액 값이 들어감

    @Enumerated(EnumType.STRING) // Enum을 String으로 저장
    private val refundStatus: RefundStatus? = null

    private val refundRequestDate: LocalDateTime? = null // 환불 요청 날짜

    private val refundDate: LocalDateTime? = null // 환불 처리 날짜


    private val createdTs: LocalDateTime? = null

    @OneToOne //예매랑 연결되면 추가하기
    @JoinColumn(name = "reserve_id")
    private val reservation: Reservation? = null
}
