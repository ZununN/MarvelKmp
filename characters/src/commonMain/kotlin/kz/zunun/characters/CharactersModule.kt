package kz.zunun.characters

import org.koin.dsl.module

val charactersModule = module {
    factory { CharactersPaging(get()) }
}