package com.example.wickedlista.database.itemstatuschecked

import kotlinx.coroutines.flow.Flow

interface ItemStatusCheckedRepository {
    suspend fun addItemStatusChecked(itemStatusChecked: ItemStatusChecked): Long
    fun getStatusesForSavedItem(savedListId: Int): Flow<List<ItemStatusChecked>>

    suspend fun updateItemStatusChecked(itemStatusChecked: ItemStatusChecked)
}