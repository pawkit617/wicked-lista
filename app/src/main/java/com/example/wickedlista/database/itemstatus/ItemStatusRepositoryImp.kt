package com.example.wickedlista.database.itemstatus

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemStatusRepositoryImp @Inject constructor(val itemStatusDao: ItemStatusDao): ItemStatusRepository {
    override suspend fun addItemStatus(itemStatus: ItemStatus) = itemStatusDao.addItemStatus(itemStatus)

    override fun getItemStatusForOwnerId(ownerId: Int): Flow<List<ItemStatus>> = itemStatusDao.getStatusesForSavedItem(ownerId)
}