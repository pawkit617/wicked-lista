package com.example.wickedlista.database.saveditems

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedItemsRepositoryImp @Inject constructor(val savedItemsDao: SavedItemsDao): SavedItemsRespository {
    override suspend fun addItemToList(savedItems: SavedItems): Long = savedItemsDao.addItemToList(savedItems)
    override fun getAllSavedItemsForListId(savedListId: Int): Flow<List<SavedItems>> = savedItemsDao.getAllSavedItemForListId(savedListId)
}