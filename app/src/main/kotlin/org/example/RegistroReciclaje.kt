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

/**
 * Repositorio en memoria para almacenar los registros de reciclaje
 */
object RepositorioRegistros {
    private val registros = mutableListOf<RegistroReciclaje>()
    
    /**
     * Agrega un nuevo registro al repositorio
     */
    fun agregar(registro: RegistroReciclaje) {
        registros.add(registro)
    }
    
    /**
     * Obtiene todos los registros
     */
    fun obtenerTodos(): List<RegistroReciclaje> {
        return registros.toList()
    }
    
    /**
     * Obtiene los registros de un usuario específico
     */
    fun obtenerPorUsuario(usuario: Usuario): List<RegistroReciclaje> {
        return registros.filter { it.usuario == usuario }
    }
    
    /**
     * Obtiene los registros de un punto de reciclaje específico
     */
    fun obtenerPorPunto(punto: PuntoDeReciclaje): List<RegistroReciclaje> {
        return registros.filter { it.puntoDeReciclaje == punto }
    }
    
    /**
     * Obtiene los registros de un tipo de material específico
     */
    fun obtenerPorTipoMaterial(tipo: TipoMaterial): List<RegistroReciclaje> {
        return registros.filter { it.material.tipo == tipo }
    }
    
    /**
     * Calcula el total de kg reciclados
     */
    fun totalKgReciclados(): Double {
        return registros.sumOf { it.cantidad }
    }
    
    /**
     * Calcula el total de puntos otorgados
     */
    fun totalPuntosOtorgados(): Int {
        return registros.sumOf { 
            CalculadoraPuntos.calcularPuntos(it.material, it.cantidad) 
        }
    }
    
    /**
     * Muestra todos los registros
     */
    fun verTodos() {
        println("\n═══════════════════════════════════════")
        println("       HISTORIAL DE RECICLAJES")
        println("═══════════════════════════════════════")
        
        if (registros.isEmpty()) {
            println("No hay registros de reciclaje aún.")
        } else {
            println("Total de registros: ${registros.size}")
            println("Total reciclado: ${"%.2f".format(totalKgReciclados())} kg")
            println("Total puntos: ${totalPuntosOtorgados()}")
            println("\n--- Registros ---")
            registros.forEachIndexed { index, registro ->
                println("\n#${index + 1}")
                registro.mostrar()
            }
        }
        println("═══════════════════════════════════════")
    }
    
    /**
     * Muestra estadísticas generales
     */
    fun verEstadisticas() {
        println("\n═══════════════════════════════════════")
        println("          ESTADÍSTICAS GENERALES")
        println("═══════════════════════════════════════")
        
        if (registros.isEmpty()) {
            println("No hay datos suficientes para mostrar estadísticas.")
            return
        }
        
        println("📊 Total de reciclajes: ${registros.size}")
        println("⚖️  Total reciclado: ${"%.2f".format(totalKgReciclados())} kg")
        println("🌟 Total puntos otorgados: ${totalPuntosOtorgados()}")
        
        println("\n📦 Por tipo de material:")
        TipoMaterial.values().forEach { tipo ->
            val registrosTipo = obtenerPorTipoMaterial(tipo)
            if (registrosTipo.isNotEmpty()) {
                val totalKg = registrosTipo.sumOf { it.cantidad }
                println("  • $tipo: ${"%.2f".format(totalKg)} kg (${registrosTipo.size} reciclajes)")
            }
        }
        
        println("\n👥 Usuarios más activos:")
        val usuariosActivos = registros.groupBy { it.usuario }
            .map { (usuario, regs) -> usuario to regs.size }
            .sortedByDescending { it.second }
            .take(3)
        
        usuariosActivos.forEachIndexed { index, (usuario, count) ->
            println("  ${index + 1}. ${usuario.nombre}: $count reciclajes (${usuario.puntos} puntos)")
        }
        
        println("═══════════════════════════════════════")
    }
    
    /**
     * Limpia todos los registros (útil para testing)
     */
    fun limpiar() {
        registros.clear()
    }
}