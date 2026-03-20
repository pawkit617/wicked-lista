package com.example.wickedlista.database.itemstatus

import kotlinx.coroutines.flow.Flow

interface ItemStatusRepository {

    suspend fun addItemStatus(itemStatus: ItemStatus): Long

    fun getItemStatusForOwnerId(ownerId: Int) : Flow<List<ItemStatus>>
}