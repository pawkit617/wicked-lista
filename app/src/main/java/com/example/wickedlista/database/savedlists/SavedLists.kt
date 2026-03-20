package com.example.wickedlista.database.savedlists

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.wickedlista.database.homecategories.HomeCategories

@Entity(
    tableName = "saved_lists",
    foreignKeys = [
        ForeignKey(
            entity = HomeCategories::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ],
    indices = [Index(value = ["owner"], unique = true), Index(value = ["category_id"])]
)
data class SavedLists(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "saved_list_id")
    val savedListId: Int = 0,
    @ColumnInfo(name = "category_id")
    val homeCategoriesForeignId: Long,
    @ColumnInfo(name = "owner")
    val owner: String
)