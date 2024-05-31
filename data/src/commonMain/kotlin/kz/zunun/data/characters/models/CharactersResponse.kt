package kz.zunun.data.characters.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(
    @SerialName("code")
    val code: Int,

    @SerialName("data")
    val data: Data,

    @SerialName("status")
    val status: String,

)