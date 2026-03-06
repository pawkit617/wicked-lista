package com.example.wickedlista.database.homecategories

import com.example.wickedlista.database.savedlists.SavedLists
import kotlinx.coroutines.flow.Flow

interface HomeCategoriesRepository {

    fun getAllHomeCategoriesStream(): Flow<List<HomeCategories>>

    suspend fun addHomeCategories(homeCategories: HomeCategories): Long

    suspend fun addInitialListForCategory(savedLists: SavedLists): Long

    suspend fun deleteHomeCategory(homeCategoriesId: Int)
}