package org.example

import java.util.Scanner

/**
 * Punto de entrada principal de la aplicación.
 * Implementa un menú interactivo de consola para el sistema de reciclaje.
 */
fun main() {
    val scanner = Scanner(System.`in`)

    // ✅ CARGAR DATOS AL INICIAR (Persistencia)
    println("🔄 Iniciando sistema...")
    val datosCargados = GestorPersistencia.cargarTodo()
    val materiales = mutableListOf<MaterialReciclable>()
    val puntosReciclaje = datosCargados.puntos.toMutableList()
    val usuarios = datosCargados.usuarios.toMutableList()
    var usuarioActual: Usuario? = null

    // Restaurar registros en el repositorio
    datosCargados.registros.forEach { RepositorioRegistros.agregar(it) }

    println("✅ Sistema listo\n")

    // ✅ BUCLE PRINCIPAL - La aplicación no se cierra hasta seleccionar "Salir"
    var continuar = true
    while (continuar) {
        mostrarMenuPrincipal(usuarioActual)

        print("➤ Selecciona una opción: ")
        val opcion = leerEntero(scanner)

        when (opcion) {
            1 -> menuGestionUsuarios(usuarios, scanner, usuarioActual) { usuarioActual = it }
            2 -> menuGestionMateriales(materiales, scanner)
            3 -> menuGestionPuntos(puntosReciclaje, scanner)
            4 -> registrarReciclaje(usuarioActual, materiales, puntosReciclaje, scanner)
            5 -> menuConsultas(usuarioActual, puntosReciclaje, scanner)
            6 -> verTablaDePuntos()
            7 -> guardarDatosManualmente(usuarios, puntosReciclaje)
            8 -> continuar = salirDeLaAplicacion(usuarios, puntosReciclaje, scanner)
            else -> println("\n❌ Opción inválida. Por favor, selecciona un número del 1 al 8.\n")
        }

        // Pausa para que el usuario pueda leer el resultado
        if (continuar && opcion in 1..8) {
            print("\nPresiona ENTER para continuar...")
            scanner.nextLine()
        }
    }
}

/**
 * Muestra el menú principal del sistema
 */
fun mostrarMenuPrincipal(usuarioActual: Usuario?) {
    println("\n╔════════════════════════════════════════════════╗")
    println("║     SISTEMA DE RECICLAJE INTERACTIVO 🌱       ║")
    println("╚════════════════════════════════════════════════╝")

    if (usuarioActual != null) {
        println("👤 Usuario: ${usuarioActual.nombre} (${usuarioActual.puntos} pts)")
    } else {
        println("⚠️  No hay usuario seleccionado")
    }

    println("\n┌─────────────────────────────────────────────────┐")
    println("│  MENÚ PRINCIPAL                                 │")
    println("├─────────────────────────────────────────────────┤")
    println("│  1. 👤 Gestión de Usuarios                      │")
    println("│  2. 📦 Gestión de Materiales                    │")
    println("│  3. 📍 Gestión de Puntos de Reciclaje          │")
    println("│  4. ♻️  Registrar Reciclaje                     │")
    println("│  5. 📊 Consultas e Informes                     │")
    println("│  6. 💰 Ver Tabla de Puntos                      │")
    println("│  7. 💾 Guardar Datos                            │")
    println("│  8. 🚪 Salir                                    │")
    println("└─────────────────────────────────────────────────┘")
}

/**
 * Submenú: Gestión de Usuarios
 */
fun menuGestionUsuarios(
    usuarios: MutableList<Usuario>,
    scanner: Scanner,
    usuarioActual: Usuario?,
    actualizarUsuario: (Usuario) -> Unit
) {
    println("\n╔════════════════════════════════════════════════╗")
    println("║           GESTIÓN DE USUARIOS 👤               ║")
    println("╚════════════════════════════════════════════════╝")
    println("1. Crear nuevo usuario")
    println("2. Seleccionar usuario existente")
    println("3. Ver todos los usuarios")
    println("4. Ver mi perfil")
    println("5. Volver al menú principal")
    print("\n➤ Opción: ")

    when (leerEntero(scanner)) {
        1 -> actualizarUsuario(Usuario.crearOSeleccionar(usuarios, scanner))
        2 -> {
            if (usuarios.isEmpty()) {
                println("\n❌ No hay usuarios registrados. Crea uno primero.")
            } else {
                actualizarUsuario(Usuario.crearOSeleccionar(usuarios, scanner))
            }
        }
        3 -> Usuario.verTodos(usuarios)
        4 -> {
            if (usuarioActual != null) {
                usuarioActual.verPerfil()
            } else {
                println("\n❌ No hay usuario seleccionado. Selecciona uno primero.")
            }
        }
        5 -> println("\n↩️  Volviendo al menú principal...")
        else -> println("\n❌ Opción inválida")
    }
}

