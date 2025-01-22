package chapter01

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StatementTest {

    private lateinit var plays: Plays
    private lateinit var invoices: List<Invoice>

    @BeforeEach
    fun setup() {
        // Arrange: JSON 파일 로드 및 데이터 클래스 객체로 변환
        plays = DataLoader.loadPlays("plays.json")
        invoices = DataLoader.loadInvoices("invoice.json")
    }

    @Test
    fun `should print a statement`() {
        // Arrange: 기대하는 출력
        val expected = """
            청구 내역 (고객명: BigCo)
              Hamlet: $650.00 (55석)
              As You Like It: $580.00 (35석)
              Othello: $500.00 (40석)
            총액: $1,730.00
            적립 포인트: 47점
        """.trimIndent()

        // Act: statement 함수 호출
        val actual = statement(invoices[0], plays)

        // Assert: 기대하는 출력과 실제 출력 비교
        assertEquals(expected, actual)
    }
}
