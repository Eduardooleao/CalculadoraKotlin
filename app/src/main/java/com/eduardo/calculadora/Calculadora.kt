package com.eduardo.calculadora

class Calculadora(private var operando1: Double = 0.0, private var operando2: Double = 0.0, private var operador: String = "") {

    // Função pra definir operandos e operador
    fun setOperandos(op1: Double, op2: Double, op: String) {
        operando1 = op1
        operando2 = op2
        operador = op
    }

    // Função pra calcular o resultado
    fun calcular(): Double {
        return when (operador) {
            "+" -> operando1 + operando2
            "-" -> operando1 - operando2
            "*" -> operando1 * operando2
            "/" -> if (operando2 != 0.0) operando1 / operando2 else throw ArithmeticException("Divisão por zero!")
            else -> 0.0
        }
    }

    // Função para apagar o ultimo caractere
    fun apagarUltimoCaractere(textoAtual: String): String {
        return if (textoAtual.isNotEmpty()) {
            textoAtual.dropLast(1)
        } else {
            ""
        }
    }

    // Função pra resetar
    fun resetar() {
        operando1 = 0.0
        operando2 = 0.0
        operador = ""
    }
}