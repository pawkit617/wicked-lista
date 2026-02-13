package com.example.wickedlista.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HomeLists::class], version = 2, exportSchema = false)
abstract class WickedListaDatabase: RoomDatabase() {
    abstract fun homeListDao(): HomeListsDao
}

