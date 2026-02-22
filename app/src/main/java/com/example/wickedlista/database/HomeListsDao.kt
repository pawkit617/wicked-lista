package com.example.wickedlista.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeListsDao {
    @Query("SELECT * FROM home_lists")
    suspend fun getAllHomeListsX(): List<HomeLists>
    @Query("SELECT * FROM home_lists")
    fun getAllHomeLists(): Flow<List<HomeLists>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNewList(homeList: HomeLists): Long

    @Query("DELETE FROM home_lists WHERE id = :homeListId")
    suspend fun deleteHomeList(homeListId: Int)
}

