package com.yashk9.noterr.repo

import com.yashk9.noterr.data.local.AppDatabase
import com.yashk9.noterr.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepo @Inject constructor(private val db: AppDatabase) {
    suspend fun addNote(note: Note){
        db.getNoteDao().addNote(note)
    }

    suspend fun updateNote(note: Note){
        db.getNoteDao().updateNote(note)
    }

    fun getAllNotes(): Flow<List<Note>> {
        return db.getNoteDao().getAllNotes()
    }

    fun getNoteById(id: Int): Flow<Note>{
        return db.getNoteDao().getNoteById(id)
    }

    suspend fun deleteNoteById(id: Int){
        db.getNoteDao().deleteNoteById(id)
    }

    suspend fun deleteNotes(note: Note){
        db.getNoteDao().deleteNote(note)
    }

    fun searchNote(text: String?): Flow<List<Note>>{
        return db.getNoteDao().query(text)
    }
}