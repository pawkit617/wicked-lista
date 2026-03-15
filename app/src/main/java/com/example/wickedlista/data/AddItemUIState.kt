package com.example.wickedlista.data

data class AddItemUIState(
    val itemLabel: String = "",
    val itemDescription: String = "",
    val itemInitialStatus: String = "",
    val itemAdditionalStatus: String = "",
    val itemAdditionalStatus2: String = "",
    val itemAdditionalStatus3: String = "",
    val itemStatuses: List<CharSequence> = listOf(),
    val showSuccessAddAMoreItemDialog: Boolean = false,
    val showAdditionalItemsDialog: Boolean = false,
    val hasBlankLabelError: Boolean = false,
    val hasSQLError: Boolean = false
)
