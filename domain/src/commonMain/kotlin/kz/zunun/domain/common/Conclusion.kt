package kz.zunun.domain.common


suspend fun <T> apiCall(block: suspend () -> T): Conclusion<T> {
    return try {
        Conclusion.Success(block.invoke())
    } catch (e: Exception) {
        println("apiCall: $e")
        Conclusion.Failure(e)
    }
}

sealed class Conclusion<out T> {
    data class Success<T>(val value: T) : Conclusion<T>()
    data class Failure(val exception: Throwable) : Conclusion<Nothing>()
}

val <T> Conclusion<T>.isFailure: Boolean get() = this is Conclusion.Failure

val <T> Conclusion<T>.isSuccess: Boolean get() = this !is Conclusion.Failure

fun <T> Conclusion<T>.getOrNull() = when (this) {
    is Conclusion.Failure -> null
    is Conclusion.Success -> value
}


fun <R, T : R> Conclusion<T>.getOrDefault(defaultValue: R): R = when (this) {
    is Conclusion.Failure -> defaultValue
    is Conclusion.Success -> value
}

fun Conclusion<*>.throwOnFailure() {
    if (this is Conclusion.Failure) throw exception
}

fun <T> Conclusion<T>.getOrThrow(): T {
    throwOnFailure()
    return (this as Conclusion.Success<T>).value
}

inline fun <R, T : R> Conclusion<T>.getOrElse(onFailure: (exception: Throwable) -> R): R = when (this) {
    is Conclusion.Failure -> onFailure.invoke(exception)
    is Conclusion.Success -> value
}

inline fun <R, T> Conclusion<T>.map(transform: (value: T) -> R): Conclusion<R> = when (this) {
    is Conclusion.Failure -> Conclusion.Failure(exception)
    is Conclusion.Success -> Conclusion.Success(transform.invoke(this.value))
}

inline fun <R, T> Conclusion<T>.mapCatching(transform: (value: T) -> R): Conclusion<R> = when (this) {
    is Conclusion.Failure -> Conclusion.Failure(exception)
    is Conclusion.Success -> try {
        Conclusion.Success(transform.invoke(value))
    } catch (e: Exception) {
        Conclusion.Failure(e)
    }
}

inline fun <R, T> Conclusion<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R,
) = when (this) {
    is Conclusion.Failure -> onFailure(exception)
    is Conclusion.Success -> onSuccess(value)
}


inline fun <T> Conclusion<T>.onSuccess(action: (value: T) -> Unit): Conclusion<T> {
    if (this is Conclusion.Success) {
        action.invoke(value)
    }
    return this
}


inline fun <T> Conclusion<T>.onFailure(action: (exception: Throwable) -> Unit): Conclusion<T> {
    if (this is Conclusion.Failure) {
        action.invoke(exception)
    }
    return this
}