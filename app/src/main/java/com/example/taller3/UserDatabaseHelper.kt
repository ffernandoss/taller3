package com.example.taller3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Database helper class for managing user data
class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Called when the database is created for the first time
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE) // Create the users table
    }

    // Called when the database needs to be upgraded
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // Drop the old table if it exists
        onCreate(db) // Create a new table
    }

    // Insert a new user into the database
    fun insertUser(name: String, color: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name) // Add the user's name
            put(COLUMN_COLOR, color) // Add the user's color
        }
        db.insert(TABLE_NAME, null, values) // Insert the values into the table
        db.close() // Close the database
    }

    // Update the color of an existing user
    fun updateUserColor(name: String, color: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COLOR, color) // Update the user's color
        }
        db.update(TABLE_NAME, values, "$COLUMN_NAME = ?", arrayOf(name)) // Update the table
        db.close() // Close the database
    }

    // Get the color of a user by their name
    fun getUserColor(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_COLOR), "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        val color = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COLOR)) else Color.White.toArgb()
        cursor.close() // Close the cursor
        db.close() // Close the database
        return color // Return the user's color
    }

    // Get a list of all users in the database
    fun getAllUsers(): List<Triple<String, Int, String>> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME, COLUMN_COLOR), null, null, null, null, null)
        val users = mutableListOf<Triple<String, Int, String>>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME)) // Get the user's name
                val color = getInt(getColumnIndexOrThrow(COLUMN_COLOR)) // Get the user's color
                val colorName = when (color) {
                    Color.Red.toArgb() -> "Rojo"
                    Color.Green.toArgb() -> "Verde"
                    Color.Blue.toArgb() -> "Azul"
                    Color.Yellow.toArgb() -> "Amarillo"
                    else -> "Desconocido"
                }
                users.add(Triple(name, color, colorName)) // Add the user to the list
            }
        }
        cursor.close() // Close the cursor
        db.close() // Close the database
        return users // Return the list of users
    }

    // Check if a user exists in the database
    fun userExists(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME), "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        val exists = cursor.count > 0 // Check if the user exists
        cursor.close() // Close the cursor
        db.close() // Close the database
        return exists // Return whether the user exists
    }

    // Delete a user from the database
    fun deleteUser(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(name)) // Delete the user
        db.close() // Close the database
    }

    companion object {
        private const val DATABASE_NAME = "user.db" // Database name
        private const val DATABASE_VERSION = 2 // Database version
        private const val TABLE_NAME = "users" // Table name
        private const val COLUMN_ID = "id" // Column for user ID
        private const val COLUMN_NAME = "name" // Column for user name
        private const val COLUMN_COLOR = "color" // Column for user color

        // SQL statement to create the users table
        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_COLOR INTEGER)"
    }
}