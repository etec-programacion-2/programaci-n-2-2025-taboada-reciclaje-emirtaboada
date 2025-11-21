package org.example

import java.util.Scanner

/**
 * Punto de entrada principal - MÁXIMO 10 LÍNEAS EJECUTABLES
 */
fun main() {
    val aplicacion = AplicacionReciclaje(Scanner(System.`in`))
    aplicacion.iniciar()
}

/**
 * CLASE PRINCIPAL DE LA APLICACIÓN
 * Cumple con Single Responsibility: Gestiona el flujo principal de la aplicación
 */
class AplicacionReciclaje(private val scanner: Scanner) {
    private val sistemaDatos: SistemaDatos = inicializarSistema()
    private var usuarioActual: Usuario? = null
    private val gestorMenu = GestorMenu(scanner)

    fun iniciar() {
        while (true) {
            gestorMenu.mostrarMenuPrincipal(usuarioActual)
            val opcion = LectorEntrada.leerEntero(scanner)

            if (!procesarOpcion(opcion)) {
                break
            }

            gestorMenu.pausarPantalla(opcion)
        }
    }

    private fun inicializarSistema(): SistemaDatos {
        println("🔄 Iniciando sistema...")
        val datosCargados = GestorPersistencia.cargarTodo()
        datosCargados.registros.forEach { RepositorioRegistros.agregar(it) }
        println("✅ Sistema listo\n")

        return SistemaDatos(
            materiales = datosCargados.materiales.toMutableList(),
            puntosReciclaje = datosCargados.puntos.toMutableList(),
            usuarios = datosCargados.usuarios.toMutableList()
        )
    }

    private fun procesarOpcion(opcion: Int): Boolean {
        val procesador = ProcesadorOpciones(sistemaDatos, scanner, usuarioActual) {
            usuarioActual = it
        }
        return procesador.procesar(opcion)
    }
}

/**
 * GESTOR DE MENÚ
 * Responsabilidad: Mostrar menús y pausar pantalla
 */
class GestorMenu(private val scanner: Scanner) {

    fun mostrarMenuPrincipal(usuarioActual: Usuario?) {
        println("\n╔═══════════════════════════════════════════════╗")
        println("║     SISTEMA DE RECICLAJE INTERACTIVO 🌱       ║")
        println("╚═══════════════════════════════════════════════╝")

        if (usuarioActual != null) {
            println("👤 Usuario: ${usuarioActual.nombre} (${usuarioActual.puntos} pts)")
        } else {
            println("⚠️  No hay usuario seleccionado")
        }

        println("\n┌───────────────────────────────────────────────┐")
        println("│  MENÚ PRINCIPAL                                 │")
        println("├───────────────────────────────────────────────┤")
        println("│  1. 👤 Gestión de Usuarios                      │")
        println("│  2. 📦 Gestión de Materiales                    │")
        println("│  3. 📍 Gestión de Puntos de Reciclaje          │")
        println("│  4. ♻️  Registrar Reciclaje                     │")
        println("│  5. 📊 Consultas e Informes                     │")
        println("│  6. 💰 Ver Tabla de Puntos                      │")
        println("│  7. 💾 Guardar Datos                            │")
        println("│  8. 🚪 Salir                                    │")
        println("└───────────────────────────────────────────────┘")
    }

    fun pausarPantalla(opcion: Int) {
        if (opcion in 1..8) {
            print("\nPresiona ENTER para continuar...")
            scanner.nextLine()
        }
    }
}

/**
 * PROCESADOR DE OPCIONES
 * Responsabilidad: Procesar las opciones del menú principal
 */
