package kz.zunun.domain.character

import kz.zunun.domain.characters.models.CharacterItemModel
import kz.zunun.domain.common.Conclusion

interface CharacterRepository {
    suspend fun fetchCharacter(id: Int): Conclusion<CharacterItemModel>
}

interface CharacterRemoteDataSource {
    suspend fun fetchCharacter(id: Int): Conclusion<CharacterItemModel>
}
