package com.example.wickedlista.database

import kotlinx.coroutines.flow.Flow

interface HomeListsRepository {
    fun getAllHomeListsStream(): Flow<List<HomeLists>>

    suspend fun addHomeList(homeLists: HomeLists): Long
}