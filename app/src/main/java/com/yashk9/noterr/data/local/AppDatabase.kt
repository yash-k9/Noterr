package com.yashk9.noterr.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yashk9.noterr.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase(): RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    fun getInstance(context: Context) = Room.databaseBuilder(context, AppDatabase::class.java, "note.db")
    .fallbackToDestructiveMigration()
    .build()
}