class ProcesadorOpciones(
    private val datos: SistemaDatos,
    private val scanner: Scanner,
    private val usuarioActual: Usuario?,
    private val actualizarUsuario: (Usuario) -> Unit
) {

    fun procesar(opcion: Int): Boolean {
        return when (opcion) {
            1 -> { MenuUsuarios(datos.usuarios, scanner, usuarioActual, actualizarUsuario).mostrar(); true }
            2 -> { MenuMateriales(datos.materiales, scanner).mostrar(); true }
            3 -> { MenuPuntosReciclaje(datos.puntosReciclaje, scanner).mostrar(); true }
            4 -> { RegistradorReciclaje(usuarioActual, datos.materiales, datos.puntosReciclaje, scanner).registrar(); true }
            5 -> { MenuConsultas(usuarioActual, datos.puntosReciclaje, scanner).mostrar(); true }
            6 -> { CalculadoraPuntos.mostrarTablaPuntos(); true }
            7 -> { GuardadorDatos(datos.usuarios, datos.materiales, datos.puntosReciclaje).guardar(); true }
            8 -> SalidaAplicacion(datos.usuarios, datos.materiales, datos.puntosReciclaje, scanner).ejecutar()
            else -> { println("\n❌ Opción inválida. Por favor, selecciona un número del 1 al 8.\n"); true }
        }
    }
}

/**
 * CLASE BASE ABSTRACTA PARA MENÚS
 * Implementa HERENCIA y POLIMORFISMO
 */
abstract class MenuBase(protected val scanner: Scanner) {
    abstract fun mostrar()
    abstract fun obtenerTitulo(): String

    protected fun mostrarEncabezado() {
        println("\n╔═══════════════════════════════════════════════╗")
        println("║           ${obtenerTitulo().padEnd(35)}     ║")
        println("╚═══════════════════════════════════════════════╝")
    }
}

/**
 * MENÚ DE USUARIOS - Hereda de MenuBase
 */
