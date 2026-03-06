package com.example.wickedlista.database.savedlists

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavedListsRepositoryImp @Inject constructor(val savedListsDao: SavedListsDao): SavedListsRepository {
    override fun getAllSavedListsWithCategoryId(categoryId: Int): Flow<List<SavedLists>> =
        savedListsDao.getAllSavedListsWithCategoryId(categoryId)

    override suspend fun addOwner(savedLists: SavedLists): Long =
        savedListsDao.addOwnerWithCategoryId(savedLists)

    override suspend fun deleteOwner(savedListId: Int) = savedListsDao.deleteOwnerWithId(savedListId)
}
