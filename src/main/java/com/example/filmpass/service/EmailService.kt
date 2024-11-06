package com.example.filmpass.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {

    fun sendReservationConfirmation(to: String, from: String, movieName: String, reservationDetails: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)

        helper.setTo(to)
        helper.setFrom(from)
        helper.setSubject("예매 완료 확인")
        helper.setText("""
            <h1>예매가 완료되었습니다!</h1>
            <p>영화: <strong>$movieName</strong></p>
            <p>예매 상세 정보:</p>
            <p>$reservationDetails</p>
        """.trimIndent(), true)

        mailSender.send(message)
    }
}
