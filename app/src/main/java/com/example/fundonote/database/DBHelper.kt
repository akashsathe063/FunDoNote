package com.example.fundonote.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fundonote.model.Notes

class DBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_Version) {
    companion object {
        private val DB_NAME = "NotesDB"
        private val DB_Version = 1
        private val TABLE_NAME = "Note"
        private val NOTE_ID = "NoteId"
        private val NOTE_TiTle = "NoteTitle"
        private val NOTE_DESCRIPTION = "NoteDescription"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME($NOTE_ID TEXT PRIMARY KEY  ,$NOTE_TiTle TEXT,$NOTE_DESCRIPTION TEXT)"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }


    fun getAllNotes(): ArrayList<Notes> {
        val noteArrayList = ArrayList<Notes>()
        val sqliteDataBase = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = sqliteDataBase.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val notes = Notes()
                    notes.noteId = cursor.getColumnIndex(NOTE_ID).toString()
                    notes.noteTitle = cursor.getColumnIndex(NOTE_TiTle).toString()
                    notes.noteDescription = cursor.getColumnIndex(NOTE_DESCRIPTION).toString()
                    noteArrayList.add(notes)
                } while (cursor.moveToNext())
            }
        }
        //  cursor.close()
        return noteArrayList
    }

    // insertion operation
    fun addNotes(notes: Notes): Boolean {
        val sqliteDataBase = this.writableDatabase
        val values = ContentValues()
        values.put(NOTE_ID, notes.noteId)
        values.put(NOTE_TiTle, notes.noteTitle)
        values.put(NOTE_DESCRIPTION, notes.noteDescription)
        val _Success: Long = sqliteDataBase.insert(TABLE_NAME, null, values)
        //  sqliteDataBase.close()
        return (_Success.toInt() != -1)

    }

}