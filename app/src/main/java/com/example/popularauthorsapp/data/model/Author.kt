package com.example.popularauthorsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey val authorId: Int,
    val name: String,
    val image: String,
    val url: String,
    val popularBookTitle: String,
    val popularBookUrl: String,
    val numberPublishedBooks: Int
)
