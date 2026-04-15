package com.example.wickedlista.data

data class AddItemUIState(
    val itemStatuses: List<String> = listOf(),
    val useMenuForStatus: Boolean = false,
    val showSuccessAddMoreItemDialog: Boolean = false,
    val hasBlankLabelError: Boolean = false,
    val hasBlankStatusError: Boolean = false,
)
