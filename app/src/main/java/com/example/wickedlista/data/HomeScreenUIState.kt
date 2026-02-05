package com.example.wickedlista.data

data class HomeScreenUIState(
    val existingLists: List<String> = listOf(),
    val titleOfNewList:String = "",
    val subjectOfNewList: String = "",
    val isError: Boolean = false
)
