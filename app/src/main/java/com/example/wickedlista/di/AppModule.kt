package com.example.wickedlista.di

import android.content.Context
import androidx.room.Room
import com.example.wickedlista.database.WickedListaDatabase
import com.example.wickedlista.database.homecategories.HomeCategoriesDao
import com.example.wickedlista.database.homecategories.HomeCategoriesRepositoryImp
import com.example.wickedlista.database.itemstatus.ItemStatusDao
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedDao
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import com.example.wickedlista.database.savedlists.SavedListsDao
import com.example.wickedlista.database.savedlists.SavedListsRepositoryImp
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
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    @Singleton
    fun provideHomeListDao(wickedListaDatabase: WickedListaDatabase): HomeCategoriesDao {
        return wickedListaDatabase.homeCategoriesDao()
    }

    @Provides
    @Singleton
    fun provideHomeListRepositoryImp(homeCategoriesDao: HomeCategoriesDao): HomeCategoriesRepositoryImp {
        return HomeCategoriesRepositoryImp(homeCategoriesDao)
    }

    @Provides
    @Singleton
    fun provideSavedListsDao(wickedListaDatabase: WickedListaDatabase): SavedListsDao {
        return wickedListaDatabase.savedListsDao()
    }

    @Provides
    @Singleton
    fun provideSavedListsRepositoryImp(savedListsDao: SavedListsDao): SavedListsRepositoryImp {
        return SavedListsRepositoryImp(savedListsDao)
    }

    @Provides
    @Singleton
    fun provideSavedItemDao(wickedListaDatabase: WickedListaDatabase): SavedItemsDao {
        return wickedListaDatabase.savedItemsDao()
    }

    @Provides
    @Singleton
    fun provideSavedItemsRepositoryImp(savedItemsDao: SavedItemsDao): SavedItemsRepositoryImp {
        return SavedItemsRepositoryImp(savedItemsDao)
    }

    @Provides
    @Singleton
    fun providesItemStatusDao(wickedListaDatabase: WickedListaDatabase): ItemStatusDao {
        return wickedListaDatabase.itemStatusDao()
    }

    @Provides
    @Singleton
    fun providesItemStatusCheckedDao(wickedListaDatabase: WickedListaDatabase): ItemStatusCheckedDao {
        return wickedListaDatabase.itemStatusCheckedDao()
    }
}