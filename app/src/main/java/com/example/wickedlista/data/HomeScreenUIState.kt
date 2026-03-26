package com.example.wickedlista.data

import com.example.wickedlista.database.homecategories.HomeCategories

data class HomeScreenUIState(
    val existingHomeLists: List<HomeCategories> = listOf(),
    val currentlySelectedHomeList: Triple<Int, String, String> = Triple(-1, "", ""),
    val isError: Boolean = false,
    val hasSQLError: Boolean = false,
    val hasNoListFor: Boolean = false,
    val showCreateDialog: Boolean = false,
    val showDeletionDialog: Boolean = false,
    val showNoListMessage: Boolean = false
)
