package com.example.fourthwok.db

import android.content.Context
import androidx.room.*

@Database(entities = [Note::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "notes_database"
            ).build()
    }
}
