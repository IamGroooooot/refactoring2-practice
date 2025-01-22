package chapter01

class Plays(private val plays: Map<String, Play>) {
    fun get(playID: String): Play {
        return plays[playID] ?: throw IllegalArgumentException("플레이 정보를 찾을 수 없습니다. playID=$playID")
    }
}