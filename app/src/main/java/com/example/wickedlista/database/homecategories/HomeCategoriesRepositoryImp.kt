package com.example.wickedlista.database.homecategories

import com.example.wickedlista.database.savedlists.SavedLists
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeCategoriesRepositoryImp @Inject constructor(val homeCategoriesDao: HomeCategoriesDao): HomeCategoriesRepository {
    override fun getAllHomeCategoriesStream(): Flow<List<HomeCategories>> = homeCategoriesDao.getAllHomeCategories()

    override suspend fun addHomeCategories(homeCategories: HomeCategories) = homeCategoriesDao.insertNewHomeCategories(homeCategories)

    override suspend fun addInitialListForCategory(savedLists: SavedLists) = homeCategoriesDao.insertInitialListForCategory(savedLists = savedLists)

    override suspend fun deleteHomeCategory(homeCategoriesId: Int) = homeCategoriesDao.deleteHomeCategories(homeCategoriesId)

}