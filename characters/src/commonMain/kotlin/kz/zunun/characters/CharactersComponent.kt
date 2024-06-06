package kz.zunun.characters

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.StateFlow
import kz.zunun.core.MVIComponentContext
import kz.zunun.core.createVM
import org.koin.core.component.get


class CharactersComponent(
    componentContext: ComponentContext,
    private val onItemClicked: (Int) -> Unit,
) : MVIComponentContext<State, Action>(componentContext) {

    private val vm = createVM { CharactersViewModel(get()) }
    override val state: StateFlow<State> = vm.stateFlow

    init {
        vm.liableFlow.collectInScope {
            when (it) {
                is Liable.NavigateToDetails -> onItemClicked(it.id)
            }
        }
    }

    override fun dispatch(action: Action) {
        vm.dispatch(action)
    }
}