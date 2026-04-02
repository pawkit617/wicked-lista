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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.SQLException
import javax.inject.Inject
import kotlin.text.isNotEmpty

@HiltViewModel
class ModifyItemViewModel @Inject constructor(
    private val savedListRepositoryImp: SavedItemsRepositoryImp,
    private val itemsRepositoryImp: ItemStatusRepositoryImp
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

    //region Repo Operations
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
                            showSuccessAddMoreItemDialog = true
                        )
                    }
                    clearTextFieldStates()
                    clearErrors()
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
                itemsRepositoryImp.addItemStatus(itemStatus)
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

    fun updateSavedItem(
        savedItemId: Int,
        ownerId: Int
    ) {
        viewModelScope.launch {
            val updatedItem = SavedItems(
                savedItemId,
                ownerId,
                labelTextFieldState.text.toString(),
                descTextFieldState.text.toString(),
                statusTextFieldForMenuState.text.toString()
            )
            savedListRepositoryImp.updateSavedItem(updatedItem)
        }
    }

    fun fillFormForItemEdit(ownerId: Int, itemLabel:String, itemDesc: String, currentStatus: String) {
        viewModelScope.launch {
            val listOfStatusFlow = itemsRepositoryImp.getItemStatusForOwnerId(ownerId)
            val itemStatusList = listOfStatusFlow.first()
            if (itemStatusList.isNotEmpty()) {
                val itemStatuses = itemStatusList.first()
                labelTextFieldState.edit {
                    replace(0, labelTextFieldState.text.length, itemLabel)
                }
                descTextFieldState.edit {
                    replace(0, descTextFieldState.text.length, itemDesc)
                }
                statusTextFieldForMenuState.edit {
                    replace(0, statusTextFieldForMenuState.text.length, currentStatus)
                }
                _uiState.update { it ->
                    it.copy(
                        itemStatuses = itemStatuses.let { item ->
                            listOf(item.firstStatus, item.secondStatus, item.thirdStatus, item.fourthStatus)
                        }.filter { it.isNotEmpty() }
                    )
                }
            }
        }
    }

    fun updateStatusesForItem(ownerId: Int) { //CURT -  REVISIT This for cleaner logic for add vs. edit
        viewModelScope.launch {
            val listOfStatusFlow = itemsRepositoryImp.getItemStatusForOwnerId(ownerId)
            val itemStatusList = listOfStatusFlow.first()
            if (itemStatusList.isNotEmpty()) {
                val itemStatuses = itemStatusList.first()
                _uiState.update { it ->
                    it.copy(
                        itemStatuses = itemStatuses.let { item ->
                            listOf(item.firstStatus, item.secondStatus, item.thirdStatus, item.fourthStatus)
                        }.filter { it.isNotEmpty() }
                    )
                }
            }
        }
    }

    fun deleteSavedItem(savedItemId: Int) {
        viewModelScope.launch {
            savedListRepositoryImp.deleteSavedItem(savedItemId)
        }
    }

    //endregion
    fun setShowSuccessAddMoreItemDialog(shouldShow: Boolean) {
        _uiState.update {
            it.copy(
                showSuccessAddMoreItemDialog = shouldShow
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