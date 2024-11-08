package com.example.taller3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taller3.ui.theme.Taller3Theme

class SegundaActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller3Theme {
                SegundaPantalla()
            }
        }
    }
}

@Composable
fun SegundaPantalla() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var name by remember { mutableStateOf("") }
    var savedName by remember { mutableStateOf("") }
    val dbHelper = UserDatabaseHelper(context)
    val users = remember { mutableStateListOf<Triple<String, Int, String>>() }
    var showDialog by remember { mutableStateOf(false) }
    var deleteName by remember { mutableStateOf("") }
    var showNotFoundDialog by remember { mutableStateOf(false) }
    var showUserExistsDialog by remember { mutableStateOf(false) }
    var backgroundColor by remember { mutableStateOf(Color(sharedPreferences.getInt("background_color", Color.White.toArgb()))) }

    LaunchedEffect(Unit) {
        users.addAll(dbHelper.getAllUsers())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                if (dbHelper.userExists(name)) {
                    showUserExistsDialog = true
                } else {
                    dbHelper.insertUser(name, backgroundColor.toArgb())
                    sharedPreferences.edit().putString("user_name", name).apply()
                    savedName = name
                    users.clear()
                    users.addAll(dbHelper.getAllUsers())
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Guardar Nombre")
        }
        Text(
            text = "Nombre guardado: $savedName",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                showDialog = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Borrar Usuario de SQLite")
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, AjustesActividad::class.java))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Ir a Ajustes")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Usuarios guardados en SQLite:", modifier = Modifier.padding(bottom = 8.dp))
        users.forEach { (user, color, colorName) ->
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(text = user, modifier = Modifier.weight(1f))
                Box(modifier = Modifier.size(24.dp).background(Color(color)))
                Text(text = colorName, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Borrar Usuario") },
            text = {
                Column {
                    Text("Introduce el nombre del usuario a borrar:")
                    TextField(
                        value = deleteName,
                        onValueChange = { deleteName = it }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dbHelper.userExists(deleteName)) {
                            dbHelper.deleteUser(deleteName)
                            users.clear()
                            users.addAll(dbHelper.getAllUsers())
                            showDialog = false
                        } else {
                            showNotFoundDialog = true
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showNotFoundDialog) {
        AlertDialog(
            onDismissRequest = { showNotFoundDialog = false },
            title = { Text("Usuario no encontrado") },
            text = { Text("El usuario con el nombre \"$deleteName\" no fue encontrado.") },
            confirmButton = {
                Button(onClick = { showNotFoundDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (showUserExistsDialog) {
        AlertDialog(
            onDismissRequest = { showUserExistsDialog = false },
            title = { Text("Usuario ya existe") },
            text = { Text("El usuario con el nombre \"$name\" ya existe.") },
            confirmButton = {
                Button(onClick = { showUserExistsDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SegundaPantallaPreview() {
    Taller3Theme {
        SegundaPantalla()
    }
}