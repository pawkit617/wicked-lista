package com.example.wickedlista.ui.viewmodels

import android.database.sqlite.SQLiteException
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.database.homecategories.HomeCategoriesDao
import com.example.wickedlista.database.homecategories.HomeCategoriesRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest  {
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var homeCategoriesDao: HomeCategoriesDao


    //Curtis - it is not necessary to  coEvery {wickedListaDatabase.homeCategoriesDao.insertNewHomeCategories(any())} returns 1L
    //          i can use the DAO without the database reference
//    @Mock
//    private lateinit var wickedListaDatabase: WickedListaDatabase

    private lateinit var homeCategoriesRepositoryImp: HomeCategoriesRepositoryImp
    private lateinit var homeScreenViewModel: HomeScreenViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(unconfinedTestDispatcher)
        MockitoAnnotations.openMocks(this)
        homeCategoriesDao = mockk<HomeCategoriesDao>()
        homeCategoriesRepositoryImp = HomeCategoriesRepositoryImp(homeCategoriesDao)
        homeScreenViewModel = HomeScreenViewModel(homeCategoriesRepositoryImp)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun create_New_List_Success() = runTest {
        coEvery {homeCategoriesDao.insertNewHomeCategories(any())} returns 1L
        coEvery {homeCategoriesDao.insertInitialListForCategory(any()) } returns 1L

        homeScreenViewModel.createNewList("Recipes", "Pies", "Custard")
        val noCategoryError = homeScreenViewModel.uiState.value.isError
        assertTrue(!noCategoryError)
    }

    @Test
    fun create_New_List_No_List_For_Error() {
        homeScreenViewModel.createNewList("Recipes", "Pies", "")
        val noCategoryError = homeScreenViewModel.uiState.value.hasNoListFor
        assertTrue(noCategoryError)
    }


    @Test
    fun create_New_List_No_Category_Error() {
        homeScreenViewModel.createNewList("", "Pies", "Custard")
        val noCategoryError = homeScreenViewModel.uiState.value.isError
        assertTrue(noCategoryError)
    }

    @Test
    fun create_New_List_SQL_Error() = runTest {
        coEvery {homeCategoriesDao.insertNewHomeCategories(any())} throws SQLiteException()
        homeScreenViewModel.createNewList("Recipes", "Pies", "Custard")
        assertTrue(homeScreenViewModel.uiState.value.hasSQLError)
    }

    @Test
    fun get_Lists() = runTest {
        coEvery {homeCategoriesDao.getAllHomeCategories()} returns flowOf(listOf(HomeCategories(1, "Recipes", "Pies")))
        homeScreenViewModel.getLists()
        assertTrue(!homeScreenViewModel.uiState.value.showNoListMessage)

        coEvery {homeCategoriesDao.getAllHomeCategories()} returns flowOf(listOf())
        homeScreenViewModel.getLists()
        assertTrue(homeScreenViewModel.uiState.value.showNoListMessage)
    }

    @Test
    fun delete_Home_List() = runTest {
        coEvery {homeCategoriesDao.deleteHomeCategories(any())} returns Unit

        homeScreenViewModel.setCurrentlySelectedHomeList(1, "Recipes", "Pies")
        homeScreenViewModel.deleteHomeList()
        assertTrue(!homeScreenViewModel.uiState.value.showDeletionDialog)
    }

    @Test
    fun set_deletion_dialog_Visibility() {
        homeScreenViewModel.setDeletionDialogVisibility(true)
        val shouldShow = homeScreenViewModel.uiState.value.showDeletionDialog
        assertTrue(shouldShow)
    }

    @Test
    fun set_create_dialog_Visibility() {
        homeScreenViewModel.setCreateDialogVisibility(true)
        val shouldShow = homeScreenViewModel.uiState.value.showCreateDialog
        assertTrue(shouldShow)
    }


    @Test
    fun set_Currently_Selected_Home_List() {
        homeScreenViewModel.setCurrentlySelectedHomeList(1, "Recipes", "Pies")
        val (first, second, third) = homeScreenViewModel.uiState.value.currentlySelectedHomeList
        assertTrue(first == 1 && second == "Recipes" && third == "Pies")
    }

    @Test
    fun clear_Errors() {
        homeScreenViewModel.clearErrors()
        val sqlError = homeScreenViewModel.uiState.value.hasSQLError
        val isError = homeScreenViewModel.uiState.value.isError
        val hasNoList =  homeScreenViewModel.uiState.value.hasNoListFor

        assertTrue(!sqlError && !isError && !hasNoList)
    }

    @Test
    fun clear_Text_Field_States() {
        homeScreenViewModel.clearTextFieldStates()
        val categoryText = homeScreenViewModel.categorySavedState.text
        val topicText =  homeScreenViewModel.topicSavedState.text
        val listFor =  homeScreenViewModel.listForSavedState.text

        assertTrue(
            categoryText.isEmpty() && topicText.isEmpty() && listFor.isEmpty()
        )
    }
}