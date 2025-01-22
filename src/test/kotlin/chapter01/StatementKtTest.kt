package chapter01

import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class StatementTest {

    private lateinit var json: Json
    private lateinit var plays: Plays
    private lateinit var invoices: List<Invoice>

    @BeforeEach
    fun setup() {
        json = Json { ignoreUnknownKeys = false }

        // Arrange: JSON 파일 로드 및 데이터 클래스 객체로 변환
        plays = loadPlays("plays.json")
        invoices = loadInvoices("invoice.json")
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

    private fun loadPlays(filename: String = "plays.json"): Plays {
        val playsFile = getResourceFile(filename)
        val playsData: Map<String, Play> = json.decodeFromString(playsFile.readText())
        return Plays(playsData)
    }

    private fun loadInvoices(filename: String = "invoice.json"): List<Invoice> {
        val invoicesFile = getResourceFile(filename)
        return json.decodeFromString(invoicesFile.readText())
    }

    private fun getResourceFile(filename: String): File {
        val resource = javaClass.classLoader.getResource(filename)
            ?: throw IllegalArgumentException("리소스 파일을 찾을 수 없습니다: $filename")
        return File(resource.file)
    }
}
