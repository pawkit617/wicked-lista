package com.example.wickedlista.data

data class AddItemUIState(
    val itemLabel: String = "",
    val itemDescription: String = "",
    val itemInitialStatus: String = "",
    val itemAdditionalStatus: String = "",
    val itemAdditionalStatus2: String = "",
    val itemAdditionalStatus3: String = "",
    val hasBlankLabelError: Boolean = false
)
