package com.example.popularauthorsapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.popularauthorsapp.data.model.Author

@Database(entities = [Author::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao

    companion object {
        const val DATABASE_NAME = "popular_authors.db"
    }

}