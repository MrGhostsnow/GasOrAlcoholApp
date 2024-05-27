package com.example.gasoralcohol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gasoralcohol.ui.theme.GasOrAlcoholTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GasOrAlcoholTheme() {
                App()
                }
        }
    }
}

@Composable
fun App() {
    val gasValue = remember { mutableStateOf("") }
    val alcoholValue = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }

    Column(
        Modifier
            .background(color = Color(0xFF833131))
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Álcool ou Gasolina?", style = TextStyle(
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            AnimatedVisibility(visible = errorMessage.value.isNotEmpty()) {
                Text(
                    text = errorMessage.value, style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Red
                    )
                )
            }

            AnimatedVisibility(visible = alcoholValue.value.isNotBlank() && gasValue.value.isNotBlank() && errorMessage.value.isEmpty()) {
                Text(
                    text = calculateBestOption(gasValue.value, alcoholValue.value),
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }

            TextField(
                value = gasValue.value,
                onValueChange = {
                    gasValue.value = filterInput(it)
                    errorMessage.value = validateInput(gasValue.value, alcoholValue.value)
                },
                label = {
                    Text(text = "Gasolina (preço por litro)")
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
            TextField(
                value = alcoholValue.value,
                onValueChange = {
                    alcoholValue.value = filterInput(it)
                    errorMessage.value = validateInput(gasValue.value, alcoholValue.value)
                },
                label = {
                    Text(text = "Álcool (preço por litro)")
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

fun filterInput(input: String): String {
    val regex = Regex("^[0-9]*\\.?[0-9]*$")
    return if (regex.matches(input)) input else input.dropLast(1)
}

fun validateInput(gas: String, alcohol: String): String {
    return try {
        if (gas.isNotBlank()) gas.toDouble()
        if (alcohol.isNotBlank()) alcohol.toDouble()
        ""
    } catch (e: NumberFormatException) {
        "Por favor, insira valores válidos."
    }
}

fun calculateBestOption(gas: String, alcohol: String): String {
    return try {
        val alcoholPrice = alcohol.toDouble()
        val gasPrice = gas.toDouble()
        if (alcoholPrice / gasPrice > 0.7) "Gasolina" else "Álcool"
    } catch (e: NumberFormatException) {
        ""
    }
}


@Preview
@Composable
fun AppPreview() {
    GasOrAlcoholTheme {
        App()
    }
}
