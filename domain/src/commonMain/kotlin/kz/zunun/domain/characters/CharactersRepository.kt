package kz.zunun.domain.characters

import kz.zunun.domain.characters.models.CharacterItemModel
import kz.zunun.domain.common.Conclusion
import kz.zunun.domain.common.PagingData

interface CharactersRepository {
    suspend fun fetchCharacters(): Conclusion<List<CharacterItemModel>>

    suspend fun fetchCharactersPaging(
        limit: Int,
        page: Int,
    ): Conclusion<PagingData<CharacterItemModel>>
}