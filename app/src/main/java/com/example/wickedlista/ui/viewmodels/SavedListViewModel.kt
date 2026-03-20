package com.example.wickedlista.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.SQLiteException
import com.example.wickedlista.data.SavedListUIState
import com.example.wickedlista.database.homecategories.HomeCategoriesRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import com.example.wickedlista.database.savedlists.SavedLists
import com.example.wickedlista.database.savedlists.SavedListsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedListViewModel @Inject constructor(
    val savedListsRepositoryImp: SavedListsRepositoryImp,
    val itemsRepositoryImp: SavedItemsRepositoryImp
): ViewModel() {
    private val _uiState = MutableStateFlow(SavedListUIState())
    val uiState: StateFlow<SavedListUIState> = _uiState.asStateFlow()

    val addOwnerTextFieldState = TextFieldState("")

    //region Owner Operations
    fun getAllSavedListsForCategoryId(categoryId: Int) {
        viewModelScope.launch {
            val listsForCategoryId =
                savedListsRepositoryImp.getAllSavedListsWithCategoryId(categoryId)

            val theFirst = listsForCategoryId.first()
            val currentSelectedId = _uiState.value.selectedOwner

            _uiState.update {
                it.copy(
                    allSavedLists = theFirst,
                    selectedOwner = if (currentSelectedId.first == -1 && theFirst.isNotEmpty()) {
                        Pair(theFirst.first().savedListId, theFirst.first().owner)
                    } else {
                        currentSelectedId
                    }
                )
            }
        }
    }

    fun addOwner(categoryId: Int, newOwner: String) {
        viewModelScope.launch {
            try {
                if (addOwnerTextFieldState.text.isNotEmpty()) {
                    val newSavedLists = SavedLists(
                        homeCategoriesForeignId = categoryId.toLong(),
                        owner = newOwner
                    )
                    val addedId = savedListsRepositoryImp.addOwner(newSavedLists)
                    clearTextFieldState()
                    clearErrors()
                    _uiState.update {
                        it.copy(
                            allSavedLists = listOf(),
                            selectedOwner = Pair(addedId.toInt(), newOwner),
                            showAddOwnerDialog = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(hasBlankError = true)
                    }
                }
            } catch (_: SQLiteException) {
                _uiState.update {
                    it.copy(hasSQLError = true)
                }
            }
        }
    }

    fun deleteOwner(ownerId: Int) {
        viewModelScope.launch {
            savedListsRepositoryImp.deleteOwner(ownerId)
            _uiState.update {
                it.copy(
                    selectedOwner = Pair(-1, ""),
                    allSavedLists = listOf(),
                    showDeletionDialog = false
                )
            }
        }
    }

    fun setSelectedOwner(selectedOwner: Pair<Int, String>) {
        _uiState.update {
            it.copy(
                selectedOwner = selectedOwner
            )
        }
    }

    fun setShowAddOwner(shouldShow: Boolean) {
        _uiState.update {
            it.copy(showAddOwnerDialog = shouldShow)
        }
    }

    fun setShowDeletionOwner(shouldShow: Boolean) {
        _uiState.update {
            it.copy(showDeletionDialog = shouldShow)
        }
    }
    //endregion

    //region List of Items for Owner
    fun getAllSavedItemsForSelectedOwner() {
        val selectedOwner = _uiState.value.selectedOwner
        viewModelScope.launch {
            val listOfItemsForOwnerFlow = itemsRepositoryImp
                .getAllSavedItemsForListId(selectedOwner.first)

            val itemsForListOwner = listOfItemsForOwnerFlow.first()

            _uiState.update {
                val hasNoItems = itemsForListOwner.isEmpty()
                it.copy(
                    allSavedItemsForList = itemsForListOwner,
                    showHintScreenToAddItems = hasNoItems
                )
            }
        }
    }

    //endregion
    fun clearErrors() {
        _uiState.update {
            it.copy(
                hasSQLError = false,
                hasBlankError = false
            )
        }
    }

    fun clearTextFieldState() {
        addOwnerTextFieldState.clearText()
    }
}
