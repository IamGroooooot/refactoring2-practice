package chapter01

import java.text.NumberFormat
import java.util.Locale.US

fun statement(invoice: Invoice, plays: Plays): String {
    return renderPlainText(createStatementData(plays, invoice), plays)
}

fun htmlStatement(invoice: Invoice, plays: Plays): String {
    return renderHtml(createStatementData(plays, invoice))
}

fun renderHtml(data: StatementData): String {
    fun usd(number: Int): String {
        return NumberFormat.getCurrencyInstance(US).format(number / 100.0)
    }

    var result = "<h1>청구 내역 (고객명: ${data.customer})</h1>\n"
    result += "<table>\n"
    result += "<tr><th>연극</th><th>좌석 수</th><th>금액</th></tr>\n"
    for (perf in data.performances) {
        result += "<tr><td>${perf.play.name}</td><td>${perf.audience}</td>"
        result += "<td>${usd(perf.amount)}</td></tr>\n"
    }
    result += "</table>\n"
    result += "<p>총액: <em>${usd(data.totalAmount)}</em></p>\n"
    result += "<p>적립 포인트: <em>${data.totalVolumeCredits}</em>점</p>"
    return result
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
