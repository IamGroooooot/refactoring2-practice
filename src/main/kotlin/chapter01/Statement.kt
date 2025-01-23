package chapter01

import java.text.NumberFormat
import java.util.Locale.US

fun statement(invoice: Invoice, plays: Plays): String {
    val statementData = mutableMapOf<String, Any>()
    statementData.put("customer", invoice.customer)

    fun enrichPerformance(performance: Performance): Performance {
        val result = performance.copy()
        return result
    }
    statementData.put("performances", invoice.performances.map { enrichPerformance(it) })

    return renderPlainText(statementData, plays)
}

private fun renderPlainText(data: MutableMap<String, Any>, plays: Plays): String {
    fun playFor(aPerformance: Performance): Play {
        return plays[aPerformance.playID]!!
    }

    fun amountFor(aPerformance: Performance): Int {
        var result = 0
        when (playFor(aPerformance).type) {
            "tragedy" -> {
                result = 40000
                if (aPerformance.audience > 30) {
                    result += 1000 * (aPerformance.audience - 30)
                }
            }

            "comedy" -> {
                result = 30000
                if (aPerformance.audience > 20) {
                    result += 10000 + 500 * (aPerformance.audience - 20)
                }
                result += 300 * aPerformance.audience
            }

            else -> {
                throw Error("알 수 없는 장르: ${playFor(aPerformance).type}")
            }
        }

        return result
    }

    fun volumeCreditsFor(aPerformance: Performance): Int {
        var result = maxOf(aPerformance.audience - 30, 0)
        if (playFor(aPerformance).type == "comedy")
            result += aPerformance.audience / 5
        return result
    }

    fun usd(number: Int): String {
        return NumberFormat.getCurrencyInstance(US).format(number / 100.0)
    }

    fun totalVolumeCredits(): Int {
        var result = 0
        for (perf in data["performances"] as List<Performance>) {
            result += volumeCreditsFor(perf)
        }
        return result
    }

    fun totalAmount(): Int {
        var result = 0
        for (perf in data["performances"] as List<Performance>) {
            result += amountFor(perf)
        }
        return result
    }

    var result = "청구 내역 (고객명: ${data["customer"]})\n"
    for (perf in data["performances"] as List<Performance>) {
        // 청구 내역을 출력한다.
        result += "  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience}석)\n"
    }
    result += "총액: ${usd(totalAmount())}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점"
    return result
}
