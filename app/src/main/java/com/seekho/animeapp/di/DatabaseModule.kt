package com.seekho.animeapp.di

import android.content.Context
import androidx.room.Room
import com.seekho.animeapp.data.local.database.AnimeDatabase
import com.seekho.animeapp.data.local.database.dao.AnimeDao
import com.seekho.animeapp.utils.Constants
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
    fun provideAnimeDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AnimeDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao {
        return database.animeDao()
    }
}
