package com.example.fourthwok

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fourthwok.db.AppDatabase
import com.example.fourthwok.db.Note
import com.example.fourthwok.ui.NoteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteActivity : AppCompatActivity() {

    private lateinit var note: Note
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note)

        adapter = NoteAdapter(emptyList())

        // Получите переданную заметку из Intent
        note = intent.getSerializableExtra("note") as Note

        // Настройте поля ввода заметки
        titleEditText = findViewById(R.id.edit_text_title)
        contentEditText = findViewById(R.id.edit_text_content)
        titleEditText.setText(note.title)
        contentEditText.setText(note.content)

        // Настройте кнопку "Сохранить заметку" для сохранения отредактированной заметки
        val saveButton: Button = findViewById(R.id.button_save)
        saveButton.setOnClickListener {
            saveNote()
        }

        // Настройте кнопку "Удалить" для удаления заметки
        val deleteButton: Button = findViewById(R.id.button_delete)
        deleteButton.setOnClickListener {
            deleteNote()
        }

        // Настройте кнопку "Отмена" для отмены редактирования
        val cancelButton: Button = findViewById(R.id.button_cancel)
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun saveNote() {
        // Проверка инициализации adapter
        if (!::adapter.isInitialized) {
            // Обработка ошибки инициализации
            return
        }
        // Обновите поля заметки с данными из полей ввода
        val updatedTitle = titleEditText.text.toString()
        val updatedContent = contentEditText.text.toString()

        note.title = updatedTitle
        note.content = updatedContent

        val db = AppDatabase.getInstance(this)
        val noteDao = db.noteDao()

        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    noteDao.update(note)
                }
                val allNotes = withContext(Dispatchers.IO) {
                    noteDao.getAll()
                }
                adapter.updateList(allNotes)
                adapter.notifyDataSetChanged()
                setResult(RESULT_OK)
                finish()
            } catch (e: Exception) {
                // Обработка ошибки при сохранении заметки
                // TODO: Добавьте необходимую обработку ошибки
            }
        }
    }

    private fun deleteNote() {
        // Получите экземпляр базы данных
        val db = AppDatabase.getInstance(this)

        // Получите объект DAO из базы данных
        val noteDao = db.noteDao()

        // Выполните удаление заметки из базы данных
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                noteDao.delete(note)
            }
            val updatedNotes = withContext(Dispatchers.IO) {
                noteDao.getAll()
            }
            adapter.updateList(updatedNotes)
            adapter.notifyDataSetChanged()
            setResult(RESULT_OK)
            finish()
        }
    }
}
