package com.example.wickedlista.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.database.homecategories.HomeCategoriesDao
import com.example.wickedlista.database.itemstatus.ItemStatus
import com.example.wickedlista.database.itemstatus.ItemStatusDao
import com.example.wickedlista.database.itemstatuschecked.ItemStatusChecked
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedDao
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.database.savedlists.SavedListsDao

@Database(entities = [HomeCategories::class, SavedLists::class, SavedItems::class, ItemStatus::class, ItemStatusChecked::class], version = 19, exportSchema = false)
abstract class WickedListaDatabase: RoomDatabase() {
    abstract fun homeCategoriesDao(): HomeCategoriesDao
    abstract fun savedListsDao(): SavedListsDao
    abstract fun savedItemsDao(): SavedItemsDao
    abstract fun itemStatusDao(): ItemStatusDao
    abstract fun itemStatusCheckedDao(): ItemStatusCheckedDao
}