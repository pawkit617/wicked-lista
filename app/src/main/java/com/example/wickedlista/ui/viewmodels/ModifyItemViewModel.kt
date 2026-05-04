package com.example.wickedlista.ui.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wickedlista.data.AddItemUIState
import com.example.wickedlista.database.itemstatus.ItemStatus
import com.example.wickedlista.database.itemstatus.ItemStatusRepositoryImp
import com.example.wickedlista.database.itemstatuschecked.ItemStatusChecked
import com.example.wickedlista.database.itemstatuschecked.ItemStatusCheckedRepositoryImp
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.saveditems.SavedItemsRepositoryImp
import com.example.wickedlista.database.saveditems.StatusType
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
    private val itemsRepositoryImp: ItemStatusRepositoryImp,
    private val itemStatusCheckedRepositoryImp: ItemStatusCheckedRepositoryImp
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

    val checkboxTextFieldState = TextFieldState()

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

    private fun isFormValidForCheckboxStatus(): Boolean {
        val isCheckboxLabelAssigned = _uiState.value.itemStatusCheckboxLabel.isNotEmpty()

        val isCheckboxFieldEmpty =  if (!isCheckboxLabelAssigned) checkboxTextFieldState.text.isEmpty() else false

        val isLabelFieldEmpty = labelTextFieldState.text.isEmpty()


        _uiState.update {
            it.copy(
                hasBlankLabelError = isLabelFieldEmpty,
                hasBlankStatusError = isCheckboxFieldEmpty
            )
        }

        return !isLabelFieldEmpty && !isCheckboxFieldEmpty
    }

    //region Repo Operations
    fun addItemToListWithId(savedListId: Int, useMenuForStatus: Boolean = false) {
        if (isFormValid(useMenuForStatus)) {
            addItemStatusWithListId(savedListId)
            addItemInfoWithListId(savedListId)
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

    fun checkItemsStatusesForOwnerId(ownerId: Int) {
        viewModelScope.launch {
            val listOfStatus = itemsRepositoryImp.getItemStatusForOwnerId(ownerId).first()
            val listOfCheckedStatus = itemStatusCheckedRepositoryImp.getStatusesForSavedItem(ownerId).first()

            if (listOfStatus.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        statusType = StatusType.MenuStatusType,
                        useMenuForStatus = true,
                        itemStatuses = listOfStatus.first().let { item ->
                            listOf(item.firstStatus, item.secondStatus, item.thirdStatus, item.fourthStatus)
                        }.filter { it.isNotEmpty() }
                    )
                }
            } else if (listOfCheckedStatus.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        statusType = StatusType.CheckboxStatusType,
                        itemStatusCheckboxLabel = listOfCheckedStatus.first().statusLabel, //why null?
                        itemStatusCheckboxChecked = listOfCheckedStatus.first().isChecked,
                        useCheckboxStatus = true
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        statusType = StatusType.UnassignedStatusType
                    )
                }
            }
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
                status = givenStatus.toString(),
                statusType = StatusType.MenuStatusType
            )
            savedListRepositoryImp.addItemToList(savedItems)
            //Log.d("RID", rid.toString()) CURT - Suppressed: java.lang.RuntimeException: Method d in android.util.Log not mocked - Watch out for testing

            _uiState.update {
                it.copy(
                    showSuccessAddMoreItemDialog = true,
                    useMenuForStatus = true,
                    statusType = StatusType.MenuStatusType
                )
            }
            clearInfoTextFieldStates()
            clearTextFieldStatesForStatusMenu()
            clearErrors()
        }
    }

    fun addItemToListWithIdForCheckedLabel(savedListId: Int) {
        if (isFormValidForCheckboxStatus()) {
            addItemStatusWithListIdForCheckbox(savedListId)
            addItemInfoWithListIdForCheckbox(savedListId)
        }
    }

    private fun addItemInfoWithListIdForCheckbox(savedListId: Int) {
        var checkboxLabel = checkboxTextFieldState.text.toString()
        checkboxLabel = checkboxLabel.ifEmpty { _uiState.value.itemStatusCheckboxLabel }

        val savedItems = SavedItems(
            label = labelTextFieldState.text.toString(),
            savedListForeignId = savedListId,
            description = descTextFieldState.text.toString(),
            status = checkboxLabel,
            statusType = StatusType.CheckboxStatusType
        )
        viewModelScope.launch {
            savedListRepositoryImp.addItemToList(savedItems)
        }

        _uiState.update {
            it.copy(
                showSuccessAddMoreItemDialog = true,
                useCheckboxStatus = true,
                statusType = StatusType.CheckboxStatusType,
            )
        }
    }

    private fun addItemStatusWithListIdForCheckbox(savedListId: Int) {
        var checkboxLabel = checkboxTextFieldState.text.toString()
        checkboxLabel = checkboxLabel.ifEmpty { _uiState.value.itemStatusCheckboxLabel }

        viewModelScope.launch {
            val itemStatusChecked = ItemStatusChecked(
                savedListForeignId = savedListId,
                statusLabel = checkboxLabel,
                isChecked = false
            )
            itemStatusCheckedRepositoryImp.addItemStatusChecked(itemStatusChecked)

            _uiState.update {
                it.copy(
                    itemStatusCheckboxLabel = checkboxLabel, //??? works for add more at first, but not for add when coming back in
                    useCheckboxStatus = true
                )
            }

            clearInfoTextFieldStates()
            clearTextFieldStatesForStatusMenu()
            clearErrors()
        }
    }

    fun updateSavedItem(
        savedItemId: Int,
        ownerId: Int
    ) : Boolean {
        val isValid = labelTextFieldState.text.isNotEmpty()
        if (isValid) {
            viewModelScope.launch {
                val statusText = if (
                    _uiState.value.statusType.type == StatusType.MenuStatusType.type
                ) {
                    statusTextFieldForMenuState.text.toString()
                } else {
                    _uiState.value.itemStatusCheckboxLabel
                }

                val updatedItem = SavedItems(
                    savedItemId,
                    ownerId,
                    labelTextFieldState.text.toString(),
                    descTextFieldState.text.toString(),
                    statusText
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

    fun prepopulateFormWithItemInfo(ownerId: Int, itemLabel:String = "", itemDesc: String = "", currentStatus: String = "") {
        labelTextFieldState.edit {
            replace(0, labelTextFieldState.text.length, itemLabel)
        }
        descTextFieldState.edit {
            replace(0, descTextFieldState.text.length, itemDesc)
        }

        prepopulateFormWithMenuStatuses(ownerId, currentStatus)
        prepopulateFormWithCheckboxStatuses(ownerId, currentStatus)
    }

    private fun prepopulateFormWithMenuStatuses(ownerId: Int, currentStatus: String) {
        viewModelScope.launch {
            val listOfStatusFlow = itemsRepositoryImp.getItemStatusForOwnerId(ownerId)
            val itemStatusList = listOfStatusFlow.first()
            if (itemStatusList.isNotEmpty()) {
                val itemStatuses = itemStatusList.first()
                statusTextFieldForMenuState.edit {
                    replace(0, statusTextFieldForMenuState.text.length, currentStatus)
                }
                _uiState.update { it ->
                    it.copy(
                        itemStatuses = itemStatuses.let { item ->
                            listOf(item.firstStatus, item.secondStatus, item.thirdStatus, item.fourthStatus)
                        }.filter { it.isNotEmpty() },
                        useMenuForStatus = true,
                        useCheckboxStatus = false,
                        statusType = StatusType.MenuStatusType
                    )
                }
            }
        }
    }

    private fun prepopulateFormWithCheckboxStatuses(ownerId: Int, currentStatus: String) {
        viewModelScope.launch {
            val checkboxStatusFlow = itemStatusCheckedRepositoryImp.getStatusesForSavedItem(ownerId)
            val checkboxStatusList = checkboxStatusFlow.first()
            if (checkboxStatusList.isNotEmpty()) {
                val checkboxStatus = checkboxStatusList.first()
                _uiState.update {
                    it.copy(
                        itemStatusCheckboxLabel = checkboxStatus.statusLabel,
                        itemStatusCheckboxChecked = checkboxStatus.isChecked,
                        useCheckboxStatus = true,
                        useMenuForStatus = false,
                        statusType = StatusType.CheckboxStatusType
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

    fun setItemStatusCheckboxLabel(label: String) {
        _uiState.update {
            it.copy(
                itemStatusCheckboxLabel = label
            )
        }
    }

    fun setItemStatusCheckboxChecked(isChecked: Boolean) {
        _uiState.update {
            it.copy(
                itemStatusCheckboxChecked = isChecked
            )
        }
    }

    fun clearInfoTextFieldStates() {
        labelTextFieldState.clearText()
        descTextFieldState.clearText()
    }

    fun clearTextFieldStatesForStatusMenu() {
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

    fun setUseCheckboxStatus(onOrOff: Boolean) {
        //val statusType = if (onOrOff) StatusType.CheckboxStatusType else StatusType.UnassignedStatusType
        _uiState.update {
            it.copy(
                useCheckboxStatus = onOrOff,
                //statusType = statusType
            )
        }
    }
}