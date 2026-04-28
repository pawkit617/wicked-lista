package com.example.wickedlista.database.itemstatuschecked

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.wickedlista.database.savedlists.SavedLists

@Entity(
    tableName = "item_statuses_checked",
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

data class ItemStatusChecked(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("status_checked_id")
    val statusCheckedId: Int = 0,
    val statusLabel: String,
    @ColumnInfo("saved_list_foreign_id")
    val savedListForeignId: Int,
    val isChecked: Boolean
)

