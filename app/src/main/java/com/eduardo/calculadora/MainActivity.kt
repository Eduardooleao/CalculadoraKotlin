package com.eduardo.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Button
import com.eduardo.calculador.FP
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.liveRegion
import com.eduardo.calculador.CCIp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface (modifier = Modifier.fillMaxSize()){
                    CalculadoraScreen()
                }
            }
        }
    }
}

@Composable
fun CalculadoraScreen() {
    var inputAtual by remember {mutableStateOf("")}
    val calculadora = remember { Calculadora()}
    var displayText by remember { mutableStateOf("")}
    var op1 by remember { mutableStateOf(0.0)}
    var operador by remember { mutableStateOf("")}
    var inputUser by remember { mutableStateOf("") }
    var isCalculado by remember { mutableStateOf(false) }
    var multiplier by remember { mutableStateOf(1f) }


    Column(
        modifier = Modifier
            .background(FP)
            .fillMaxSize()
            .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {

        Text(
            text = displayText,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 80.sp * multiplier,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                color = CCIp
            ),
            maxLines = 1,
            softWrap = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .semantics { liveRegion = LiveRegionMode.Polite},
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    multiplier *= 0.9f
                }
            }
        )

        // 1. Lista de botões com as cores do iPhone (Cor do Botão, Cor do Texto)
        val botoes = listOf(
            listOf(
                "C" to Color.LightGray,
                "DEL" to Color.LightGray,
                "%" to Color.LightGray,
                "/" to Color(0xFFFF9F0A)
            ),
            listOf(
                "7" to Color(0xFF333333),
                "8" to Color(0xFF333333),
                "9" to Color(0xFF333333),
                "*" to Color(0xFFFF9F0A)
            ),
            listOf(
                "4" to Color(0xFF333333),
                "5" to Color(0xFF333333),
                "6" to Color(0xFF333333),
                "-" to Color(0xFFFF9F0A)
            ),
            listOf(
                "1" to Color(0xFF333333),
                "2" to Color(0xFF333333),
                "3" to Color(0xFF333333),
                "+" to Color(0xFFFF9F0A)
            ),
            listOf("0" to Color(0xFF333333), "=" to Color(0xFFFF9F0A))
        )

        botoes.forEach { linha ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp) // Espaço entre os botões
            ) {
                linha.forEach { (label, corBotao) ->
                    // Define a cor do texto: botões claros tem texto preto, o resto é branco
                    val corTexto = if (corBotao == Color.LightGray) Color.Black else Color.White
                    // O botão "0" é mais largo (peso 2)
                    val peso = if (label == "0") 2f else 1f

                    Button(
                        onClick = {
                            when (label) {
                                in "0".."9" -> {
                                    if (isCalculado) {
                                        inputAtual = label
                                        displayText = label
                                        op1 = 0.0
                                        operador = ""
                                        isCalculado = false
                                    } else {
                                        inputAtual += label
                                        if (operador.isEmpty()) displayText = inputAtual
                                        else {
                                            val o1 = if (op1 % 1 == 0.0) op1.toInt()
                                                .toString() else op1.toString()
                                            displayText = "$o1 $operador $inputAtual"
                                        }
                                    }
                                }

                                "+", "-", "*", "/" -> {
                                    if (inputAtual.isNotEmpty() && operador.isNotEmpty()) {
                                        val op2 = inputAtual.toDouble()
                                        calculadora.setOperandos(op1, op2, operador)
                                        try {
                                            val resultado = calculadora.calcular()
                                            op1 = resultado
                                            operador = label
                                            inputAtual = ""

                                            val o1Formatado = if (op1 % 1 == 0.0) op1.toInt().toString() else op1.toString()
                                            displayText = "$o1Formatado $operador "
                                        } catch (e: Exception) {
                                            displayText = "Erro"
                                        }
                                    }
                                    else if (inputAtual.isNotEmpty()) {
                                        op1 = inputAtual.toDouble()
                                        operador = label
                                        val o1Formatado = if (op1 % 1 == 0.0) op1.toInt().toString() else op1.toString()
                                        displayText = "$o1Formatado $operador "
                                        inputAtual = ""
                                        isCalculado = false
                                    }
                                }

                                "=" -> {
                                    if (inputAtual.isNotEmpty() && operador.isNotEmpty()) {
                                        val op2 = inputAtual.toDouble()
                                        calculadora.setOperandos(op1, op2, operador)
                                        try {
                                            val res = calculadora.calcular()
                                            val r = if (res % 1 == 0.0) res.toInt()
                                                .toString() else res.toString()
                                            displayText = r
                                            inputAtual = r
                                            isCalculado = true
                                            operador = ""
                                        } catch (e: Exception) {
                                            displayText = "Erro"
                                        }
                                    }
                                }

                                "C" -> {
                                    inputAtual = ""; op1 = 0.0; operador = ""; displayText =
                                        ""; isCalculado = false
                                }

                                "DEL" -> {
                                    inputAtual =
                                        if (inputAtual.isNotEmpty()) inputAtual.dropLast(1) else ""
                                    displayText = inputAtual
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(peso)
                            .padding(vertical = 6.dp)
                            .then(if (label != "0") Modifier.aspectRatio(1f) else Modifier),
                        shape = CircleShape,
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = corBotao
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Medium,
                            color = corTexto
                        )
                    }
                }
            }
        }
    }
}


