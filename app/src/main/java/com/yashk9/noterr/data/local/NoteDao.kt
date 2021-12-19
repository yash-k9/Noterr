package com.yashk9.noterr.data.local

import androidx.room.*
import com.yashk9.noterr.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM note order by createdAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * from note where id = :id")
    fun getNoteById(id: Int): Flow<Note>

    @Query("DELETE FROM note WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note WHERE title LIKE '%' || :text || '%' order by createdAt DESC")
    fun query(text: String?): Flow<List<Note>>
}