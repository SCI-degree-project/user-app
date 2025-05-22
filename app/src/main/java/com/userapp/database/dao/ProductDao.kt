package com.userapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.userapp.database.entities.FavoriteProductEntity

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(movie: FavoriteProductEntity)

    @Query("SELECT * FROM favorite_products")
    suspend fun getFavoriteProducts(): List<FavoriteProductEntity>
}