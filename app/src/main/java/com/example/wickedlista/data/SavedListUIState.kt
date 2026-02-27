package com.example.wickedlista.data

import com.example.wickedlista.database.SavedItems
import com.example.wickedlista.database.SavedLists

data class SavedListUIState(
    val allSavedLists: List<SavedLists> = listOf(), //CURT - all the saved lists under the group
    val allSavedItemsForList: List<SavedItems> = listOf() //CURT - all the saved items for a particular list
)
