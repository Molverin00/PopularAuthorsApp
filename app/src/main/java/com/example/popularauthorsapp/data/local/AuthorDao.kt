package com.example.popularauthorsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.popularauthorsapp.data.model.Author
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {
    @Query("SELECT * FROM authors ORDER BY name ASC")
    fun getAllAuthors(): Flow<List<Author>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAuthors(authors: List<Author>)

    @Query("DELETE FROM authors")
    suspend fun deleteAllAuthors()
}