class MenuUsuarios(
    private val usuarios: MutableList<Usuario>,
    scanner: Scanner,
    private val usuarioActual: Usuario?,
    private val actualizarUsuario: (Usuario) -> Unit
) : MenuBase(scanner) {

    override fun obtenerTitulo() = "GESTIÓN DE USUARIOS 👤"

    override fun mostrar() {
        mostrarEncabezado()
        println("1. Crear nuevo usuario")
        println("2. Seleccionar usuario existente")
        println("3. Ver todos los usuarios")
        println("4. Ver mi perfil")
        println("5. Volver al menú principal")
        print("\n➤ Opción: ")

        when (LectorEntrada.leerEntero(scanner)) {
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
}

/**
 * MENÚ DE MATERIALES - Hereda de MenuBase
 */
class MenuMateriales(
    private val materiales: MutableList<MaterialReciclable>,
    scanner: Scanner
) : MenuBase(scanner) {

    override fun obtenerTitulo() = "GESTIÓN DE MATERIALES 📦"

    override fun mostrar() {
        mostrarEncabezado()
        println("1. Crear nuevo material")
        println("2. Ver materiales registrados")
        println("3. Volver al menú principal")
        print("\n➤ Opción: ")

        when (LectorEntrada.leerEntero(scanner)) {
            1 -> MaterialReciclable.crear(materiales, scanner)
            2 -> MaterialReciclable.verTodos(materiales)
            3 -> println("\n↩️  Volviendo al menú principal...")
            else -> println("\n❌ Opción inválida")
        }
    }
}

/**
 * MENÚ DE PUNTOS DE RECICLAJE - Hereda de MenuBase
 */
class MenuPuntosReciclaje(
    private val puntos: MutableList<PuntoDeReciclaje>,
    scanner: Scanner
) : MenuBase(scanner) {

    override fun obtenerTitulo() = "GESTIÓN DE PUNTOS DE RECICLAJE 📍"

    override fun mostrar() {
        mostrarEncabezado()
        println("1. Crear nuevo punto de reciclaje")
        println("2. Ver puntos de reciclaje")
        println("3. Ver historial de un punto")
        println("4. Ver estadísticas de un punto")
        println("5. Volver al menú principal")
        print("\n➤ Opción: ")

        when (LectorEntrada.leerEntero(scanner)) {
            1 -> PuntoDeReciclaje.crear(puntos, scanner)
            2 -> PuntoDeReciclaje.verTodos(puntos)
            3 -> VisualizadorHistorialPunto(puntos, scanner).mostrar()
            4 -> VisualizadorEstadisticasPunto(puntos, scanner).mostrar()
            5 -> println("\n↩️  Volviendo al menú principal...")
            else -> println("\n❌ Opción inválida")
        }
    }
}

/**
 * MENÚ DE CONSULTAS - Hereda de MenuBase
 */
class MenuConsultas(
    private val usuarioActual: Usuario?,
    private val puntos: List<PuntoDeReciclaje>,
    scanner: Scanner
) : MenuBase(scanner) {

    override fun obtenerTitulo() = "CONSULTAS E INFORMES 📊"

    override fun mostrar() {
        mostrarEncabezado()
        println("1. Ver mis puntos")
        println("2. Ver mi perfil completo")
        println("3. Ver mi historial de reciclajes")
        println("4. Ver todos los registros del sistema")
        println("5. Ver estadísticas generales")
        println("6. Volver al menú principal")
        print("\n➤ Opción: ")

        when (LectorEntrada.leerEntero(scanner)) {
            1 -> mostrarPuntosUsuario()
            2 -> mostrarPerfilUsuario()
            3 -> mostrarHistorialUsuario()
            4 -> RepositorioRegistros.verTodos()
            5 -> RepositorioRegistros.verEstadisticas()
            6 -> println("\n↩️  Volviendo al menú principal...")
            else -> println("\n❌ Opción inválida")
        }
    }

    private fun mostrarPuntosUsuario() {
        if (usuarioActual != null) {
            println("\n🌟 Tus puntos: ${usuarioActual.puntos}")
        } else {
            println("\n❌ No hay usuario seleccionado")
        }
    }

    private fun mostrarPerfilUsuario() {
        if (usuarioActual != null) {
            usuarioActual.verPerfil()
        } else {
            println("\n❌ No hay usuario seleccionado")
        }
    }

    private fun mostrarHistorialUsuario() {
        if (usuarioActual != null) {
            usuarioActual.verHistorialReciclaje()
        } else {
            println("\n❌ No hay usuario seleccionado")
        }
    }
}

/**
 * REGISTRADOR DE RECICLAJE
 * Responsabilidad: Gestionar el proceso de registro de reciclaje
 */
class RegistradorReciclaje(
    private val usuarioActual: Usuario?,
    private val materiales: List<MaterialReciclable>,
    private val puntos: List<PuntoDeReciclaje>,
    private val scanner: Scanner
) {

    fun registrar() {
        println("\n╔═══════════════════════════════════════════════╗")
        println("║          REGISTRAR RECICLAJE ♻️                ║")
        println("╚═══════════════════════════════════════════════╝")

        val validador = ValidadorPreRegistro()
        if (!validador.validar(usuarioActual, materiales, puntos)) {
            return
        }

        usuarioActual?.reciclar(materiales, puntos, scanner)
    }
}

/**
 * VALIDADOR PRE-REGISTRO
 * Responsabilidad: Validar condiciones antes del registro
 */
class ValidadorPreRegistro {

    fun validar(usuario: Usuario?, materiales: List<MaterialReciclable>, puntos: List<PuntoDeReciclaje>): Boolean {
        if (usuario == null) {
            println("❌ Error: No hay usuario seleccionado")
            println("💡 Ve a 'Gestión de Usuarios' para crear o seleccionar un usuario")
            return false
        }

        if (materiales.isEmpty()) {
            println("❌ Error: No hay materiales disponibles")
            println("💡 Ve a 'Gestión de Materiales' para crear materiales")
            return false
        }

        if (puntos.isEmpty()) {
            println("❌ Error: No hay puntos de reciclaje disponibles")
            println("💡 Ve a 'Gestión de Puntos' para crear puntos de reciclaje")
            return false
        }

        return true
    }
}

/**
 * VISUALIZADOR DE HISTORIAL DE PUNTO
 */
class VisualizadorHistorialPunto(
    private val puntos: List<PuntoDeReciclaje>,
    private val scanner: Scanner
) {

    fun mostrar() {
        if (puntos.isEmpty()) {
            println("\n❌ No hay puntos de reciclaje creados")
            return
        }

        println("\n--- SELECCIONAR PUNTO DE RECICLAJE ---")
        puntos.forEachIndexed { index, punto ->
            println("${index + 1}. ${punto.nombre}")
        }
        print("➤ Selecciona el punto: ")
        val index = LectorEntrada.leerEntero(scanner) - 1
        puntos.getOrNull(index)?.verHistorial()
            ?: println("\n❌ Punto no válido")
    }
}

/**
 * VISUALIZADOR DE ESTADÍSTICAS DE PUNTO
 */
class VisualizadorEstadisticasPunto(
    private val puntos: List<PuntoDeReciclaje>,
    private val scanner: Scanner
) {

    fun mostrar() {
        if (puntos.isEmpty()) {
            println("\n❌ No hay puntos de reciclaje creados")
            return
        }

        println("\n--- ESTADÍSTICAS DE PUNTO DE RECICLAJE ---")
        puntos.forEachIndexed { index, punto ->
            println("${index + 1}. ${punto.nombre}")
        }
        print("➤ Selecciona el punto: ")
        val index = LectorEntrada.leerEntero(scanner) - 1
        val punto = puntos.getOrNull(index)

        if (punto != null) {
            mostrarEstadisticas(punto)
        } else {
            println("\n❌ Punto no válido")
        }
    }

    private fun mostrarEstadisticas(punto: PuntoDeReciclaje) {
        val stats = GestorDeReciclaje.calcularEstadisticasPunto(punto)
        println("\n╔═══════════════════════════════════╗")
        println("  ESTADÍSTICAS: ${punto.nombre}")
        println("╚═══════════════════════════════════╝")
        println("📍 Dirección: ${punto.direccion}")
        println("📦 Materiales aceptados: ${punto.materialesAceptados}")
        println("\n📊 Estadísticas:")
        println("  • Total de reciclajes recibidos: ${stats.totalReciclajes}")
        println("  • Total de kg recibidos: ${"%.2f".format(stats.totalKgRecibidos)} kg")
        println("  • Usuarios únicos: ${stats.usuariosUnicos}")
        println("╚═══════════════════════════════════╝")
    }
}

/**
 * GUARDADOR DE DATOS
 * Responsabilidad: Guardar datos manualmente
 */
class GuardadorDatos(
    private val usuarios: List<Usuario>,
    private val materiales: List<MaterialReciclable>,
    private val puntos: List<PuntoDeReciclaje>
) {

    fun guardar() {
        println("\n💾 Guardando datos...")
        GestorPersistencia.guardarTodo(
            usuarios,
            materiales,
            puntos,
            RepositorioRegistros.obtenerTodos()
        )
    }
}

/**
 * SALIDA DE APLICACIÓN
 * Responsabilidad: Manejar la salida del programa
 */
class SalidaAplicacion(
    private val usuarios: List<Usuario>,
    private val materiales: List<MaterialReciclable>,
    private val puntos: List<PuntoDeReciclaje>,
    private val scanner: Scanner
) {

    fun ejecutar(): Boolean {
        println("\n╔═══════════════════════════════════════════════╗")
        println("║              SALIR DEL SISTEMA                 ║")
        println("╚═══════════════════════════════════════════════╝")

        print("¿Deseas guardar los cambios antes de salir? (S/N): ")
        val respuesta = scanner.nextLine().trim().uppercase()

        if (respuesta == "S" || respuesta == "SI" || respuesta == "Y" || respuesta == "YES") {
            GestorPersistencia.guardarTodo(usuarios, materiales, puntos, RepositorioRegistros.obtenerTodos())
            println("\n💾 Datos guardados exitosamente")
        } else {
            println("\n⚠️  Cambios descartados")
        }

        println("\n╔═══════════════════════════════════════════════╗")
        println("║  ¡Gracias por usar el sistema de reciclaje!   ║")
        println("║              🌍 ♻️ 🌱                          ║")
        println("║     Juntos construimos un mundo mejor         ║")
        println("╚═══════════════════════════════════════════════╝\n")

        scanner.close()
        return false
    }
}

/**
 * LECTOR DE ENTRADA
 * Utilidad para leer entrada del usuario de forma segura
 */
object LectorEntrada {

    fun leerEntero(scanner: Scanner): Int {
        return try {
            val input = scanner.nextLine().trim()
            input.toIntOrNull() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun leerCadena(scanner: Scanner): String {
        return try {
            scanner.nextLine().trim()
        } catch (e: Exception) {
            ""
        }
    }
}

/**
 * SISTEMA DE DATOS
 * Encapsula los datos del sistema
 */
data class SistemaDatos(
    val materiales: MutableList<MaterialReciclable>,
    val puntosReciclaje: MutableList<PuntoDeReciclaje>,
    val usuarios: MutableList<Usuario>
)