package com.example.taller3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taller3.ui.theme.Taller3Theme

class AjustesActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AjustesActividadContent(innerPadding)
                }
            }
        }
    }
}

@Composable
fun AjustesActividadContent(padding: PaddingValues) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    var backgroundColor by remember { mutableStateOf(Color.White) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
            .background(backgroundColor)
    ) {
        Text("Seleccione el color de fondo:", modifier = Modifier.padding(bottom = 16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            ColorButton(Color.Red, "Rojo") { color ->
                backgroundColor = color
                sharedPreferences.edit().putInt("background_color", color.toArgb()).apply()
            }
            ColorButton(Color.Green, "Verde") { color ->
                backgroundColor = color
                sharedPreferences.edit().putInt("background_color", color.toArgb()).apply()
            }
            ColorButton(Color.Blue, "Azul") { color ->
                backgroundColor = color
                sharedPreferences.edit().putInt("background_color", color.toArgb()).apply()
            }
        }
        Button(
            onClick = {
                context.startActivity(Intent(context, SegundaActividad::class.java))
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Volver a Segunda Actividad")
        }
    }
}

@Composable
fun ColorButton(color: Color, label: String, onClick: (Color) -> Unit) {
    Button(
        onClick = { onClick(color) },
        modifier = Modifier
            .size(50.dp)
            .background(color)
    ) {
        Text(label, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun AjustesActividadPreview() {
    Taller3Theme {
        AjustesActividadContent(PaddingValues())
    }
}