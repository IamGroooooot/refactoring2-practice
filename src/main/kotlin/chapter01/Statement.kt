package chapter01

import java.text.NumberFormat
import java.util.Locale.US

fun statement(invoice: Invoice, plays: Plays): String {
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

    fun format(number: Double): String {
        return NumberFormat.getCurrencyInstance(US).format(number)
    }

    var totalAmount = 0
    var volumeCredits = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"

    for (perf in invoice.performances) {
        volumeCredits += volumeCreditsFor(perf)

        // 청구 내역을 출력한다.
        result += "  ${playFor(perf).name}: ${format(amountFor(perf) / 100.0)} (${perf.audience}석)\n"
        totalAmount += amountFor(perf)
    }

    result += "총액: ${format(totalAmount / 100.0)}\n"
    result += "적립 포인트: ${volumeCredits}점"
    return result
}
