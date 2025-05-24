package com.userapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteProductEntity (
    @PrimaryKey val productId: String,
    val addedAt: Long = System.currentTimeMillis()
)