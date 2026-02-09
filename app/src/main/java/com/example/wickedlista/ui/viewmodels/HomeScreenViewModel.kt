package com.example.wickedlista.ui.viewmodels


import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import com.example.wickedlista.data.HomeScreenUIState
import com.example.wickedlista.database.HomeListsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(SavedStateHandleSaveableApi::class)
class HomeScreenViewModel @Inject constructor(val homeListsRepositoryImp: HomeListsRepositoryImp): ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUIState()) //Backing field
    val uiState: StateFlow<HomeScreenUIState> = _uiState.asStateFlow()   //Backing Property
    val titleSavedState = TextFieldState("")
    val subjectSavedState = TextFieldState("")
    fun createNewList() {
        //validate input
        //access Room
        Log.d("CURT",  "${titleSavedState.text}  " + subjectSavedState.text)
        if (titleSavedState.text.isEmpty()) {
            _uiState.update { titleSavedState ->
                titleSavedState.copy(isError = true)
            }
        } else {
            viewModelScope.launch {
                val flowOfLists = homeListsRepositoryImp.homeListsDao.getAllHomeLists()
            }
        }
    }


}