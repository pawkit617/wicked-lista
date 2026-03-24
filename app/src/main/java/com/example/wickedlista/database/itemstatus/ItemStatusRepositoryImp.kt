package com.example.wickedlista.database.itemstatus

import com.example.wickedlista.database.saveditems.SavedItems
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ItemStatusRepositoryImp @Inject constructor(private val itemStatusDao: ItemStatusDao): ItemStatusRepository {
    override suspend fun addItemStatus(itemStatus: ItemStatus) = itemStatusDao.addItemStatus(itemStatus)

    override fun getItemStatusForOwnerId(ownerId: Int): Flow<List<ItemStatus>> = itemStatusDao.getStatusesForSavedItem(ownerId)
}