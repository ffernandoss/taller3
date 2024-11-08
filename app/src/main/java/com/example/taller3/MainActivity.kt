package com.example.taller3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taller3.ui.theme.Taller3Theme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enable edge-to-edge display
        setContent {
            Taller3Theme {
                val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                val backgroundColor = remember { Color(sharedPreferences.getInt("background_color", Color.White.toArgb())) }
                Scaffold(modifier = Modifier.fillMaxSize().background(backgroundColor)) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ) {
                        Greeting(
                            name = getGreetingMessage(),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        NavigateButton()
                    }
                }
            }
        }
    }

    // Function to get a greeting message based on the current time
    private fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Buenos días"
            in 12..17 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Composable
fun NavigateButton() {
    val context = LocalContext.current
    Button(onClick = {
        context.startActivity(Intent(context, SegundaActividad::class.java))
    }) {
        Text(text = "Ir a Segunda Actividad")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Taller3Theme {
        Column {
            Greeting("Buenos días")
            NavigateButton()
        }
    }
}