/**
 * Submenú: Gestión de Materiales
 */
fun menuGestionMateriales(materiales: MutableList<MaterialReciclable>, scanner: Scanner) {
    println("\n╔════════════════════════════════════════════════╗")
    println("║         GESTIÓN DE MATERIALES 📦               ║")
    println("╚════════════════════════════════════════════════╝")
    println("1. Crear nuevo material")
    println("2. Ver materiales registrados")
    println("3. Volver al menú principal")
    print("\n➤ Opción: ")

    when (leerEntero(scanner)) {
        1 -> MaterialReciclable.crear(materiales, scanner)
        2 -> MaterialReciclable.verTodos(materiales)
        3 -> println("\n↩️  Volviendo al menú principal...")
        else -> println("\n❌ Opción inválida")
    }
}

/**
 * Submenú: Gestión de Puntos de Reciclaje
 */
fun menuGestionPuntos(puntos: MutableList<PuntoDeReciclaje>, scanner: Scanner) {
    println("\n╔════════════════════════════════════════════════╗")
    println("║      GESTIÓN DE PUNTOS DE RECICLAJE 📍        ║")
    println("╚════════════════════════════════════════════════╝")
    println("1. Crear nuevo punto de reciclaje")
    println("2. Ver puntos de reciclaje")
    println("3. Ver historial de un punto")
    println("4. Ver estadísticas de un punto")
    println("5. Volver al menú principal")
    print("\n➤ Opción: ")

    when (leerEntero(scanner)) {
        1 -> PuntoDeReciclaje.crear(puntos, scanner)
        2 -> PuntoDeReciclaje.verTodos(puntos)
        3 -> verHistorialPunto(puntos, scanner)
        4 -> verEstadisticasPunto(puntos, scanner)
        5 -> println("\n↩️  Volviendo al menú principal...")
        else -> println("\n❌ Opción inválida")
    }
}

/**
 * Opción principal: Registrar Reciclaje
 */
fun registrarReciclaje(
    usuarioActual: Usuario?,
    materiales: List<MaterialReciclable>,
    puntos: List<PuntoDeReciclaje>,
    scanner: Scanner
) {
    println("\n╔════════════════════════════════════════════════╗")
    println("║          REGISTRAR RECICLAJE ♻️                ║")
    println("╚════════════════════════════════════════════════╝")

    if (usuarioActual == null) {
        println("❌ Error: No hay usuario seleccionado")
        println("💡 Ve a 'Gestión de Usuarios' para crear o seleccionar un usuario")
        return
    }

    if (materiales.isEmpty()) {
        println("❌ Error: No hay materiales disponibles")
        println("💡 Ve a 'Gestión de Materiales' para crear materiales")
        return
    }

    if (puntos.isEmpty()) {
        println("❌ Error: No hay puntos de reciclaje disponibles")
        println("💡 Ve a 'Gestión de Puntos' para crear puntos de reciclaje")
        return
    }

    usuarioActual.reciclar(materiales, puntos, scanner)
}

/**
 * Submenú: Consultas e Informes
 */
fun menuConsultas(
    usuarioActual: Usuario?,
    puntos: List<PuntoDeReciclaje>,
    scanner: Scanner
) {
    println("\n╔════════════════════════════════════════════════╗")
    println("║        CONSULTAS E INFORMES 📊                 ║")
    println("╚════════════════════════════════════════════════╝")
    println("1. Ver mis puntos")
    println("2. Ver mi perfil completo")
    println("3. Ver mi historial de reciclajes")
    println("4. Ver todos los registros del sistema")
    println("5. Ver estadísticas generales")
    println("6. Volver al menú principal")
    print("\n➤ Opción: ")

    when (leerEntero(scanner)) {
        1 -> {
            if (usuarioActual != null) {
                println("\n🌟 Tus puntos: ${usuarioActual.puntos}")
            } else {
                println("\n❌ No hay usuario seleccionado")
            }
        }
        2 -> {
            if (usuarioActual != null) {
                usuarioActual.verPerfil()
            } else {
                println("\n❌ No hay usuario seleccionado")
            }
        }
        3 -> {
            if (usuarioActual != null) {
                usuarioActual.verHistorialReciclaje()
            } else {
                println("\n❌ No hay usuario seleccionado")
            }
        }
        4 -> RepositorioRegistros.verTodos()
        5 -> RepositorioRegistros.verEstadisticas()
        6 -> println("\n↩️  Volviendo al menú principal...")
        else -> println("\n❌ Opción inválida")
    }
}

