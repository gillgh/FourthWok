package com.example.fourthwok

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.fourthwok.db.AppDatabase
import com.example.fourthwok.ui.MyPageAdapter
import com.example.fourthwok.ui.NoteList
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: MyPageAdapter

    private val createNoteLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Обновляем список заметок
                val noteDao = AppDatabase.getInstance(applicationContext).noteDao()
                lifecycleScope.launch {
                    val allNotes = withContext(Dispatchers.IO) {
                        noteDao.getAll()
                    }
                    // Обновляем список во всех фрагментах, использующих адаптер
                    pagerAdapter.fragments.forEach { fragment ->
                        if (fragment is NoteList) {
                            fragment.noteViewModel.updateNotes(allNotes)
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация ViewPager2 и PagerAdapter
        viewPager = findViewById(R.id.view_pager)
        pagerAdapter = MyPageAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = pagerAdapter

        // Инициализация TabLayout
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        // Использование TabLayoutMediator для связывания TabLayout и ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Заметки"
                1 -> "Теги"
                else -> "Неизвестный таб"
            }
        }.attach()

        viewPager.currentItem = 0

        // Находим кнопку "Создать" и устанавливаем обработчик клика на неё
        val createButton = findViewById<Button>(R.id.button_create_note)
        createButton.setOnClickListener {
            createNote()
        }
    }

    private fun createNote() {
        val intent = Intent(this, CreateNote::class.java)
        createNoteLauncher.launch(intent)
    }




}
