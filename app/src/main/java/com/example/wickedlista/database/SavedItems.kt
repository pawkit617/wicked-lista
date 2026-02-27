package com.example.wickedlista.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "saved_items",
    foreignKeys = [
        ForeignKey(
            entity = SavedLists::class,
            parentColumns = ["savedListId"],
            childColumns = ["savedListForeignId"]
        )

    ]
)
data class SavedItems(
    @PrimaryKey(autoGenerate = true)
    val savedItemId: Int = 0,
    val savedListForeignId: Int,
    val name: String
)