/**
 * Muestra la tabla de puntos por tipo de material
 */
fun verTablaDePuntos() {
    CalculadoraPuntos.mostrarTablaPuntos()
}

/**
 * Ver historial de un punto de reciclaje
 */
fun verHistorialPunto(puntos: List<PuntoDeReciclaje>, scanner: Scanner) {
    if (puntos.isEmpty()) {
        println("\n❌ No hay puntos de reciclaje creados")
        return
    }

    println("\n--- SELECCIONAR PUNTO DE RECICLAJE ---")
    puntos.forEachIndexed { index, punto ->
        println("${index + 1}. ${punto.nombre}")
    }
    print("➤ Selecciona el punto: ")
    val index = leerEntero(scanner) - 1
    puntos.getOrNull(index)?.verHistorial()
        ?: println("\n❌ Punto no válido")
}

/**
 * Ver estadísticas de un punto de reciclaje
 */
fun verEstadisticasPunto(puntos: List<PuntoDeReciclaje>, scanner: Scanner) {
    if (puntos.isEmpty()) {
        println("\n❌ No hay puntos de reciclaje creados")
        return
    }

    println("\n--- ESTADÍSTICAS DE PUNTO DE RECICLAJE ---")
    puntos.forEachIndexed { index, punto ->
        println("${index + 1}. ${punto.nombre}")
    }
    print("➤ Selecciona el punto: ")
    val index = leerEntero(scanner) - 1
    val punto = puntos.getOrNull(index)

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

/**
 * Guarda los datos manualmente
 */
fun guardarDatosManualmente(usuarios: List<Usuario>, puntos: List<PuntoDeReciclaje>) {
    println("\n💾 Guardando datos...")
    GestorPersistencia.guardarTodo(
        usuarios = usuarios,
        puntos = puntos,
        registros = RepositorioRegistros.obtenerTodos()
    )
}

/**
 * Maneja la salida de la aplicación
 * @return false para terminar el bucle principal
 */
fun salirDeLaAplicacion(
    usuarios: List<Usuario>,
    puntos: List<PuntoDeReciclaje>,
    scanner: Scanner
): Boolean {
    println("\n╔════════════════════════════════════════════════╗")
    println("║              SALIR DEL SISTEMA                 ║")
    println("╚════════════════════════════════════════════════╝")

    print("¿Deseas guardar los cambios antes de salir? (S/N): ")
    val respuesta = scanner.nextLine().trim().uppercase()

    if (respuesta == "S" || respuesta == "SI" || respuesta == "Y" || respuesta == "YES") {
        GestorPersistencia.guardarTodo(
            usuarios = usuarios,
            puntos = puntos,
            registros = RepositorioRegistros.obtenerTodos()
        )
        println("\n💾 Datos guardados exitosamente")
    } else {
        println("\n⚠️  Cambios descartados")
    }

    println("\n╔════════════════════════════════════════════════╗")
    println("║  ¡Gracias por usar el sistema de reciclaje!   ║")
    println("║              🌍 ♻️ 🌱                          ║")
    println("║     Juntos construimos un mundo mejor         ║")
    println("╚════════════════════════════════════════════════╝\n")

    scanner.close()
    return false  // Retorna false para terminar el bucle
}

/**
 * Lee un entero de forma segura, retorna 0 si hay error
 */
fun leerEntero(scanner: Scanner): Int {
    return try {
        val input = scanner.nextLine().trim()
        input.toIntOrNull() ?: 0
    } catch (e: Exception) {
        0
    }
}

/**
 * Lee una cadena de forma segura
 */
fun leerCadena(scanner: Scanner): String {
    return try {
        scanner.nextLine().trim()
    } catch (e: Exception) {
        ""
    }
}