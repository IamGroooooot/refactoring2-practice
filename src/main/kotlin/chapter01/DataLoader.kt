package chapter01

import kotlinx.serialization.json.Json
import java.io.File

object DataLoader {

    private val json = Json { ignoreUnknownKeys = false }

    fun loadPlays(filename: String = "plays.json"): Plays {
        val playsFile = getResourceFile(filename)
        return json.decodeFromString(playsFile.readText())
    }

    fun loadInvoices(filename: String = "invoice.json"): List<Invoice> {
        val invoicesFile = getResourceFile(filename)
        return json.decodeFromString(invoicesFile.readText())
    }

    private fun getResourceFile(filename: String): File {
        val resource = this::class.java.classLoader.getResource(filename)
            ?: throw IllegalArgumentException("리소스 파일을 찾을 수 없습니다: $filename")
        return File(resource.file)
    }

}
