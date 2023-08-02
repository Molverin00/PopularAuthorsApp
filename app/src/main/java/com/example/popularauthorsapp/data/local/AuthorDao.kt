package com.example.popularauthorsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popularauthorsapp.data.model.Author

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAuthors(authors: List<Author>)

    @Query("SELECT * FROM authors")
    suspend fun getAllAuthors(): List<Author>

    @Query("DELETE FROM authors")
    suspend fun deleteAllAuthors()
}