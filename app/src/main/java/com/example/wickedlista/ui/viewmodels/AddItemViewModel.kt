package com.example.wickedlista.ui.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wickedlista.data.AddItemUIState
import com.example.wickedlista.database.itemstatus.ItemStatus
import com.example.wickedlista.database.itemstatus.ItemStatusRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.SQLException
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    val savedListRepositoryImp: SavedItemsRepositoryImp,
    val itemsRepositoryImp: ItemStatusRepositoryImp
): ViewModel() {
    private val _uiState = MutableStateFlow(AddItemUIState())
    val uiState: StateFlow<AddItemUIState> = _uiState.asStateFlow()

    val labelTextFieldState = TextFieldState()
    val descTextFieldState = TextFieldState()
    val initialStatusTextFieldState = TextFieldState()
    val additionalStatusTextFieldState = TextFieldState()
    val additionalStatus2TextFieldState = TextFieldState()
    val additionalStatus3TextFieldState = TextFieldState()
    val statusTextFieldForMenuState = TextFieldState()

    fun addItemToList(savedListId: Int) {
        viewModelScope.launch {
            try {
                if (labelTextFieldState.text.isNotEmpty()) {

                    val givenStatus = statusTextFieldForMenuState.text.ifEmpty {
                        initialStatusTextFieldState.text.toString()
                    }

                    val savedItems = SavedItems(
                        label = labelTextFieldState.text.toString(),
                        savedListForeignId = savedListId,
                        description = descTextFieldState.text.toString(),
                        status = givenStatus.toString()
                    )
                    savedListRepositoryImp.addItemToList(savedItems)
                    addItemStatusWithListId(savedListId)

                    _uiState.update {
                        it.copy(
                            showSuccessAddAMoreItemDialog = true
                        )
                    }
                    clearTextFieldStates()
                } else {
                    _uiState.update {
                        it.copy(hasBlankLabelError = true)
                    }
                }
            } catch (_: SQLException) {
                _uiState.update {
                    it.copy(hasSQLError = true)
                }
            }
        }
    }

    private fun addItemStatusWithListId(savedListId: Int) {
        _uiState.value.itemStatuses.ifEmpty {
            val initialStatus = initialStatusTextFieldState.text.toString()
            val additionalStatus = additionalStatusTextFieldState.text.toString()
            val additionalStatus2 = additionalStatus2TextFieldState.text.toString()
            val additionalStatus3 = additionalStatus3TextFieldState.text.toString()

            viewModelScope.launch {
                val itemStatus = ItemStatus(
                    savedListForeignId = savedListId,
                    firstStatus = initialStatus,
                    secondStatus = additionalStatus,
                    thirdStatus = additionalStatus2,
                    fourthStatus = additionalStatus3
                )
                itemsRepositoryImp.itemStatusDao.addItemStatus(itemStatus)
            }
            val statuses = listOf(
                initialStatus,
                additionalStatus,
                additionalStatus2,
                additionalStatus3
            )
            _uiState.update {
                it.copy(
                    itemStatuses =  statuses.filter { it.isNotEmpty() },
                )
            }
        }
    }

    fun setShowSuccessAddAMoreItemDialog(shouldShow: Boolean) {
        _uiState.update {
            it.copy(
                showSuccessAddAMoreItemDialog = shouldShow
            )
        }
    }

    fun setAdditionalItemsDialog(shouldShow: Boolean) {
        _uiState.update {
            it.copy(
                showAdditionalItemsDialog = shouldShow
            )
        }
    }

    fun clearTextFieldStates() {
        labelTextFieldState.clearText()
        descTextFieldState.clearText()
        initialStatusTextFieldState.clearText()
        additionalStatusTextFieldState.clearText()
        additionalStatus2TextFieldState.clearText()
        additionalStatus3TextFieldState.clearText()
        statusTextFieldForMenuState.clearText()
    }

    fun clearErrors() {
        _uiState.update {
            it.copy(
                hasSQLError = false,
                hasBlankLabelError = false
            )
        }
    }
}