package com.example.wickedlista.database.saveditems

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedItemsRepositoryImp @Inject constructor(val savedItemsDao: SavedItemsDao): SavedItemsRepository {
    override suspend fun addItemToList(savedItems: SavedItems): Long = savedItemsDao.addItemToList(savedItems)

    override suspend fun updateSavedItem(savedItems: SavedItems): Int = savedItemsDao.updateSavedItem(savedItems)

    override fun getAllSavedItemsForListId(savedListId: Int): Flow<List<SavedItems>> = savedItemsDao.getAllSavedItemForListId(savedListId)

    override suspend fun deleteSavedItem(savedItemId: Int): Int = savedItemsDao.deleteSavedItem(savedItemId)
}