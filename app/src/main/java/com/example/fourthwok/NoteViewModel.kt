package com.example.fourthwok

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fourthwok.db.Note

class NoteViewModel: ViewModel(){
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    // Метод для обновления списка заметок
    fun updateNotes(newNotes: List<Note>) {
        _notes.value = newNotes
    }
}