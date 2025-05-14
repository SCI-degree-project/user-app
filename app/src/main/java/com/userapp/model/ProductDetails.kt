package com.userapp.model

data class ProductDetails(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val materials: List<String>,
    val style: String,
    val tenantId: String,
    val gallery: List<String>,
    val model: String
)

