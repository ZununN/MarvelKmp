package kz.zunun.data.characters

import kz.zunun.data.core.network.ApiService
import kz.zunun.domain.characters.CharactersRepository
import kz.zunun.domain.common.apiCall

class CharactersRepositoryImpl(private val service: ApiService) : CharactersRepository {

    override suspend fun fetchCharacters() = apiCall { service.getCharacters().toModel() }

    override suspend fun fetchCharactersPaging(limit: Int, page: Int) = apiCall {
        service.getCharactersPaging(limit = limit, offset = limit * page).toPagingModel()
    }

}

