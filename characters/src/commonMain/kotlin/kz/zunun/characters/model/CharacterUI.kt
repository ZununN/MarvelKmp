package kz.zunun.characters.model

import kotlinx.serialization.Serializable
import kz.zunun.domain.characters.models.CharacterItemModel


@Serializable
data class CharacterUI(
    val name: String,
    val description: String?,
    val imageUrl: String,
    val id: Int,
)

fun CharacterItemModel.toUi(): CharacterUI {
    return CharacterUI(
        id = this.id,
        name = this.name,
        description = this.description,
        imageUrl = this.imageUrl
    )
}
