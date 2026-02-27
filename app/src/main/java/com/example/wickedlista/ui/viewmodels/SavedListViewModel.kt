package com.example.wickedlista.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.wickedlista.data.SavedListUIState
import com.example.wickedlista.database.HomeListsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SavedListViewModel @Inject constructor(homeListsRepositoryImp: HomeListsRepositoryImp): ViewModel() {
    private val _uiState = MutableStateFlow(SavedListUIState())
    val uiState: StateFlow<SavedListUIState> = _uiState.asStateFlow()
}