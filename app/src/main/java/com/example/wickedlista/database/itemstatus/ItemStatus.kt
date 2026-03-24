package com.example.wickedlista.database.itemstatus

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.savedlists.SavedLists

@Entity(
    tableName = "item_statuses",
    foreignKeys = [
        ForeignKey(
            entity = SavedLists::class,
            parentColumns = ["saved_list_id"],
            childColumns = ["saved_list_foreign_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["saved_list_foreign_id"], unique = true)]
)
data class ItemStatus(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("status_id")
    val statusId: Int = 0,
    @ColumnInfo("saved_list_foreign_id")
    val savedListForeignId: Int,
    val firstStatus: String,
    val secondStatus: String,
    val thirdStatus: String,
    val fourthStatus: String
)