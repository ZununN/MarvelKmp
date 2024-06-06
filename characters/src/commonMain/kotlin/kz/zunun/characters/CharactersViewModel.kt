package kz.zunun.characters

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.launch
import kz.zunun.characters.SubPagingInfo.Error
import kz.zunun.characters.SubPagingInfo.Loading
import kz.zunun.characters.SubPagingInfo.NotData
import kz.zunun.characters.model.CharacterUI
import kz.zunun.core.BaseViewModel
import kz.zunun.core.PagingState


sealed interface State {
    data object Loading : State
    data object Error : State

    @Immutable
    data class Success(
        val characters: List<CharacterUI>,
        val subPagingInfo: SubPagingInfo = SubPagingInfo.Loading,
    ) : State
}

enum class SubPagingInfo {
    NotData, Loading, Error
}

sealed interface Action {
    data object FetchCharacters : Action
    data object DownScroll : Action
    data class OnItemClick(val id: Int) : Action
}

sealed class Liable {
    data class NavigateToDetails(val id: Int) : Liable()
}

class CharactersViewModel(private val paging: CharactersPaging) :
    BaseViewModel<State, Action, Liable>(State.Loading) {

    init {
        dispatch(Action.FetchCharacters)
        paging.data.collectInScope {
            updateState(it)
        }
    }

    private fun updateState(pagingState: PagingState<CharacterUI>) {
        when (pagingState) {
            is PagingState.Error -> when (val current = currentState) {
                is State.Success -> setState(current.copy(subPagingInfo = Error))
                else -> setState(State.Error)
            }

            is PagingState.Loading -> when (val state = currentState) {
                is State.Success -> setState(state.copy(subPagingInfo = Loading))
                else -> setState(State.Loading)
            }

            is PagingState.Success -> {
                val info = if (pagingState.isEnd) NotData else Loading
                when (val state = currentState) {
                    is State.Success ->
                        setState(state.copy(characters = pagingState.data, subPagingInfo = info))

                    else -> setState(State.Success(pagingState.data, subPagingInfo = info))
                }
            }
        }
    }

    override fun dispatch(action: Action) {
        when (action) {
            is Action.FetchCharacters -> viewModelScope.launch {
                paging.loadData()
            }

            is Action.OnItemClick -> setLiable(Liable.NavigateToDetails(action.id))
            is Action.DownScroll -> downScroll()
        }
    }

    private fun downScroll() {
        when (val state = currentState) {
            is State.Success -> {
                when (state.subPagingInfo) {
                    Loading -> dispatch(Action.FetchCharacters)
                    else -> Unit
                }
            }

            else -> dispatch(Action.FetchCharacters)
        }
    }
}


