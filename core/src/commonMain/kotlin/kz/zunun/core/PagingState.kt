package kz.zunun.core

sealed class PagingState<out T> {
    data class Success<T>(val isEnd: Boolean, val data: List<T>) : PagingState<T>()
    data class Error(val failure: Throwable) : PagingState<Nothing>()
    data object Loading : PagingState<Nothing>()
}

fun <T> List<T>.pagingSuccess(isEnd: Boolean): PagingState.Success<T> {
    return PagingState.Success(isEnd = isEnd, data = this)
}