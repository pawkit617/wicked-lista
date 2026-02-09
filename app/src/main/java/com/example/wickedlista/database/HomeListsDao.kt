package com.example.wickedlista.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeListsDao {
    @Query("SELECT * FROM home_lists")
    fun getAllHomeLists(): Flow<List<HomeLists>>

//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    suspend fun insertNewList(homeList: HomeLists)
}

