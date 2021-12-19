package com.yashk9.noterr.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yashk9.noterr.databinding.NoteListItemBinding
import com.yashk9.noterr.model.Note

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteVH>(){

    inner class NoteVH(val binding: NoteListItemBinding): RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object: DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        val binding: NoteListItemBinding = NoteListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteVH(binding)
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val note = differ.currentList[position]

        with(holder.binding){
            noteTitle.text = note.title
            noteContent.text = note.content
            noteDate.text = note.toDateFormat()
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(note)
        }
    }

    override fun getItemCount() = differ.currentList.size

    private var onItemClickListener: ((Note) -> Unit)? = null
    fun setOnItemClickListener(listener: ((Note) -> Unit)?){
        onItemClickListener = listener
    }
}