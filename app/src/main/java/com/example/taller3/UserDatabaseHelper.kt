package com.example.taller3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(name: String, color: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_COLOR, color)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateUserColor(name: String, color: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COLOR, color)
        }
        db.update(TABLE_NAME, values, "$COLUMN_NAME = ?", arrayOf(name))
        db.close()
    }

    fun getUserColor(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_COLOR), "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        val color = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COLOR)) else Color.White.toArgb()
        cursor.close()
        db.close()
        return color
    }

    fun getAllUsers(): List<Triple<String, Int, String>> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME, COLUMN_COLOR), null, null, null, null, null)
        val users = mutableListOf<Triple<String, Int, String>>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val color = getInt(getColumnIndexOrThrow(COLUMN_COLOR))
                val colorName = when (color) {
                    Color.Red.toArgb() -> "Rojo"
                    Color.Green.toArgb() -> "Verde"
                    Color.Blue.toArgb() -> "Azul"
                    Color.Yellow.toArgb() -> "Amarillo"
                    else -> "Desconocido"
                }
                users.add(Triple(name, color, colorName))
            }
        }
        cursor.close()
        db.close()
        return users
    }

    fun userExists(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME), "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun deleteUser(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(name))
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "user.db"
        private const val DATABASE_VERSION = 2
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_COLOR = "color"

        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_COLOR INTEGER)"
    }
}