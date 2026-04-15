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
import javax.inject.Inject

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

    private fun isFormValid(useMenuForStatus: Boolean = false): Boolean {
        val isStatusFieldEmpty = if(useMenuForStatus) {
            statusTextFieldForMenuState.text.isEmpty()
        } else {
            initialStatusTextFieldState.text.isEmpty()
        }

        val isLabelFieldEmpty = labelTextFieldState.text.isEmpty()

        _uiState.update {
            it.copy(
                hasBlankLabelError = isLabelFieldEmpty,
                hasBlankStatusError = isStatusFieldEmpty
            )
        }

        return !isLabelFieldEmpty && !isStatusFieldEmpty
    }


    //region Repo Operations
    fun addItemToListWithId(savedListId: Int, isAddingMore: Boolean = false) {
        if (isFormValid(isAddingMore)) {
            addItemStatusWithListId(savedListId)
            addItemInfoWithListId(savedListId)
        }
    }

    private fun addItemInfoWithListId(savedListId: Int) {
        viewModelScope.launch {
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
            //Log.d("RID", rid.toString()) CURT - Suppressed: java.lang.RuntimeException: Method d in android.util.Log not mocked - Watch out for testing

            _uiState.update {
                it.copy(
                    showSuccessAddMoreItemDialog = true
                )
            }
            clearTextFieldStates()
            clearErrors()
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
                val statusesFiltered = statuses.filter { it.isNotEmpty() }
                it.copy(
                    itemStatuses =  statusesFiltered,
                    hasBlankStatusError = statusesFiltered.isEmpty()
                )
            }
        }
    }

    fun updateSavedItem(
        savedItemId: Int,
        ownerId: Int
    ) : Boolean {
        val isValid = labelTextFieldState.text.isNotEmpty()
        if (isValid) {
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
        } else {
            _uiState.update {
                it.copy(
                    hasBlankLabelError = labelTextFieldState.text.isEmpty()
                )
            }
        }
        return isValid
    }

    fun prepopulateFormWithItemInfo(ownerId: Int, itemLabel:String = "", itemDesc: String = "", currentStatus: String) {
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

    fun deleteSavedItem(savedItemId: Int) : Int {
        var rowsDeleted = 0
        viewModelScope.launch {
            rowsDeleted = savedListRepositoryImp.deleteSavedItem(savedItemId)
        }

        return rowsDeleted
    }

    //endregion
    fun setShowSuccessAddMoreItemDialog(shouldShow: Boolean) {
        _uiState.update {
            it.copy(
                showSuccessAddMoreItemDialog = shouldShow
            )
        }
    }

    fun setUseMenuForStatus(useMenu: Boolean) {
        _uiState.update {
            it.copy(
                useMenuForStatus = useMenu
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
                hasBlankLabelError = false,
                hasBlankStatusError = false
            )
        }
    }
}