package com.example.wickedlista.data

data class AddItemUIState(

    val itemLabel: String = "", //CURT - necessary given that i have a TextFieldState?
    val itemDescription: String = "",//CURT - necessary given that i have a TextFieldState?
    val itemInitialStatus: String = "",//CURT - necessary given that i have a TextFieldState?
    val itemAdditionalStatus: String = "",//CURT - necessary given that i have a TextFieldState?
    val itemAdditionalStatus2: String = "",//CURT - necessary given that i have a TextFieldState?
    val itemAdditionalStatus3: String = "",//CURT - necessary given that i have a TextFieldState?


    val itemStatuses: List<String> = listOf(),
    val showSuccessAddAMoreItemDialog: Boolean = false,
    val showAdditionalItemsDialog: Boolean = false,
    val hasBlankLabelError: Boolean = false,
    val hasSQLError: Boolean = false
)
