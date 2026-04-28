package com.example.wickedlista.database.itemstatuschecked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemStatusCheckedDao {
    @Insert(onConflict = IGNORE)
    suspend fun addItemStatusChecked(itemStatusChecked: ItemStatusChecked): Long

    @Query("SELECT * FROM item_statuses_checked WHERE saved_list_foreign_id = :savedListId")
    fun getStatusesForSavedItem(savedListId: Int): Flow<List<ItemStatusChecked>>

}