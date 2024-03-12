package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorRespose(
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: Int
)