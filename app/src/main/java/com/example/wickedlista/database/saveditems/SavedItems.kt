package com.example.wickedlista.database.saveditems

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.wickedlista.database.savedlists.SavedLists

@Entity(
    tableName = "saved_items",
    foreignKeys = [
        ForeignKey(
            entity = SavedLists::class,
            parentColumns = ["saved_list_id"],
            childColumns = ["saved_list_foreign_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["saved_list_foreign_id"])]
)
data class SavedItems(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("saved_item_id")
    val savedItemId: Int = 0,
    @ColumnInfo("saved_list_foreign_id")
    val savedListForeignId: Int,
    val label: String,
    val description: String,
    val status: String,
    val statusType: StatusType = StatusType.UnassignedStatusType
)

enum class StatusType(val type: String) {
    MenuStatusType(type = "Menu"),
    CheckboxStatusType(type = "Checkbox"),
    UnassignedStatusType(type = "Unassigned")
}