package com.example.wickedlista.database.itemstatus

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemStatusDao {
    @Insert(onConflict = IGNORE)
    suspend fun addItemStatus(itemStatus: ItemStatus): Long

    @Query("SELECT * FROM item_statuses WHERE saved_list_foreign_id = :savedListId")
    fun getStatusesForSavedItem(savedListId: Int): Flow<List<ItemStatus>>

}