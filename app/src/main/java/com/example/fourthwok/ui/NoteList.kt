package com.example.fourthwok.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fourthwok.*
import com.example.fourthwok.db.AppDatabase
import com.example.fourthwok.db.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class NoteList : Fragment(), OnDeleteClickListener, OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    internal lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)
        recyclerView = view.findViewById(R.id.note_list)
        adapter = NoteAdapter(emptyList())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем экземпляр базы данных
        val db = AppDatabase.getInstance(requireContext())

        // Получаем объект DAO из базы данных
        val noteDao = db.noteDao()

        // Получаем список всех заметок из базы данных
        lifecycleScope.launch {
            val allNotes = withContext(Dispatchers.IO) {
                noteDao.getAll()
            }
            adapter.updateList(allNotes)
        }

        // Установка слушателя
        adapter.setOnItemClickListener(this)


    }

    override fun onItemClick(note: Note) {
        // Реализуйте логику обработки нажатия на заметку
        openNoteForEditing(note)


    }

    override fun onDeleteClick(note: Note) {
        // Реализуйте логику обработки нажатия на заметку
        deleteNote(note)
    }
    private fun deleteNote(note: Note) {
        val db = AppDatabase.getInstance(requireContext())
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.noteDao().delete(note)
            }
            val updatedNotes = withContext(Dispatchers.IO) {
                db.noteDao().getAll()
            }
            adapter.updateList(updatedNotes)

            val position = adapter.notes.indexOf(note)
            if (position != -1) {
                adapter.deleteItem(position)
            }
        }

    }

    private fun openNoteForEditing(note: Note) {
        val intent = Intent(requireContext(), EditNoteActivity::class.java)
        // Передайте выбранную заметку через Intent, чтобы ее можно было отредактировать
        intent.putExtra("note", note)

        startActivity(intent)
    }
}
