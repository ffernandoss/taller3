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

class AjustesActividad : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Taller3Theme {
                AjustesPantalla()
            }
        }
    }
}

@Composable
fun AjustesPantalla() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val savedColor = sharedPreferences.getInt("background_color", Color.White.toArgb())
    var backgroundColor by remember { mutableStateOf(Color(savedColor)) }

    // Función para guardar el color seleccionado en SharedPreferences
    fun saveColor(color: Color) {
        with(sharedPreferences.edit()) {
            putInt("background_color", color.toArgb())
            apply()
        }
        backgroundColor = color
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Text("Seleccione el color de fondo:", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón para seleccionar el color rojo
            Button(onClick = { saveColor(Color.Red) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Rojo")
            }
            // Botón para seleccionar el color verde
            Button(onClick = { saveColor(Color.Green) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Green)) {
                Text("Verde")
            }
            // Botón para seleccionar el color azul
            Button(onClick = { saveColor(Color.Blue) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)) {
                Text("Azul")
            }
            // Botón para seleccionar el color amarillo
            Button(onClick = { saveColor(Color.Yellow) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow)) {
                Text("Amarillo")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para navegar de vuelta a SegundaActividad
        Button(
            onClick = {
                val intent = Intent(context, SegundaActividad::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver a Segunda Actividad")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AjustesPantallaPreview() {
    Taller3Theme {
        AjustesPantalla()
    }
}