package chapter01

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PlayType {
    @SerialName("tragedy")
    TRAGEDY,
    @SerialName("comedy")
    COMEDY
}

