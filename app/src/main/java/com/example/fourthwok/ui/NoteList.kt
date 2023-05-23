package com.example.fourthwok.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

class NoteList : Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    internal lateinit var adapter: NoteAdapter
    internal lateinit var noteViewModel: NoteViewModel

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
        // Подключение NoteViewModel
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

        // Наблюдение за изменениями списка заметок и обновление UI
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.updateList(notes)
        }
        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем экземпляр базы данных
        val db = AppDatabase.getInstance(requireContext())

        // Получаем объект DAO из базы данных
        val noteDao = db.noteDao()

        // Наблюдение за изменениями списка заметок и обновление UI
        noteViewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.updateList(notes)
        }


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

    private fun openNoteForEditing(note: Note) {
        val intent = Intent(requireContext(), EditNoteActivity::class.java)
        // Передайте выбранную заметку через Intent, чтобы ее можно было отредактировать
        intent.putExtra("note", note)

        startActivity(intent)
    }
}
