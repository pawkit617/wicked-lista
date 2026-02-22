package com.example.wickedlista.database

import kotlinx.coroutines.flow.Flow

interface HomeListsRepository {
    suspend fun getAllHomeListsStreamX(): List<HomeLists>


    fun getAllHomeListsStream(): Flow<List<HomeLists>>

    suspend fun addHomeList(homeLists: HomeLists): Long

    suspend fun deleteHomeList(homeListId: Int)
}