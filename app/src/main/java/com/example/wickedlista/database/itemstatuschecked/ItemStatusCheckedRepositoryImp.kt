package com.example.wickedlista.database.itemstatuschecked

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemStatusCheckedRepositoryImp @Inject constructor(
    val itemStatusCheckedDao: ItemStatusCheckedDao
) : ItemStatusCheckedRepository {
    override suspend fun addItemStatusChecked(itemStatusChecked: ItemStatusChecked): Long =
        itemStatusCheckedDao.addItemStatusChecked(itemStatusChecked)

    override fun getStatusesForSavedItem(savedListId: Int): Flow<List<ItemStatusChecked>> =
        itemStatusCheckedDao.getStatusesForSavedItem(savedListId)
}