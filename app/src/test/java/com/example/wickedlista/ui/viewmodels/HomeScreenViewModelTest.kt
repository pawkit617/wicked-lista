package com.example.wickedlista.ui.viewmodels

import com.example.wickedlista.database.homecategories.HomeCategoriesDao
import com.example.wickedlista.database.homecategories.HomeCategoriesRepositoryImp
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class HomeScreenViewModelTest  {

    @Mock
    private lateinit var homeCategoriesDao: HomeCategoriesDao
    private lateinit var homeCategoriesRepositoryImp: HomeCategoriesRepositoryImp
    private lateinit var homeScreenViewModel: HomeScreenViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeCategoriesRepositoryImp = HomeCategoriesRepositoryImp(homeCategoriesDao)
        homeScreenViewModel = HomeScreenViewModel(homeCategoriesRepositoryImp)
    }

    @Test
    fun create_New_List_No_Category_Error() {
        homeScreenViewModel.createNewList("", "Pies", "Custard")
        val noCategoryError = homeScreenViewModel.uiState.value.isError
        assertTrue(noCategoryError)
    }
//     Need UI Instrumentation tests
//    @Test
//    fun create_New_List_No_List_For_Error() {
//        homeScreenViewModel.createNewList("Recipes", "Pies", "")
//        val listForError = homeScreenViewModel.uiState.value.hasNoListFor
//        assertTrue(listForError)
//    }
//
//    @Test
//    fun set_Create_Dialog_Visibility() {
//        homeScreenViewModel.setCreateDialogVisibility(true)
//        val shouldShow = homeScreenViewModel.uiState.value.showCreateDialog
//        assertTrue(shouldShow)
//    }

    @Test
    fun set_deletion_dialog_Visibility() {
        homeScreenViewModel.setDeletionDialogVisibility(true)
        val shouldShow = homeScreenViewModel.uiState.value.showDeletionDialog
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