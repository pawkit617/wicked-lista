package com.example.wickedlista.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.database.homecategories.HomeCategoriesDao
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.database.savedlists.SavedListsDao

@Database(entities = [HomeCategories::class, SavedLists::class, SavedItems::class], version = 9, exportSchema = false)
abstract class WickedListaDatabase: RoomDatabase() {
    abstract fun homeCategoriesDao(): HomeCategoriesDao
    abstract fun savedListsDao(): SavedListsDao
}

