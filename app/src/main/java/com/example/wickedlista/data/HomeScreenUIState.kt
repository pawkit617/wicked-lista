package com.example.wickedlista.data

data class HomeScreenUIState(
    val existingLists: List<String> = listOf(),
    val currentlySelectedHomeList: Pair<Int, String> = Pair(-1, ""),
    val titleOfNewList:String = "",
    val subjectOfNewList: String = "",
    val errorMessage: String = "",
    val isError: Boolean = false,
    val hasSQLError: Boolean = false,
    val showCreateDialog: Boolean = false,
    val showDeletionDialog: Boolean = false,
    val showNoListMessage: Boolean = false
)
