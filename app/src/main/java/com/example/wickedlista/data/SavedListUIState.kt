package com.example.wickedlista.data

import android.app.Dialog
import com.example.wickedlista.database.SavedItems
import com.example.wickedlista.database.savedlists.SavedLists

data class SavedListUIState(
    val allSavedLists: List<SavedLists> = listOf(), //CURT - all the saved lists under the group
    val allSavedItemsForList: List<SavedItems> = listOf(), //CURT - all the saved items for a particular list
    val selectedOwnerId: Int = -1,
    val showAddOwnerDialog: Boolean = false,
    val hasSQLError: Boolean = false,
    val hasBlankError: Boolean = false
)
