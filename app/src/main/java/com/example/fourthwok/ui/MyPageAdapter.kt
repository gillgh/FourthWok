package com.example.fourthwok.ui
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class MyPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    val fragments = mutableListOf<Fragment>()

    override fun getItemCount(): Int {
        return fragments.size // возвращаем количество фрагментов в списке
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position] // возвращаем фрагмент с указанной позицией
    }

    init {
        fragments.add(NoteList())
        fragments.add(NoteTags())
    }
}

