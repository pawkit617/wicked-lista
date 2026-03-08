package com.example.wickedlista.ui.viewmodels

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.sqlite.SQLiteException
import com.example.wickedlista.data.SavedListUIState
import com.example.wickedlista.database.homecategories.HomeCategoriesRepositoryImp
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
class SavedListViewModel @Inject constructor(val savedListsRepositoryImp: SavedListsRepositoryImp): ViewModel() {
    private val _uiState = MutableStateFlow(SavedListUIState())
    val uiState: StateFlow<SavedListUIState> = _uiState.asStateFlow()

    val addOwnerTextFieldState = TextFieldState("")

    fun setShowAddOwner(shouldShow: Boolean) {
        _uiState.update {
            it.copy(showAddOwnerDialog = shouldShow)
        }
    }

    fun setSelectedOwnerId(ownerId: Int) {
        _uiState.update {
            it.copy(
                selectedOwnerId = ownerId
            )
        }
    }

    fun getAllSavedListsForCategoryId(categoryId: Int) {
        viewModelScope.launch {
            val listsForCategoryId =
                savedListsRepositoryImp.getAllSavedListsWithCategoryId(categoryId)
            val theFirst = listsForCategoryId.first()
            _uiState.update {
                it.copy(
                    allSavedLists = theFirst,
                    selectedOwnerId = theFirst.first().savedListId
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
                    savedListsRepositoryImp.addOwner(newSavedLists)
                    clearTextFieldState()
                    clearErrors()
                    _uiState.update {
                        it.copy(showAddOwnerDialog = false)
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
