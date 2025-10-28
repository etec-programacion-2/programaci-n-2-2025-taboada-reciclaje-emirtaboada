package org.example

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val materiales = mutableListOf<MaterialReciclable>()
    val puntosReciclaje = mutableListOf<PuntoDeReciclaje>()
    val usuarios = mutableListOf<Usuario>()
    var usuarioActual: Usuario? = null

    while (true) {
        println("\n═══════════════════════════════════════")
        println("    SISTEMA DE RECICLAJE INTERACTIVO")
        println("═══════════════════════════════════════")
        println("1. Crear Material Reciclable")
        println("2. Crear Punto de Reciclaje")
        println("3. Crear/Seleccionar Usuario")
        println("4. Reciclar Material")
        println("5. Ver Materiales Creados")
        println("6. Ver Puntos de Reciclaje")
        println("7. Ver Usuarios")
        println("8. Ver Mi Perfil")
        println("9. Ver Historial de Punto de Reciclaje")
        println("10. Ver Tabla de Puntos")
        println("11. Ver Mi Historial de Reciclajes")
        println("12. Ver Todos los Registros")
        println("13. Ver Estadísticas Generales")
        println("14. Salir")
        println("═══════════════════════════════════════")
        print("Selecciona una opción: ")
        
        when (scanner.nextLine().toIntOrNull()) {
            1 -> MaterialReciclable.crear(materiales, scanner)
            2 -> PuntoDeReciclaje.crear(puntosReciclaje, scanner)
            3 -> usuarioActual = Usuario.crearOSeleccionar(usuarios, scanner)
            4 -> {
                if (usuarioActual != null && materiales.isNotEmpty() && puntosReciclaje.isNotEmpty()) {
                    usuarioActual.reciclar(materiales, puntosReciclaje, scanner)
                } else {
                    println("\n❌ Necesitas: usuario (opción 3), materiales (opción 1) y puntos (opción 2)")
                }
            }
            5 -> MaterialReciclable.verTodos(materiales)
            6 -> PuntoDeReciclaje.verTodos(puntosReciclaje)
            7 -> Usuario.verTodos(usuarios)
            8 -> usuarioActual?.verPerfil() ?: println("\n❌ No has seleccionado un usuario")
            9 -> {
                if (puntosReciclaje.isEmpty()) {
                    println("\n❌ No hay puntos de reciclaje creados")
                } else {
                    println("\n--- SELECCIONAR PUNTO DE RECICLAJE ---")
                    puntosReciclaje.forEachIndexed { index, punto ->
                        println("${index + 1}. ${punto.nombre}")
                    }
                    print("Selecciona el punto: ")
                    val index = (scanner.nextLine().toIntOrNull() ?: 1) - 1
                    puntosReciclaje.getOrNull(index)?.verHistorial() 
                        ?: println("\n❌ Punto no válido")
                }
            }
            10 -> CalculadoraPuntos.mostrarTablaPuntos()
            11 -> {
                println("\n¡Gracias por reciclar! Hasta pronto 🌱")
                scanner.close()
                return
            }
            else -> println("\n❌ Opción inválida. Intenta nuevamente.")
        }
    }
}