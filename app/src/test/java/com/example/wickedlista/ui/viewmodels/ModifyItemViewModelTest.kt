package com.example.wickedlista.ui.viewmodels

import com.example.wickedlista.database.itemstatus.ItemStatusDao
import com.example.wickedlista.database.itemstatus.ItemStatusRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ModifyItemViewModelTest {
    @Mock
    private lateinit var savedItemsDao: SavedItemsDao
    @Mock
    private lateinit var itemStatusDao: ItemStatusDao

    private lateinit var savedItemsRepositoryImp: SavedItemsRepositoryImp
    private lateinit var itemStatusRepository: ItemStatusRepositoryImp

    private lateinit var modifyItemViewModel: ModifyItemViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savedItemsRepositoryImp = SavedItemsRepositoryImp(savedItemsDao)
        itemStatusRepository = ItemStatusRepositoryImp(itemStatusDao)
        modifyItemViewModel = ModifyItemViewModel(
            savedItemsRepositoryImp,
            itemStatusRepository
        )
    }

    @Test
    fun set_Show_Success_Add_More_Item_Dialog() {
        modifyItemViewModel.setShowSuccessAddMoreItemDialog(true)
        assertTrue(modifyItemViewModel.uiState.value.showSuccessAddMoreItemDialog)
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