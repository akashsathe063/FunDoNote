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
      //  private val USER_ID = "UserId"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME($NOTE_ID TEXT  PRIMARY KEY,$NOTE_TiTle TEXT,$NOTE_DESCRIPTION TEXT)"
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
                  //  notes.userId = cursor.getColumnIndex(USER_ID).toString()
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
      //  values.put(USER_ID, notes.userId)
          values.put(NOTE_ID,notes.noteId)
        values.put(NOTE_TiTle, notes.noteTitle)
        values.put(NOTE_DESCRIPTION, notes.noteDescription)
        val _Success: Long = sqliteDataBase.insert(TABLE_NAME, null, values)
        //  sqliteDataBase.close()
        return (_Success.toInt() != -1)

    }

    // select data for perticular id
    fun readSingleNote(noteId: String): Notes {
        val note = Notes()
        val sqliteDataBase = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $NOTE_ID = '$noteId'"
        val cursor = sqliteDataBase.rawQuery(selectQuery, null,null)

        cursor?.moveToFirst()
     //   note.userId = cursor.getColumnIndex(USER_ID).toString()
        note.noteId = cursor.getColumnIndex(NOTE_ID).toString()
        note.noteTitle = cursor.getColumnIndex(NOTE_TiTle).toString()
        note.noteDescription = cursor.getColumnIndex(NOTE_DESCRIPTION).toString()
        return note
    }

    // delete operation
    fun deleteNote(noteId: String):Boolean{
        val sqliteDataBase = this.writableDatabase
        val _Success = sqliteDataBase.delete(TABLE_NAME, NOTE_ID + "=?", arrayOf(noteId.toString())).toLong()
        return (_Success.toInt() != -1)
    }
    // update operation
    fun updateNote(notes: Notes):Boolean{
        val sqliteDataBase = this.writableDatabase
        val values = ContentValues()
        values.put(NOTE_TiTle, notes.noteTitle)
        values.put(NOTE_DESCRIPTION, notes.noteDescription)
        val _Success: Long = sqliteDataBase.update(TABLE_NAME,  values, NOTE_ID + "=?", arrayOf(notes.noteId.toString())).toLong()
        return (_Success.toInt() != -1)
    }
}