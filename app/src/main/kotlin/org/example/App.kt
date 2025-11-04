package org.example

import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    
    // ✅ CARGAR DATOS AL INICIAR
    val datosCargados = GestorPersistencia.cargarTodo()
    val materiales = mutableListOf<MaterialReciclable>()
    val puntosReciclaje = datosCargados.puntos.toMutableList()
    val usuarios = datosCargados.usuarios.toMutableList()
    var usuarioActual: Usuario? = null
    
    // Restaurar registros en el repositorio
    datosCargados.registros.forEach { RepositorioRegistros.agregar(it) }

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
        println("14. Ver Estadísticas de Punto")
        println("15. Guardar Datos Manualmente")
        println("16. Salir")
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
            11 -> usuarioActual?.verHistorialReciclaje() ?: println("\n❌ No has seleccionado un usuario")
            12 -> RepositorioRegistros.verTodos()
            13 -> RepositorioRegistros.verEstadisticas()
            14 -> {
                if (puntosReciclaje.isEmpty()) {
                    println("\n❌ No hay puntos de reciclaje creados")
                } else {
                    println("\n--- ESTADÍSTICAS DE PUNTO DE RECICLAJE ---")
                    puntosReciclaje.forEachIndexed { index, punto ->
                        println("${index + 1}. ${punto.nombre}")
                    }
                    print("Selecciona el punto: ")
                    val index = (scanner.nextLine().toIntOrNull() ?: 1) - 1
                    val punto = puntosReciclaje.getOrNull(index)
                    
                    if (punto != null) {
                        val stats = GestorDeReciclaje.calcularEstadisticasPunto(punto)
                        println("\n═══════════════════════════════════════")
                        println("  ESTADÍSTICAS: ${punto.nombre}")
                        println("═══════════════════════════════════════")
                        println("📍 Dirección: ${punto.direccion}")
                        println("📦 Materiales aceptados: ${punto.materialesAceptados}")
                        println("\n📊 Estadísticas:")
                        println("  • Total de reciclajes recibidos: ${stats.totalReciclajes}")
                        println("  • Total de kg recibidos: ${"%.2f".format(stats.totalKgRecibidos)} kg")
                        println("  • Usuarios únicos: ${stats.usuariosUnicos}")
                        println("═══════════════════════════════════════")
                    } else {
                        println("\n❌ Punto no válido")
                    }
                }
            }
            15 -> {
                // ✅ GUARDAR DATOS MANUALMENTE
                GestorPersistencia.guardarTodo(
                    usuarios = usuarios,
                    puntos = puntosReciclaje,
                    registros = RepositorioRegistros.obtenerTodos()
                )
            }
            16 -> {
                // ✅ GUARDAR DATOS AL SALIR
                print("\n¿Guardar cambios antes de salir? (S/N): ")
                if (scanner.nextLine().uppercase() != "N") {
                    GestorPersistencia.guardarTodo(
                        usuarios = usuarios,
                        puntos = puntosReciclaje,
                        registros = RepositorioRegistros.obtenerTodos()
                    )
                }
                
                println("\n¡Gracias por usar el sistema de reciclaje! 🌎♻️")
                scanner.close()
                return
            }
            else -> println("\n❌ Opción inválida. Intenta nuevamente.")
        }
    }
}