package com.example.wickedlista.database.saveditems

interface SavedItemsRespository {
    suspend fun addItemToList(savedItems: SavedItems): Long
}