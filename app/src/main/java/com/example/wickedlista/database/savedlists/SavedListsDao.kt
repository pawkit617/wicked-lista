package com.example.wickedlista.database.savedlists

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedListsDao {
    @Query("SELECT * FROM saved_lists WHERE category_id = :categoryId")
    fun getAllSavedListsWithCategoryId(categoryId: Int): Flow<List<SavedLists>>

    @Query("DELETE FROM saved_lists WHERE saved_list_id = :savedListId")
    suspend fun deleteOwnerWithId(savedListId: Int)

    @Insert(onConflict = ABORT)
    suspend fun addOwnerWithCategoryId(savedLists: SavedLists): Long
}