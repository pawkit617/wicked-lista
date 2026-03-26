package com.example.wickedlista.data

data class AddItemUIState(
    val itemStatuses: List<String> = listOf(),
    val showSuccessAddAMoreItemDialog: Boolean = false,
    val showAdditionalItemsDialog: Boolean = false,
    val hasBlankLabelError: Boolean = false,
    val hasSQLError: Boolean = false
)
