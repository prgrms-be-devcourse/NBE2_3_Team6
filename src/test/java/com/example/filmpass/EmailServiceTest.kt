import com.example.filmpass.FilmpassApplication  // 애플리케이션 클래스 경로를 올바르게 변경하세요
import com.example.filmpass.service.EmailService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [FilmpassApplication::class])
class EmailServiceIntegrationTest {

    @Autowired
    private lateinit var emailService: EmailService

    @Test
    fun `test sendReservationConfirmation with real email`() {
        emailService.sendReservationConfirmation(
            to = "youngjozizon@naver.com",
            from = "brionlee97@naver.com",
            movieName = "Test Movie",
            reservationDetails = "예매 상세 정보"
        )
        println("이메일이 전송되었습니다.")
    }
}
