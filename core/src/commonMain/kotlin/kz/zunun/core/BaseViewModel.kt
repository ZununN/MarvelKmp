package kz.zunun.core

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Action, Liable>(initValue: State) : InstanceKeeper.Instance {


    val viewModelScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    abstract fun dispatch(action: Action)

    private val _state = MutableStateFlow(initValue)

    val stateFlow = _state.asStateFlow()

    private val _liable = MutableSharedFlow<Liable>(
        replay = 0,
        extraBufferCapacity = 1, // you can increase
        BufferOverflow.DROP_OLDEST
    )

    val liableFlow: Flow<Liable> = _liable.asSharedFlow()

    protected val currentState: State get() = _state.value


    protected fun setState(newState: State) {
        _state.value = newState
    }

    protected fun setLiable(liable: Liable) {
        viewModelScope.launch {
            _liable.emit(liable)
        }
    }

    protected fun <T> Flow<T>.collectInScope(collector: suspend (T) -> Unit): Job {
        return viewModelScope.launch { collect(collector) }
    }


    override fun onDestroy() {
        viewModelScope.cancel()
        super.onDestroy()
    }
}