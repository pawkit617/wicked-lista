package com.example.wickedlista.database.savedlists

import kotlinx.coroutines.flow.Flow

interface SavedListsRepository {
    fun getAllSavedListsWithCategoryId(categoryId: Int): Flow<List<SavedLists>>

    suspend fun addOwner(savedLists: SavedLists): Long

    suspend fun deleteOwner(savedListId: Int)
}