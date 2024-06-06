package kz.zunun.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class BaseComponentContext(componentContext: ComponentContext) :
    ComponentContext by componentContext , KoinComponent {

    protected val scope = componentScope()

    protected fun <T> Flow<T>.collectInScope(collector: suspend (T) -> Unit): Job {
        return scope.launch { collect(collector) }
    }
}

abstract class MVIComponentContext<State, Action>(componentContext: ComponentContext) :
    BaseComponentContext(componentContext) {

    abstract val state: StateFlow<State>

    abstract fun dispatch(action: Action)
}


fun ComponentContext.componentScope(): CoroutineScope = CoroutineScope(
    Dispatchers.Main.immediate + SupervisorJob()
).apply {
    lifecycle.doOnDestroy { cancel() }
}

inline fun <reified T : InstanceKeeper.Instance> BaseComponentContext.createVM(factory: () -> T): T {
    return instanceKeeper.getOrCreate(key = T::class, factory = factory)
}
