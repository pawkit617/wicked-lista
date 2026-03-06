package com.example.wickedlista.database.homecategories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wickedlista.database.savedlists.SavedLists
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeCategoriesDao {
    @Query("SELECT * FROM home_categories")
    fun getAllHomeCategories(): Flow<List<HomeCategories>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertNewHomeCategories(homeCategories: HomeCategories): Long

    @Query("DELETE FROM home_categories WHERE id = :homeCategoriesId")
    suspend fun deleteHomeCategories(homeCategoriesId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInitialListForCategory(savedLists: SavedLists): Long
}

