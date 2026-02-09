package com.example.wickedlista.database

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeListsRepositoryImp @Inject constructor(val homeListsDao: HomeListsDao): HomeListsRepository {
    override fun getAllHomeListsStream(): Flow<List<HomeLists>> = homeListsDao.getAllHomeLists()

    //override suspend fun addHomeList(homeLists: HomeLists) = homeListsDao.insertNewList(homeLists)
}