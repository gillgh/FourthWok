package com.example.fourthwok

import com.example.fourthwok.db.Note


interface OnItemClickListener {
    fun onItemClick(note: Note)
}

interface OnDeleteClickListener {
    fun onDeleteClick(note: Note)
}
