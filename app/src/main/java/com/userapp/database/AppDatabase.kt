package com.userapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.userapp.database.dao.ProductDao
import com.userapp.database.entities.FavoriteProductEntity

@Database(
    entities = [FavoriteProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
}