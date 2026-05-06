package com.example.wickedlista.ui.viewmodels

import android.database.sqlite.SQLiteException
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedDao
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.database.savedlists.SavedListsDao
import com.example.wickedlista.database.savedlists.SavedListsRepositoryImp
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
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
class SavedListViewModelTest {

    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var savedListsDao: SavedListsDao
    @Mock
    private lateinit var savedItemsDao: SavedItemsDao

    @Mock lateinit var itemStatusCheckedDao: ItemStatusCheckedDao
    private lateinit var savedListsRepositoryImp: SavedListsRepositoryImp
    private lateinit var itemsRepositoryImp: SavedItemsRepositoryImp
    private lateinit var savedListViewModel: SavedListViewModel

    private lateinit var itemStatusCheckedRepositoryImp: ItemStatusCheckedRepositoryImp

    @Before
    fun setUp() {
        Dispatchers.setMain(unconfinedTestDispatcher)
        MockitoAnnotations.openMocks(this)
        savedListsDao = mockk<SavedListsDao>()
        savedItemsDao = mockk<SavedItemsDao>()
        savedListsRepositoryImp = SavedListsRepositoryImp(savedListsDao)
        itemsRepositoryImp = SavedItemsRepositoryImp(savedItemsDao)
        itemStatusCheckedRepositoryImp = ItemStatusCheckedRepositoryImp(itemStatusCheckedDao)
        savedListViewModel = SavedListViewModel(
            savedListsRepositoryImp,
            itemsRepositoryImp,
            itemStatusCheckedRepositoryImp
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun add_Owner() = runTest {
        savedListViewModel.addOwnerTextFieldState.edit {
            replace(0, length, "Blueberry")
        }

        coEvery {savedListsDao.addOwnerWithCategoryId(any())} returns 1L

        savedListViewModel.addOwner(1, "Blueberry")
        assertFalse(savedListViewModel.uiState.value.showAddOwnerDialog)
    }

    @Test
    fun add_Owner_Blank_Error()  {
        savedListViewModel.addOwnerTextFieldState.edit {
            replace(0, length, "")
        }

        savedListViewModel.addOwner(1, "Blueberry")
        assertTrue(savedListViewModel.uiState.value.hasBlankError)
    }

    @Test
    fun add_Owner_SQL_Error() = runTest {
        savedListViewModel.addOwnerTextFieldState.edit {
            replace(0, length, "Blueberry")
        }

        coEvery {savedListsDao.addOwnerWithCategoryId(any())} throws SQLiteException()

        savedListViewModel.addOwner(1, "Blueberry")
        assertTrue(savedListViewModel.uiState.value.hasSQLError)
    }

    @Test
    fun get_All_Saved_Lists_For_Category_Id() = runTest {
        coEvery {savedListsDao.getAllSavedListsWithCategoryId(1)} returns flowOf(listOf(
            SavedLists(
                1,
                1,
                "Blueberry"
            )
        ))
        savedListViewModel.getAllSavedListsForCategoryId(1)
        assertTrue(savedListViewModel.uiState.value.allSavedLists.isNotEmpty())
    }

    @Test
    fun delete_Owner() = runTest {
        coEvery {savedListsDao.deleteOwnerWithId(1)} returns Unit

        savedListViewModel.deleteOwner(1)
        assertTrue(!savedListViewModel.uiState.value.showDeletionDialog)
    }

    @Test
    fun get_All_Saved_Items_For_Selected_Owner() = runTest {
        savedListViewModel.setSelectedOwner(Pair(1, "Blueberry"))

        coEvery { savedItemsDao.getAllSavedItemForListId(1) } returns flowOf(
            listOf(
                SavedItems (
                    1,
                    1,
                    "Lemons",
                    "Lemons are tasty",
                    "Bought"
                )
            )
        )
        savedListViewModel.getAllSavedItemsForSelectedOwner()
        assertFalse(savedListViewModel.uiState.value.showHintScreenToAddItems)
    }

    @Test
    fun get_All_Saved_Items_For_Selected_Owner_Empty_List() = runTest {
        savedListViewModel.setSelectedOwner(Pair(1, "Blueberry"))

        coEvery { savedItemsDao.getAllSavedItemForListId(1) } returns flowOf(
            listOf()
        )
        savedListViewModel.getAllSavedItemsForSelectedOwner()
        assertTrue(savedListViewModel.uiState.value.showHintScreenToAddItems)
    }

    @Test
    fun set_Selected_Owner() {
        val expectedPair = Pair(3, "Blueberry")
        savedListViewModel.setSelectedOwner(Pair(3, "Blueberry"))
        assertTrue(expectedPair == savedListViewModel.uiState.value.selectedOwner)
    }

    @Test
    fun set_show_add_owner_dialog() {
        savedListViewModel.setShowAddOwner(true)
        assertTrue(savedListViewModel.uiState.value.showAddOwnerDialog)
    }
    @Test
    fun set_show_deletion_owner() {
        savedListViewModel.setShowDeletionOwner(true)
        assertTrue(savedListViewModel.uiState.value.showDeletionDialog)
    }

    @Test
    fun set_Show_Deletion_Of_Last_Owner_Warning() {
        savedListViewModel.setShowDeletionOfLastOwnerWarning(true)
        assertTrue(savedListViewModel.uiState.value.showDeletionOfLastOwnerDialog)
    }

    @Test
    fun clear_Errors() {
        savedListViewModel.clearErrors()
        val hasBlankError = savedListViewModel.uiState.value.hasBlankError
        val hasSQLError = savedListViewModel.uiState.value.hasSQLError
        assertTrue(!hasSQLError && !hasBlankError)
    }

    @Test
    fun clear_Text_Field_State() {
        savedListViewModel.clearTextFieldState()
        val text = savedListViewModel.addOwnerTextFieldState.text
        assertTrue(text.isEmpty())
    }
    /*


    fun clearTextFieldState() {
        addOwnerTextFieldState.clearText()
    }

     */
}