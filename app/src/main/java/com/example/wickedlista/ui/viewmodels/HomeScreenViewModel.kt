package com.example.wickedlista.ui.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.sqlite.SQLiteException
import com.example.wickedlista.data.HomeScreenUIState
import com.example.wickedlista.database.homecategories.HomeCategories
import com.example.wickedlista.database.homecategories.HomeCategoriesRepositoryImp
import com.example.wickedlista.database.savedlists.SavedLists
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(SavedStateHandleSaveableApi::class)
class HomeScreenViewModel @Inject constructor(val homeCategoriesRepositoryImp: HomeCategoriesRepositoryImp): ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUIState()) //Backing field
    val uiState: StateFlow<HomeScreenUIState> = _uiState.asStateFlow()   //Backing Property
    val categorySavedState = TextFieldState("")
    val topicSavedState = TextFieldState("")
    val listForSavedState = TextFieldState("")

    fun createNewList(category: CharSequence, title: CharSequence, listFor: CharSequence) {
        if (categorySavedState.text.isEmpty()) {
            _uiState.update {
                it.copy(
                    isError = true,
                    hasSQLError = false,
                    hasNoListFor = false
                )
            }
        } else if (listForSavedState.text.isEmpty()) {
            _uiState.update {
                it.copy(
                    isError = false,
                    hasSQLError = false,
                    hasNoListFor = true
                )
            }
        } else{
            viewModelScope.launch {
                try {
                    val categoryId = homeCategoriesRepositoryImp.addHomeCategories(
                        HomeCategories(
                            category = category.toString(),
                            topic = title.toString()
                        )
                    )
                    createInitialListForCategory(categoryId, listFor)
                    _uiState.update {
                        it.copy(
                            showCreateDialog = false,
                            showNoListMessage = false
                        )
                    }
                    clearTextFieldStates()
                } catch ( _: SQLiteException) {
                    _uiState.update {
                        it.copy(
                            hasSQLError = true,
                            isError = false,
                            hasNoListFor = false
                        )
                    }
                }
            }
        }
    }

    fun createInitialListForCategory(categoryId: Long, listForTitle: CharSequence) {
        viewModelScope.launch {
            homeCategoriesRepositoryImp.addInitialListForCategory(
                SavedLists(
                    homeCategoriesForeignId = categoryId,
                    owner = listForTitle.toString()
                )
            )
        }
    }
    fun getLists() {
        viewModelScope.launch {
            val homeListsFlow = homeCategoriesRepositoryImp.getAllHomeCategoriesStream()
            val homeLists = homeListsFlow.first()
            val hasLists = homeLists.isNotEmpty()
            _uiState.update {
                if (hasLists) {
                    it.copy(
                        existingHomeLists = homeLists,
                        showNoListMessage = false)

                } else {
                    it.copy(showNoListMessage = true)
                }
            }
        }
    }


    fun deleteHomeList() {
        viewModelScope.launch {
            val homeListId = _uiState.value.currentlySelectedHomeList.first
            homeCategoriesRepositoryImp.deleteHomeCategory(homeListId)
        }
    }
    fun setCurrentlySelectedHomeList(id: Int, title: String) {
        _uiState.update {
            it.copy(currentlySelectedHomeList = Pair(id, title))
        }
    }

    fun setCreateDialogVisibility(isVisible: Boolean) {
        _uiState.update {
            it.copy(showCreateDialog = isVisible)
        }
    }

    fun setDeletionDialogVisibility(isVisible: Boolean) {
        _uiState.update {
            it.copy(showDeletionDialog = isVisible)
        }
    }

    fun clearErrors() {
        _uiState.update {
            it.copy(
                isError = false,
                hasSQLError = false,
                hasNoListFor = false
            )
        }
    }

    fun clearTextFieldStates() {
        categorySavedState.clearText()
        topicSavedState.clearText()
        listForSavedState.clearText()
    }
}

