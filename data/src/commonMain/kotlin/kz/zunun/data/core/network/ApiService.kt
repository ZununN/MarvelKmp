package kz.zunun.data.core.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kz.zunun.data.characters.models.CharactersResponse

class ApiService(private val client: HttpClient) {

    private companion object {
        const val END_POINT = "https://gateway.marvel.com/v1/public/"
        const val CHARACTERS = "characters"
        const val SECRET_KEY =
            "?ts=1714327960&apikey=9f666104ea67bdba167d4a3d1e3504f4&hash=b3d90189ce985651472c93f011cc8a52"
    }


    suspend fun getCharacters(): CharactersResponse {
        val address =
            StringBuilder().append(END_POINT).append(CHARACTERS).append(SECRET_KEY).toString()
        return client.get(address).body()
    }

    suspend fun getCharactersPaging(limit: Int, offset: Int): CharactersResponse {
        val address =
            StringBuilder().append(END_POINT)
                .append("$CHARACTERS?")
                .append("limit=$limit&offset=$offset&")
                .append(SECRET_KEY.replace("?", "")).toString()
        return client.get(address).body()
    }

    suspend fun getCharacter(characterId: Int): CharactersResponse {
        val address = StringBuilder()
            .append(END_POINT)
            .append(CHARACTERS)
            .append("/${characterId}")
            .append(SECRET_KEY)
            .toString()

        return client.get(address).body<CharactersResponse>()
    }
}