package com.example.wickedlista.database.homecategories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "home_categories", indices = [Index(value = ["category"], unique = true)])
data class HomeCategories(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, //CURT - has to be 0 to auto generate
    @ColumnInfo(name = "category")
    val category: String,
    @ColumnInfo(name = "topic")
    val topic: String
)
