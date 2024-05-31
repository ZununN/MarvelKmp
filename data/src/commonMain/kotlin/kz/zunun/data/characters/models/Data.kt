package kz.zunun.data.characters.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    @SerialName("count") val count: Int,
    @SerialName("limit") val limit: Int,
    @SerialName("offset") val offset: Int,
    @SerialName("results") val results: List<CharacterInfo>,
    @SerialName("total") val total: Int,
)