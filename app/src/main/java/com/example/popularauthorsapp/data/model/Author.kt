package com.example.popularauthorsapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Unified representation of an Author, serving as a combination of DTO, model, and entity classes.
 * This data class is used for simplification purposes to avoid redundant data classes and mapping.
 *
 * @param authorId The unique identifier of the author.
 * @param name The name of the author.
 * @param image The URL of the author's image.
 * @param url The URL of the author's profile.
 * @param popularBookTitle The title of the author's popular book.
 * @param popularBookUrl The URL of the author's popular book.
 * @param numberPublishedBooks The number of books published by the author.
 */
@Entity(tableName = "authors")
data class Author(
    @PrimaryKey
    @SerializedName("author_id")
    val authorId: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("url")
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