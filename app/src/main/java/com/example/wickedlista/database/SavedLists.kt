package com.example.wickedlista.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_lists",
    foreignKeys = [
        ForeignKey(
            entity = HomeLists::class,
            parentColumns = ["id"],
            childColumns = ["list_group_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class SavedLists(
    @PrimaryKey(autoGenerate = true)
    val savedListId: Int = 0,
    @ColumnInfo(name = "list_group_id")
    val homeListForeignId: Int,
    @ColumnInfo(name = "owner")
    val owner: String
)
