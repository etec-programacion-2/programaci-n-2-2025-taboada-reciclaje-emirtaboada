package org.example

/**
 * ============================================
 * IMPLEMENTACIÓN DEL REPOSITORIO DE REGISTROS
 * ============================================
 * Implementa IRepositorioRegistros
 */
class RepositorioRegistrosImpl : IRepositorioRegistros {
    private val registros = mutableListOf<RegistroReciclaje>()

    override fun agregar(registro: RegistroReciclaje) {
        registros.add(registro)
    }

    override fun obtenerTodos(): List<RegistroReciclaje> {
        return registros.toList()
    }

    override fun obtenerPorUsuario(usuario: Usuario): List<RegistroReciclaje> {
        return registros.filter { it.usuario.email == usuario.email }
    }

    override fun obtenerPorPunto(punto: PuntoDeReciclaje): List<RegistroReciclaje> {
        return registros.filter { it.puntoDeReciclaje.nombre == punto.nombre }
    }

    override fun limpiar() {
        registros.clear()
    }

    fun verTodos() {
        println("\n╔═══════════════════════════════════════════════╗")
        println("║      TODOS LOS REGISTROS DEL SISTEMA         ║")
        println("╚═══════════════════════════════════════════════╝")

        if (registros.isEmpty()) {
            println("No hay registros en el sistema.")
        } else {
            println("Total de registros: ${registros.size}\n")
            registros.forEachIndexed { index, registro ->
                println("\n#${index + 1}")
                registro.mostrar()
            }
        }
    }

    fun verEstadisticas() {
        println("\n╔═══════════════════════════════════════════════╗")
        println("║         ESTADÍSTICAS GENERALES               ║")
        println("╚═══════════════════════════════════════════════╝")

        if (registros.isEmpty()) {
            println("No hay datos para mostrar estadísticas.")
            return
        }

        val totalRegistros = registros.size
        val totalKg = registros.sumOf { it.cantidad }
        val usuariosUnicos = registros.map { it.usuario.email }.distinct().size
        val puntosUnicos = registros.map { it.puntoDeReciclaje.nombre }.distinct().size

        println("\n📊 Resumen General:")
        println("  • Total de reciclajes: $totalRegistros")
        println("  • Total de kg reciclados: ${"%.2f".format(totalKg)} kg")
        println("  • Usuarios activos: $usuariosUnicos")
        println("  • Puntos de reciclaje activos: $puntosUnicos")

        println("\n📦 Por tipo de material:")
        val porTipo = registros.groupBy { it.material.tipo }
        porTipo.forEach { (tipo, regs) ->
            val kg = regs.sumOf { it.cantidad }
            println("  • $tipo: ${"%.2f".format(kg)} kg (${regs.size} reciclajes)")
        }
    }
}

/**
 * Singleton para mantener compatibilidad
 */
object RepositorioRegistros {
    private val instance: IRepositorioRegistros = RepositorioRegistrosImpl()
    private val impl = instance as RepositorioRegistrosImpl

    fun getInstance(): IRepositorioRegistros = instance

    fun agregar(registro: RegistroReciclaje) = instance.agregar(registro)
    fun obtenerTodos() = instance.obtenerTodos()
    fun obtenerPorUsuario(usuario: Usuario) = instance.obtenerPorUsuario(usuario)
    fun obtenerPorPunto(punto: PuntoDeReciclaje) = instance.obtenerPorPunto(punto)
    fun limpiar() = instance.limpiar()
    fun verTodos() = impl.verTodos()
    fun verEstadisticas() = impl.verEstadisticas()
}