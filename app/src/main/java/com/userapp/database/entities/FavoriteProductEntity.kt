package com.userapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "favorite_products")
data class FavoriteProductEntity (
    @PrimaryKey val productId: UUID,
    val tenantId: UUID,
    val addedAt: Long = System.currentTimeMillis()
)