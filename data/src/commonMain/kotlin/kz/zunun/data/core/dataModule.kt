package kz.zunun.data.core

import kz.zunun.data.character.CharacterRemoteDataSourceImpl
import kz.zunun.data.character.CharacterRepositoryImpl
import kz.zunun.data.characters.CharactersRepositoryImpl
import kz.zunun.data.core.network.ApiService
import kz.zunun.data.core.network.httpClientAndroid
import kz.zunun.domain.character.CharacterRemoteDataSource
import kz.zunun.domain.character.CharacterRepository
import kz.zunun.domain.characters.CharactersRepository
import org.koin.dsl.module

val dataModule = module {
    single { httpClientAndroid }
    single { ApiService(get()) }
    factory<CharactersRepository> { CharactersRepositoryImpl(get()) }
    factory<CharacterRepository> { CharacterRepositoryImpl(get()) }
    factory<CharacterRemoteDataSource> { CharacterRemoteDataSourceImpl(get()) }
}