package com.example.wickedlista.di

import android.content.Context
import androidx.room.Room
import com.example.wickedlista.database.HomeListsDao
import com.example.wickedlista.database.HomeListsRepositoryImp
import com.example.wickedlista.database.WickedListaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideWickedListaDatabase(@ApplicationContext context: Context): WickedListaDatabase {
        return Room.databaseBuilder(
                context,
                WickedListaDatabase::class.java,
                "wickalista_database"
            ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    @Singleton
    fun provideHomeListDao(wickedListaDatabase: WickedListaDatabase): HomeListsDao {
        return wickedListaDatabase.homeListDao()
    }

    @Provides
    @Singleton
    fun provideHomeListRepositoryImp(homeListsDao: HomeListsDao): HomeListsRepositoryImp {
        return HomeListsRepositoryImp(homeListsDao)
    }
}