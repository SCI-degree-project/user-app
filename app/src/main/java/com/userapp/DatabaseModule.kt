package com.userapp

import android.content.Context
import androidx.room.Room
import com.userapp.database.AppDatabase
import com.userapp.database.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "products_db"
        )
            .fallbackToDestructiveMigration(true)
            .build()

    }

    @Provides
    fun provideMovieDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }
}