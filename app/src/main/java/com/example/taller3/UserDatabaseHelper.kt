package com.example.taller3

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// Clase de ayuda para la base de datos para gestionar los datos de los usuarios
class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Se llama cuando la base de datos se crea por primera vez
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE) // Crear la tabla de usuarios
    }

    // Se llama cuando la base de datos necesita ser actualizada
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") // Eliminar la tabla antigua si existe
        onCreate(db) // Crear una nueva tabla
    }

    // Insertar un nuevo usuario en la base de datos
    fun insertUser(name: String, color: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name) // Añadir el nombre del usuario
            put(COLUMN_COLOR, color) // Añadir el color del usuario
        }
        db.insert(TABLE_NAME, null, values) // Insertar los valores en la tabla
        db.close() // Cerrar la base de datos
    }

    // Actualizar el color de un usuario existente
    fun updateUserColor(name: String, color: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COLOR, color) // Actualizar el color del usuario
        }
        db.update(TABLE_NAME, values, "$COLUMN_NAME = ?", arrayOf(name)) // Actualizar la tabla
        db.close() // Cerrar la base de datos
    }

    // Obtener el color de un usuario por su nombre
    fun getUserColor(name: String): Int {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_COLOR), "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        val color = if (cursor.moveToFirst()) cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COLOR)) else Color.White.toArgb()
        cursor.close() // Cerrar el cursor
        db.close() // Cerrar la base de datos
        return color // Devolver el color del usuario
    }

    // Obtener una lista de todos los usuarios en la base de datos
    fun getAllUsers(): List<Triple<String, Int, String>> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME, COLUMN_COLOR), null, null, null, null, null)
        val users = mutableListOf<Triple<String, Int, String>>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME)) // Obtener el nombre del usuario
                val color = getInt(getColumnIndexOrThrow(COLUMN_COLOR)) // Obtener el color del usuario
                val colorName = when (color) {
                    Color.Red.toArgb() -> "Rojo"
                    Color.Green.toArgb() -> "Verde"
                    Color.Blue.toArgb() -> "Azul"
                    Color.Yellow.toArgb() -> "Amarillo"
                    else -> "Desconocido"
                }
                users.add(Triple(name, color, colorName)) // Añadir el usuario a la lista
            }
        }
        cursor.close() // Cerrar el cursor
        db.close() // Cerrar la base de datos
        return users // Devolver la lista de usuarios
    }

    // Comprobar si un usuario existe en la base de datos
    fun userExists(name: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_NAME), "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        val exists = cursor.count > 0 // Comprobar si el usuario existe
        cursor.close() // Cerrar el cursor
        db.close() // Cerrar la base de datos
        return exists // Devolver si el usuario existe
    }

    // Eliminar un usuario de la base de datos
    fun deleteUser(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(name)) // Eliminar el usuario
        db.close() // Cerrar la base de datos
    }

    companion object {
        private const val DATABASE_NAME = "user.db" // Nombre de la base de datos
        private const val DATABASE_VERSION = 2 // Versión de la base de datos
        private const val TABLE_NAME = "users" // Nombre de la tabla
        private const val COLUMN_ID = "id" // Columna para el ID del usuario
        private const val COLUMN_NAME = "name" // Columna para el nombre del usuario
        private const val COLUMN_COLOR = "color" // Columna para el color del usuario

        // Sentencia SQL para crear la tabla de usuarios
        private const val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_COLOR INTEGER)"
    }
}