package kz.zunun.character_detail

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kz.zunun.core.BaseViewModel
import kz.zunun.core.MVIComponentContext
import kz.zunun.core.createVM
import kz.zunun.domain.character.CharacterRepository
import kz.zunun.domain.common.onFailure
import kz.zunun.domain.common.onSuccess
import org.koin.core.component.get

sealed class State {
    data object Loading : State()
    data object Error : State()
    data class Success(
        val name: String,
        val description: String?,
        val imageUrl: String,
    ) : State()
}

sealed class Action {
    data object FetchInfo : Action()
}

class CharacterDetailComponent(
    componentContext: ComponentContext,
    private val id: Int,
    val backClick: () -> Unit,
) : MVIComponentContext<State, Action>(componentContext) {

    private val vm = createVM { ViewModel(get(), id) }

    override val state: StateFlow<State> = vm.stateFlow

    override fun dispatch(action: Action) {
        vm.dispatch(action)
    }

}


class ViewModel(
    private val repository: CharacterRepository,
    private val id: Int,
) : BaseViewModel<State, Action, Unit>(State.Loading) {


    init {
        dispatch(Action.FetchInfo)
    }

    override fun dispatch(action: Action) {
        when (action) {
            Action.FetchInfo -> fetchCharacterInfo()
        }
    }

    private fun fetchCharacterInfo() {
        setState(State.Loading)
        viewModelScope.launch {
            repository.fetchCharacter(id).onSuccess {
                setState(
                    State.Success(
                        name = it.name,
                        description = it.description,
                        imageUrl = it.imageUrl
                    )
                )
            }.onFailure {
                setState(State.Error)
            }
        }
    }

}