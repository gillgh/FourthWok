package com.example.fourthwok

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fourthwok.db.AppDatabase
import com.example.fourthwok.db.Note
import com.example.fourthwok.ui.NoteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CreateNote : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonCancel: Button
    private lateinit var adapter: NoteAdapter
    private companion object {
        const val REQUEST_CODE_CREATE_NOTE = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_note)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextContent = findViewById(R.id.edit_text_content)
        buttonSave = findViewById(R.id.button_save)
        buttonCancel = findViewById(R.id.button_cancel)
        adapter = NoteAdapter(emptyList())

        buttonSave.setOnClickListener {
            saveNote()
        }
        buttonCancel.setOnClickListener {
            finish()
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString()
        val content = editTextContent.text.toString()

        val noteDao = AppDatabase.getInstance(applicationContext).noteDao()
        val note = Note(title = title, content = content, date = Date())

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    noteDao.insert(note)
                }
                val allNotes = withContext(Dispatchers.IO) {
                    noteDao.getAll()
                }
                adapter.updateList(allNotes)
                Toast.makeText(this@CreateNote, "Заметка сохранена", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent().apply {
                    // Передаем результат обратно в MainActivity
                    setResult(Activity.RESULT_OK, this)
                }
                resultIntent.putExtra("isNoteSaved", true)
                finish()
            } catch (e: Exception) {
                // Обработка ошибки при сохранении заметки
                // TODO: Добавьте необходимую обработку ошибки
            }
        }
    }
}




