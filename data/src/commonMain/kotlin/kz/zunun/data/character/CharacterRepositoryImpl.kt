package kz.zunun.data.character

import kz.zunun.data.characters.toModel
import kz.zunun.data.core.network.ApiService
import kz.zunun.domain.character.CharacterRemoteDataSource
import kz.zunun.domain.character.CharacterRepository
import kz.zunun.domain.characters.models.CharacterItemModel
import kz.zunun.domain.common.Conclusion
import kz.zunun.domain.common.apiCall

class CharacterRepositoryImpl(
    private val remoteDataSource: CharacterRemoteDataSource,
) : CharacterRepository {
    override suspend fun fetchCharacter(id: Int): Conclusion<CharacterItemModel> {
        return remoteDataSource.fetchCharacter(id)

    }
}

class CharacterRemoteDataSourceImpl(private val service: ApiService) : CharacterRemoteDataSource {

    override suspend fun fetchCharacter(id: Int) = apiCall {
        service.getCharacter(id).toModel().first()
    }
}