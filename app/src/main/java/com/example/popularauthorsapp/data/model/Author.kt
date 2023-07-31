package com.example.popularauthorsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "authors")
data class Author(
    @SerializedName("author_id")
    @PrimaryKey val authorId: Long,
    val name: String,
    val image: String,
    val url: String,
    @SerializedName("popular_book_title")
    val popularBookTitle: String,
    @SerializedName("popular_book_url")
    val popularBookUrl: String,
    @SerializedName("number_published_books")
    val numberPublishedBooks: Int
) {
    override fun toString(): String {
        return "Author(authorId=$authorId, name='$name', image='$image', url='$url', popularBookTitle='$popularBookTitle', popularBookUrl='$popularBookUrl', numberPublishedBooks=$numberPublishedBooks)"
    }
}
