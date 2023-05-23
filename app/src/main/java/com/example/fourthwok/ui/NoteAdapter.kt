package com.example.fourthwok.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fourthwok.OnDeleteClickListener
import com.example.fourthwok.OnItemClickListener
import com.example.fourthwok.R
import com.example.fourthwok.db.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(var notes: List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateList(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }


    fun removeItem(position: Int) {
        notes = notes.toMutableList().apply { removeAt(position) }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun deleteItem(position: Int) {
        notes.toMutableList().apply {
            removeAt(position)
            notes = this
            notifyItemRemoved(position)
        }
    }

    fun updateItem(position: Int, note: Note) {
        notes.toMutableList().apply {
            set(position, note)
            notes = this

        }
        notifyItemChanged(position)
    }




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.note_title)
        private val contentTextView: TextView = itemView.findViewById(R.id.note_content)
        private val dateTextView: TextView = itemView.findViewById(R.id.note_date)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = notes[position]
                    itemClickListener?.onItemClick(note)
                }
            }
        }

        fun bind(note: Note) {
            titleTextView.text = note.title
            contentTextView.text = note.content
            dateTextView.text =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(note.date)
        }
    }
}

