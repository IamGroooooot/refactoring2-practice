package chapter01

import java.text.NumberFormat
import java.util.Locale.US

data class StatementData(
    val customer: String, val performances: List<EnrichedPerformance>
)

data class EnrichedPerformance(
    val playID: String, val audience: Int, val play: Play
)

fun statement(invoice: Invoice, plays: Plays): String {
    fun playFor(aPerformance: Performance): Play {
        return plays[aPerformance.playID]!!
    }

    fun enrichPerformance(performance: Performance): EnrichedPerformance {
        return EnrichedPerformance(
            performance.playID, performance.audience, playFor(performance)
        )
    }

    val statementData = StatementData(invoice.customer, invoice.performances.map { enrichPerformance(it) })
    return renderPlainText(statementData, plays)
}

private fun renderPlainText(data: StatementData, plays: Plays): String {
    fun playFor(aPerformance: EnrichedPerformance): Play {
        return plays[aPerformance.playID]!!
    }

    fun amountFor(aPerformance: EnrichedPerformance): Int {
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

    fun volumeCreditsFor(aPerformance: EnrichedPerformance): Int {
        var result = maxOf(aPerformance.audience - 30, 0)
        if (playFor(aPerformance).type == "comedy") result += aPerformance.audience / 5
        return result
    }

    fun usd(number: Int): String {
        return NumberFormat.getCurrencyInstance(US).format(number / 100.0)
    }

    fun totalVolumeCredits(): Int {
        var result = 0
        for (perf in data.performances) {
            result += volumeCreditsFor(perf)
        }
        return result
    }

    fun totalAmount(): Int {
        var result = 0
        for (perf in data.performances) {
            result += amountFor(perf)
        }
        return result
    }

    var result = "청구 내역 (고객명: ${data.customer})\n"
    for (perf in data.performances) {
        // 청구 내역을 출력한다.
        result += "  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience}석)\n"
    }
    result += "총액: ${usd(totalAmount())}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점"
    return result
}
