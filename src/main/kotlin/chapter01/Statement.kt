package chapter01

import java.text.NumberFormat
import java.util.Locale.US

fun statement(invoice: Invoice, plays: Plays): String {
    return renderPlainText(createStatementData(plays, invoice), plays)
}

private fun renderPlainText(data: StatementData, plays: Plays): String {
    fun usd(number: Int): String {
        return NumberFormat.getCurrencyInstance(US).format(number / 100.0)
    }


    var result = "청구 내역 (고객명: ${data.customer})\n"
    for (perf in data.performances) {
        // 청구 내역을 출력한다.
        result += "  ${perf.play.name}: ${usd(perf.amount)} (${perf.audience}석)\n"
    }
    result += "총액: ${usd(data.totalAmount)}\n"
    result += "적립 포인트: ${data.totalVolumeCredits}점"
    return result
}
