package com.example.wickedlista.ui.viewmodels

import com.example.wickedlista.database.itemstatus.ItemStatus
import com.example.wickedlista.database.itemstatus.ItemStatusDao
import com.example.wickedlista.database.itemstatus.ItemStatusRepositoryImp
import com.example.wickedlista.database.itemstatuschecked.ItemStatusChecked
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedDao
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import com.example.wickedlista.database.saveditems.StatusType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ModifyItemViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var savedItemsDao: SavedItemsDao
    @Mock
    private lateinit var itemStatusDao: ItemStatusDao

    @Mock
    private lateinit var itemStatusCheckedDao: ItemStatusCheckedDao

    private lateinit var savedItemsRepositoryImp: SavedItemsRepositoryImp
    private lateinit var itemStatusRepositoryImp: ItemStatusRepositoryImp
    private lateinit var itemStatusCheckedRepositoryImp: ItemStatusCheckedRepositoryImp

    private lateinit var modifyItemViewModel: ModifyItemViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(unconfinedTestDispatcher)
        MockitoAnnotations.openMocks(this)
        savedItemsDao = mockk<SavedItemsDao>()
        itemStatusDao = mockk<ItemStatusDao>()
        itemStatusCheckedDao = mockk<ItemStatusCheckedDao>()
        savedItemsRepositoryImp = SavedItemsRepositoryImp(savedItemsDao)
        itemStatusRepositoryImp = ItemStatusRepositoryImp(itemStatusDao)
        itemStatusCheckedRepositoryImp = ItemStatusCheckedRepositoryImp(itemStatusCheckedDao)
        modifyItemViewModel = ModifyItemViewModel(
            savedItemsRepositoryImp,
            itemStatusRepositoryImp,
            itemStatusCheckedRepositoryImp
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
        coEvery { itemStatusDao.getStatusesForSavedItem(any()) } returns flowOf(
            listOf(ItemStatus(1,
                1,
                "Buy",
                "Bought",
                "Found",
                "")))

        coEvery {
            itemStatusCheckedDao.getStatusesForSavedItem(1)
        } returns flowOf(
            listOf()
        )
        modifyItemViewModel.prepopulateFormWithItemInfo(1, "Lemons", "Lemons are tasty", "Bought")
        assertTrue(modifyItemViewModel.uiState.value.itemStatuses.size == 3)
    }

    @Test
    fun prepopulate_Form_With_Item_Info_Checkbox() = runTest {

        coEvery { itemStatusDao.getStatusesForSavedItem(any()) } returns flowOf(
            listOf()
        )

        coEvery {
            itemStatusCheckedDao.getStatusesForSavedItem(1)
        } returns flowOf(
            listOf(
                ItemStatusChecked(
                    1,
                    "Bought",
                    1,
                    1,
                    isChecked = true
                )
            )
        )
        modifyItemViewModel.prepopulateFormWithItemInfo(1, "Lemons", "Lemons are tasty", "Bought")
        assertTrue(modifyItemViewModel.uiState.value.itemStatusCheckboxChecked)
    }

    @Test
    fun add_Item_To_List_With_Id_For_Checked_Label() = runTest {
        modifyItemViewModel.labelTextFieldState.edit {
            replace(0, modifyItemViewModel.labelTextFieldState.text.length, "Lemons")
        }
        modifyItemViewModel.checkboxTextFieldState.edit {
            replace(0, modifyItemViewModel.checkboxTextFieldState.text.length, "Bought")
        }

        coEvery { savedItemsDao.addItemToList(any()) } returns 1L
        coEvery { itemStatusCheckedDao.addItemStatusChecked(any()) } returns 1L

        modifyItemViewModel.addItemToListWithIdForCheckedLabel(1)
        assertTrue(modifyItemViewModel.uiState.value.showSuccessAddMoreItemDialog)
    }

    @Test
    fun check_Items_Statuses_For_OwnerId_Menu_Options() = runTest() {
       coEvery { itemStatusDao.getStatusesForSavedItem(any()) } returns flowOf(
           listOf(ItemStatus(
               1,1,
               "Buy", "Bought",
               "Sold", ""))
       )
       coEvery { itemStatusCheckedDao.getStatusesForSavedItem(any()) } returns flowOf(listOf())

       modifyItemViewModel.checkItemsStatusesForOwnerId(1)
       assertTrue(modifyItemViewModel.uiState.value.itemStatuses.size == 3)
    }

    @Test
    fun check_Items_Statuses_For_OwnerId_Checkbox() = runTest() {
        coEvery { itemStatusDao.getStatusesForSavedItem(any()) } returns flowOf(
            listOf()
        )
        coEvery { itemStatusCheckedDao.getStatusesForSavedItem(any()) } returns flowOf(
            listOf(
                ItemStatusChecked(
                    1,
                    "Bought",
                    1,
                    1,
                    isChecked = true
                )
            )
        )

        modifyItemViewModel.checkItemsStatusesForOwnerId(1)
        assertTrue(modifyItemViewModel.uiState.value.itemStatusCheckboxLabel == "Bought")
    }

    @Test
    fun check_Items_Statuses_For_OwnerId_Unassigned() = runTest() {
        coEvery { itemStatusDao.getStatusesForSavedItem(any()) } returns flowOf(
            listOf()
        )
        coEvery { itemStatusCheckedDao.getStatusesForSavedItem(any()) } returns flowOf(
            listOf()
        )

        modifyItemViewModel.checkItemsStatusesForOwnerId(1)
        assertTrue(modifyItemViewModel.uiState.value.statusType == StatusType.UnassignedStatusType)
    }

    @Test
    fun set_Item_Status_Checkbox_Checked() {
        modifyItemViewModel.setItemStatusCheckboxChecked(true)
        assertTrue(modifyItemViewModel.uiState.value.itemStatusCheckboxChecked)
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
    fun set_Use_Checkbox_Status() {
        modifyItemViewModel.setUseCheckboxStatus(true)
        assertTrue(modifyItemViewModel.uiState.value.useCheckboxStatus)
    }
    @Test
    fun clear_Text_Field_States() {
        modifyItemViewModel.clearInfoTextFieldStates()
        modifyItemViewModel.clearTextFieldStatesForStatusMenu()
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