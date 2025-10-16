package com.userapp.model

data class ProductDetails(
    val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val materials: List<String>,
    val style: String?,
    val tenantId: String,
    val media: MediaDetails,
    val dimensions: DimensionsDetails?
)

data class DimensionsDetails(
    val width: Double,
    val height: Double,
    val depth: Double
)

data class MediaDetails(
    val gallery: List<GalleryItemDetails>,
    val model: String
)

data class GalleryItemDetails(
    val imageUrl: String
)

