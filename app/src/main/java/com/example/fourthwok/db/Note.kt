package com.example.fourthwok.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var title: String,
    var content: String,
    val date: Date
) : java.io.Serializable
