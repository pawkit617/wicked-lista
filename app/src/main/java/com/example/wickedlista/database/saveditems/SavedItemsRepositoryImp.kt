package com.example.wickedlista.database.saveditems

import javax.inject.Inject

class SavedItemsRepositoryImp @Inject constructor(val savedItemsDao: SavedItemsDao): SavedItemsRespository {
    override suspend fun addItemToList(savedItems: SavedItems): Long = savedItemsDao.addItemToList(savedItems)
}