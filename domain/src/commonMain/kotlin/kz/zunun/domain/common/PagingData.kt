package kz.zunun.domain.common

data class PagingData<T>(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val total: Int,
    val data: List<T>,
)