package chapter01

import java.text.NumberFormat
import java.util.Locale.US

fun statement(invoice: Invoice, plays: Plays): String {
    fun amountFor(perf: Performance, play: Play): Int {
        var thisAmount = 0
        when (play.type) {
            "tragedy" -> {
                thisAmount = 40000
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30)
                }
            }

            "comedy" -> {
                thisAmount = 30000
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20)
                }
                thisAmount += 300 * perf.audience
            }

            else -> {
                throw Error("알 수 없는 장르: ${play.type}")
            }
        }

        return thisAmount
    }

    var totalAmount = 0
    var volumeCredit = 0
    var result = "청구 내역 (고객명: ${invoice.customer})\n"
    val format = { number: Double -> NumberFormat.getCurrencyInstance(US).format(number) }

    for (perf in invoice.performances) {
        val play = plays[perf.playID]!!
        val thisAmount = amountFor(perf, play)

        // 포인트를 적립한다.
        volumeCredit += maxOf(perf.audience - 30, 0)

        // 희극 관객 5명마다 추가 포인트를 제공한다
        if (play.type == "comedy") volumeCredit += perf.audience / 5

        // 청구 내역을 출력한다.
        result += "  ${play.name}: ${format(thisAmount / 100.0)} (${perf.audience}석)\n"
        totalAmount += thisAmount
    }

    result += "총액: $${"%,.2f".format(totalAmount / 100.0)}\n"
    result += "적립 포인트: ${volumeCredit}점"
    return result
}
