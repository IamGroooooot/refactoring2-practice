package chapter01

import kotlinx.serialization.Serializable

@Serializable
data class Invoice(
    val customer: String,
    val performances: List<Performance>
)
