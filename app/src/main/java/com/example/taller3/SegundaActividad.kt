package com.example.taller3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taller3.ui.theme.Taller3Theme

class SegundaActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SegundaActividadContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun SegundaActividadContent(padding: PaddingValues) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var savedName by remember { mutableStateOf("") }
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Button(
            onClick = {
                sharedPreferences.edit().putString("user_name", name).apply()
                savedName = name
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Guardar Nombre")
        }
        Text(
            text = "Nombre guardado: $savedName",
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )
        Button(
            onClick = { /* De momento no hace nada */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, AjustesActividad::class.java))
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Ir a Ajustes")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegundaActividadPreview() {
    Taller3Theme {
        SegundaActividadContent(PaddingValues())
    }
}