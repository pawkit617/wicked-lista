package com.example.wickedlista.ui.viewmodels

import com.example.wickedlista.database.saveditems.SavedItemsDao
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import com.example.wickedlista.database.savedlists.SavedListsDao
import com.example.wickedlista.database.savedlists.SavedListsRepositoryImp
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SavedListViewModelTest {
    @Mock
    private lateinit var savedListsDao: SavedListsDao
    @Mock
    private lateinit var savedItemsDao: SavedItemsDao
    private lateinit var savedListsRepositoryImp: SavedListsRepositoryImp
    private lateinit var itemsRepositoryImp: SavedItemsRepositoryImp
    private lateinit var savedListViewModel: SavedListViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        savedListsRepositoryImp = SavedListsRepositoryImp(savedListsDao)
        itemsRepositoryImp = SavedItemsRepositoryImp(savedItemsDao)

        savedListViewModel = SavedListViewModel(
            savedListsRepositoryImp,
            itemsRepositoryImp
        )
    }

    @Test
    fun set_Selected_Owner() {
        val expectedPair = Pair<Int, String>(3, "Blueberry")
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