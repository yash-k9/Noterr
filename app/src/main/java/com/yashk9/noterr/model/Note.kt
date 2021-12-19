package com.yashk9.noterr.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var content: String,
    var createdAt: Long = System.currentTimeMillis(),
    var theme: String? = null,
    var label: String? = null
) : Serializable {
    fun toDateFormat(): String{
        val formatter = Formatter()
        val calendar = Calendar.getInstance()
        calendar.time = Date(createdAt)
        return formatter.format("%tB %td", calendar, calendar).toString()
    }
}
