package com.example.wickedlista.database.saveditems

import kotlinx.coroutines.flow.Flow

interface SavedItemsRespository {
    suspend fun addItemToList(savedItems: SavedItems): Long

    fun getAllSavedItemsForListId(savedListId: Int): Flow<List<SavedItems>>
}