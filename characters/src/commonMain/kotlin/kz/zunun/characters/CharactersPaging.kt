package kz.zunun.characters

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kz.zunun.characters.model.CharacterUI
import kz.zunun.characters.model.toUi
import kz.zunun.core.PagingState
import kz.zunun.core.pagingSuccess
import kz.zunun.domain.characters.CharactersRepository
import kz.zunun.domain.common.onFailure
import kz.zunun.domain.common.onSuccess

class CharactersPaging(private val repository: CharactersRepository) {

    private var all = emptyList<CharacterUI>()
    private var page = 0
    private var isDataFinished = false
    private var lastState: PagingState<CharacterUI>? = null

    private companion object {
        const val LIMIT = 20
    }

    private val _data = MutableSharedFlow<PagingState<CharacterUI>>()
    val data = _data.asSharedFlow()

    suspend fun loadData() {
        if (lastState is PagingState.Loading || isDataFinished) return
        setState(PagingState.Loading)
        repository.fetchCharactersPaging(limit = LIMIT, page = page).onSuccess { pagingData ->
            val newData = pagingData.data.map { it.toUi() }
            all += newData
            all.distinctBy { it.id }
            isDataFinished = newData.count() < LIMIT
            setState(all.pagingSuccess(isDataFinished))
            page++
        }.onFailure {
            setState(PagingState.Error(it))
        }
    }

    private suspend fun setState(state: PagingState<CharacterUI>) {
        lastState = state
        _data.emit(state)
    }

}