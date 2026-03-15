package com.example.wickedlista.database.saveditems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT

@Dao
interface SavedItemsDao {
    @Insert(onConflict = ABORT)
    suspend fun addItemToList(savedItems: SavedItems): Long
}