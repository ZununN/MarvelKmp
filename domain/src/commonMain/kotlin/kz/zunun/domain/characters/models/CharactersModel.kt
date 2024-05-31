package kz.zunun.domain.characters.models

data class CharacterItemModel(
    val name: String,
    val description: String?,
    val imageUrl: String,
    val id: Int,
)