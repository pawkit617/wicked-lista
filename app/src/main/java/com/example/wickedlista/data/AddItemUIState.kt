package com.example.wickedlista.data

import com.example.wickedlista.database.saveditems.StatusType

data class AddItemUIState(
    val itemStatuses: List<String> = listOf(),
    val itemStatusCheckboxLabel: String = "",
    val itemStatusCheckboxChecked: Boolean = false,
    val useMenuForStatus: Boolean = false,
    val showSuccessAddMoreItemDialog: Boolean = false,
    val hasBlankLabelError: Boolean = false,
    val hasBlankStatusError: Boolean = false,
    val useCheckboxStatus: Boolean = false,
    val statusType: StatusType = StatusType.UnassignedStatusType
)
