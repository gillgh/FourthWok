package com.example.fourthwok.db

import androidx.room.*

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY date DESC")
    suspend fun getAll(): List<Note>

    @Insert
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}