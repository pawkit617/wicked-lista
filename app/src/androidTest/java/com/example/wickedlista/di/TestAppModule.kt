package com.example.wickedlista.di

import android.content.Context
import androidx.room.Room
import com.example.wickedlista.database.WickedListaDatabase
import com.example.wickedlista.database.homecategories.HomeCategoriesDao
import com.example.wickedlista.database.itemstatus.ItemStatusDao
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.savedlists.SavedListsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {
    @Provides
    @Singleton
    fun provideInMemoryTestingDataBase(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(
            context,
            WickedListaDatabase::class.java
        ).build()

    @Provides
    @Singleton
    fun provideHomeCategoriesDao(database: WickedListaDatabase): HomeCategoriesDao {
        return database.homeCategoriesDao()
    }

    @Provides
    @Singleton
    fun provideSavedListsDao(database: WickedListaDatabase): SavedListsDao {
        return database.savedListsDao()
    }

    @Provides
    @Singleton
    fun provideSavedItemsDao(database: WickedListaDatabase): SavedItemsDao {
        return database.savedItemsDao()
    }

    @Provides
    @Singleton
    fun provideItemStatusDao(database: WickedListaDatabase): ItemStatusDao {
        return database.itemStatusDao()
    }
}