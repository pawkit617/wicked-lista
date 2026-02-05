package com.example.wickedlista.ui.viewmodels


import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import com.example.wickedlista.data.HomeScreenUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@OptIn(SavedStateHandleSaveableApi::class)
class HomeScreenViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {
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
        }
    }


}