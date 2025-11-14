package org.example

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Representa un registro de una transacción de reciclaje.
 * Almacena quién recicló, qué material, dónde y cuándo.
 */
data class RegistroReciclaje(
    val usuario: Usuario,
    val material: MaterialReciclable,
    val puntoDeReciclaje: PuntoDeReciclaje,
    val cantidad: Double,
    val fecha: LocalDateTime = LocalDateTime.now()
) {
    /**
     * Formatea la fecha en un formato legible
     */
    fun fechaFormateada(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return fecha.format(formatter)
    }

    /**
     * Muestra el registro de forma legible
     * Ahora recibe la calculadora como parámetro (Principio D)
     */
    fun mostrar() {
        println("┌─────────────────────────────────────────")
        println("│ REGISTRO DE RECICLAJE")
        println("├─────────────────────────────────────────")
        println("│ Usuario: ${usuario.nombre}")
        println("│ Material: ${material.nombre} (${material.tipo})")
        println("│ Cantidad: ${"%.2f".format(cantidad)} kg")
        println("│ Punto: ${puntoDeReciclaje.nombre}")
        println("│ Fecha: ${fechaFormateada()}")
        println("│ Puntos ganados: ${CalculadoraPuntos.calcularPuntos(material, cantidad)}")
        println("└─────────────────────────────────────────")
    }

    override fun toString(): String {
        return "${fechaFormateada()} | ${usuario.nombre} recicló ${"%.2f".format(cantidad)} kg de ${material.nombre} en ${puntoDeReciclaje.nombre}"
    }
}