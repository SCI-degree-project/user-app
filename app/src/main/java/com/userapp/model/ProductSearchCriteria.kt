package com.userapp.model

data class ProductSearchCriteria(
    val name: String? = null,
    val style: String? = null,
    val materials: List<String> = emptyList(),
    val sortBy: String? = null,
    val direction: String? = null,
    val page: Int = 0,
    val size: Int = 20
)
