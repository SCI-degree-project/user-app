package com.userapp.model

data class PaginatedResponse<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val number: Int,
    val size: Int,
    val last: Boolean
)
