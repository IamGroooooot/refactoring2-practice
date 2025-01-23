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

    fun usd(number: Int): String {
        return NumberFormat.getCurrencyInstance(US).format(number / 100.0)
    }

    fun totalVolumeCredits(): Int {
        var volumeCredits = 0
        for (perf in invoice.performances) {
            volumeCredits += volumeCreditsFor(perf)
        }
        return volumeCredits
    }

    fun 임시(): Int {
        var totalAmount = 0
        for (perf in invoice.performances) {
            totalAmount += amountFor(perf)
        }
        return totalAmount
    }

    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    for (perf in invoice.performances) {
        // 청구 내역을 출력한다.
        result += "  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience}석)\n"
    }

    var totalAmount = 임시()

    result += "총액: ${usd(totalAmount)}\n"
    result += "적립 포인트: ${totalVolumeCredits()}점"
    return result
}
