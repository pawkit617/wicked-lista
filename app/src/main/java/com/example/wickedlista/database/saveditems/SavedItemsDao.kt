package com.example.wickedlista.database.saveditems

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemsDao {
    @Insert(onConflict = ABORT)
    suspend fun addItemToList(savedItems: SavedItems): Long

    @Update
    suspend fun updateSavedItem(savedItems: SavedItems): Int

    @Query("SELECT * FROM saved_items WHERE saved_list_foreign_id = :savedListId")
    fun getAllSavedItemForListId(savedListId: Int): Flow<List<SavedItems>>

   @Query("DELETE FROM saved_items WHERE saved_item_id = :savedItemId")
    suspend fun deleteSavedItem(savedItemId: Int): Int
}