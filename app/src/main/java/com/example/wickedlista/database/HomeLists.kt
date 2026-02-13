package com.example.wickedlista.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "home_lists", indices = [Index(value = ["title"], unique = true)])
data class HomeLists(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, //CURT - has to be 0 to auto generate
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "subject")
    val subject: String
)
