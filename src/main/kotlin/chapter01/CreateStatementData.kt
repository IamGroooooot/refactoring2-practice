package chapter01


data class StatementData(
    val customer: String, val performances: List<EnrichedPerformance>
) {
    var totalAmount: Int = 0
    var totalVolumeCredits: Int = 0
}

class EnrichedPerformance() {
    var playID: String = ""
    var audience: Int = 0
    var amount: Int = 0
    var volumeCredits: Int = 0
    var play: Play? = null
    var calculator: PerformanceCalculator? = null

    constructor(aPerformance: Performance) : this() {
        this.playID = aPerformance.playID
        this.audience = aPerformance.audience
    }
}

class PerformanceCalculator {
    var aPerformance: Performance
    var play: Play? = null
    val amount: Int
        get() {
            var result = 0
            when (this.play?.type) {
                "tragedy" -> {
                    result = 40000
                    if (this.aPerformance.audience > 30) {
                        result += 1000 * (this.aPerformance.audience - 30)
                    }
                }

                "comedy" -> {
                    result = 30000
                    if (this.aPerformance.audience > 20) {
                        result += 10000 + 500 * (this.aPerformance.audience - 20)
                    }
                    result += 300 * this.aPerformance.audience
                }

                else -> {
                    throw Error("알 수 없는 장르: ${this.play?.type}")
                }
            }

            return result
        }
    val volumeCredits: Int
        get() {
            var result = maxOf(this.aPerformance.audience - 30, 0)
            if (this.play?.type == "comedy") result += this.aPerformance.audience / 5
            return result
        }

    constructor(aPerformance: Performance, aPlay: Play) {
        this.aPerformance = aPerformance
        this.play = aPlay
    }
}

internal fun createStatementData(plays: Plays, invoice: Invoice): StatementData {
    fun playFor(aPerformance: Performance): Play {
        return plays[aPerformance.playID]!!
    }

    fun totalVolumeCredits(data: StatementData): Int {
        return data.performances.sumOf { it.volumeCredits }
    }

    fun totalAmount(data: StatementData): Int {
        return data.performances.sumOf { it.amount }
    }

    fun enrichPerformance(aPerformance: Performance): EnrichedPerformance {
        val calculator = createPerformanceCalculator(aPerformance, playFor(aPerformance))
        val result = EnrichedPerformance(aPerformance)
        result.play = calculator.play
        result.amount = calculator.amount
        result.volumeCredits = calculator.volumeCredits
        return result
    }

    val statementData = StatementData(
        invoice.customer, invoice.performances.map { enrichPerformance(it) }).apply {
        totalAmount = totalAmount(this)
        totalVolumeCredits = totalVolumeCredits(this)
    }
    return statementData
}

private fun createPerformanceCalculator(aPerformance: Performance, aPlay: Play): PerformanceCalculator =
    PerformanceCalculator(aPerformance, aPlay)