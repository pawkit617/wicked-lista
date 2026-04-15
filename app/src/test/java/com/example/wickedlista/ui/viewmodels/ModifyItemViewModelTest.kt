package com.example.wickedlista.ui.viewmodels

import com.example.wickedlista.database.itemstatus.ItemStatus
import com.example.wickedlista.database.itemstatus.ItemStatusDao
import com.example.wickedlista.database.itemstatus.ItemStatusRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
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

class ModifyItemViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var savedItemsDao: SavedItemsDao
    @Mock
    private lateinit var itemStatusDao: ItemStatusDao

    private lateinit var savedItemsRepositoryImp: SavedItemsRepositoryImp
    private lateinit var itemStatusRepository: ItemStatusRepositoryImp

    private lateinit var modifyItemViewModel: ModifyItemViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(unconfinedTestDispatcher)
        MockitoAnnotations.openMocks(this)
        savedItemsDao = mockk<SavedItemsDao>()
        itemStatusDao = mockk<ItemStatusDao>()
        savedItemsRepositoryImp = SavedItemsRepositoryImp(savedItemsDao)
        itemStatusRepository = ItemStatusRepositoryImp(itemStatusDao)
        modifyItemViewModel = ModifyItemViewModel(
            savedItemsRepositoryImp,
            itemStatusRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun add_Item_To_List() = runTest {
        modifyItemViewModel.initialStatusTextFieldState.edit {
            replace(0, length, "Buy")
        }
        modifyItemViewModel.additionalStatusTextFieldState.edit {
            replace(0, length, "Bought")
        }
        modifyItemViewModel.labelTextFieldState.edit {
            replace(0, length, "Lemons")
        }

        coEvery {savedItemsDao.addItemToList(any())} returns 1L
        coEvery {itemStatusDao.addItemStatus(any())} returns 1L

        modifyItemViewModel.addItemToListWithId(1)
        assertTrue(modifyItemViewModel.uiState.value.showSuccessAddMoreItemDialog)
    }

    @Test
    fun add_Item_To_List_With_Menu_Status() = runTest {
        modifyItemViewModel.statusTextFieldForMenuState.edit {
            replace(0, length, "Buy")
        }
        modifyItemViewModel.labelTextFieldState.edit {
            replace(0, length, "Lemons")
        }


        coEvery {savedItemsDao.addItemToList(any())} returns 1L
        coEvery {itemStatusDao.addItemStatus(any())} returns 1L

        modifyItemViewModel.addItemToListWithId(1, true)
        assertTrue(modifyItemViewModel.uiState.value.showSuccessAddMoreItemDialog)
    }

    @Test
    fun update_Saved_Item_Check_For_Filled_Label() = runTest {
        modifyItemViewModel.labelTextFieldState.edit {
            replace(0, length, "Lemons")
        }
        coEvery {savedItemsDao.updateSavedItem(any())} returns 1
        modifyItemViewModel.updateSavedItem(1, 1)
        assertFalse(modifyItemViewModel.uiState.value.hasBlankLabelError)
    }

    @Test
    fun update_Saved_Item_Check_Has_Blank_Label() = runTest {
        coEvery {savedItemsDao.updateSavedItem(any())} returns 1
        modifyItemViewModel.updateSavedItem(1, 1)
        assertTrue(modifyItemViewModel.uiState.value.hasBlankLabelError)
    }

    @Test
    fun delete_Saved_Item() = runTest {
        coEvery {savedItemsDao.deleteSavedItem(any())} returns 1
        val rid = modifyItemViewModel.deleteSavedItem(1)
        assertTrue(rid == 1)
    }

    @Test
    fun update_Statuses_For_Item() = runTest {
        modifyItemViewModel.initialStatusTextFieldState.edit {
            replace(0, length, "Buy")
        }
        modifyItemViewModel.additionalStatusTextFieldState.edit {
            replace(0, length, "Bought")
        }
        modifyItemViewModel.additionalStatus2TextFieldState.edit {
            replace(0, length, "Found")
        }

        coEvery {itemStatusDao.getStatusesForSavedItem(any())} returns flowOf(
            listOf(ItemStatus(1,
                1,
                "Buy",
                "Bought",
                "Found",
                "")))

        modifyItemViewModel.updateStatusesForItem(1)
        val statuses = modifyItemViewModel.uiState.value.itemStatuses
        assertTrue(statuses.size == 3)
    }

    @Test
    fun prepopulate_Form_With_Item_Info() = runTest {
        modifyItemViewModel.initialStatusTextFieldState.edit {
            replace(0, length, "Buy")
        }
        modifyItemViewModel.additionalStatusTextFieldState.edit {
            replace(0, length, "Bought")
        }
        modifyItemViewModel.additionalStatus2TextFieldState.edit {
            replace(0, length, "Found")
        }
        coEvery {itemStatusDao.getStatusesForSavedItem(any())} returns flowOf(
            listOf(ItemStatus(1,
                1,
                "Buy",
                "Bought",
                "Found",
                "")))
        modifyItemViewModel.prepopulateFormWithItemInfo(1, "Lemons", "Lemons are tasty", "Bought")
        assertTrue(modifyItemViewModel.uiState.value.itemStatuses.size == 3)
    }

    @Test
    fun set_Show_Success_Add_More_Item_Dialog() {
        modifyItemViewModel.setShowSuccessAddMoreItemDialog(true)
        assertTrue(modifyItemViewModel.uiState.value.showSuccessAddMoreItemDialog)
    }

    @Test
    fun set_Use_Menu_For_Status() {
        modifyItemViewModel.setUseMenuForStatus(true)
        assertTrue(modifyItemViewModel.uiState.value.useMenuForStatus)
    }

    @Test
    fun clear_Text_Field_States() {
        modifyItemViewModel.clearTextFieldStates()
        val textList = listOf(
            modifyItemViewModel.labelTextFieldState.text,
            modifyItemViewModel.descTextFieldState.text,
            modifyItemViewModel.initialStatusTextFieldState.text,
            modifyItemViewModel.additionalStatusTextFieldState.text,
            modifyItemViewModel.additionalStatus2TextFieldState.text,
            modifyItemViewModel.additionalStatus3TextFieldState.text,
            modifyItemViewModel.statusTextFieldForMenuState.text
        )

        assertTrue(textList.filter { it.isNotEmpty() }.size == 0)
    }
}