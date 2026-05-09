package com.example.wickedlista.database.saveditems

import kotlinx.coroutines.flow.Flow

interface SavedItemsRepository {
    suspend fun addItemToList(savedItems: SavedItems): Long

    suspend fun updateSavedItem(savedItems: SavedItems): Int

    fun getAllSavedItemsForListId(savedListId: Int): Flow<List<SavedItems>>

    suspend fun deleteSavedItem(savedItemId: Int): Int
    suspend fun updateSavedItemForCheckboxOnly(savedItem: SavedItems)
}