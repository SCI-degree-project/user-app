package com.userapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.userapp.database.dao.ProductDao
import com.userapp.database.dao.SearchHistoryDao
import com.userapp.database.entities.FavoriteProductEntity
import com.userapp.database.entities.SearchHistoryEntity

@Database(
    entities = [FavoriteProductEntity::class, SearchHistoryEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}