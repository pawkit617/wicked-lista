package com.example.wickedlista.database.itemstatuschecked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.wickedlista.database.saveditems.SavedItems
import com.example.wickedlista.database.savedlists.SavedLists

@Entity(
    tableName = "item_statuses_checked",
    foreignKeys = [
        ForeignKey(
            entity = SavedLists::class,
            parentColumns = ["saved_list_id"],
            childColumns = ["saved_list_foreign_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SavedItems::class,
            parentColumns = ["saved_item_id"],
            childColumns = ["saved_item_foreign_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["saved_item_foreign_id"], unique = true), Index(value = ["saved_list_foreign_id"], unique = false)]
)

data class ItemStatusChecked(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("status_checked_id")
    val statusCheckedId: Int = 0,
    val statusLabel: String,
    @ColumnInfo("saved_item_foreign_id")
    val savedItemForeignId: Int,
    @ColumnInfo("saved_list_foreign_id")
    val savedListForeignId: Int,
    var isChecked: Boolean
)

