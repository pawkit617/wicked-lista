package com.example.wickedlista.database.saveditems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemsDao {
    @Insert(onConflict = ABORT)
    suspend fun addItemToList(savedItems: SavedItems): Long

    @Query("SELECT * FROM saved_items WHERE saved_list_foreign_id = :savedListId")
    fun getAllSavedItemForListId(savedListId: Int): Flow<List<SavedItems>>
}