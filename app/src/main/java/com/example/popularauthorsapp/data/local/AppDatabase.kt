package com.example.popularauthorsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.popularauthorsapp.data.model.Author

@Database(entities = [Author::class], version = 7, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authorDao(): AuthorDao

    companion object {
        private const val DATABASE_NAME = "popular_authors.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}