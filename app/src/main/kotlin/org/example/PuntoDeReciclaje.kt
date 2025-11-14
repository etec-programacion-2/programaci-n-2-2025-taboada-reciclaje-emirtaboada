package org.example

import java.util.Scanner

data class PuntoDeReciclaje(
    val nombre: String,
    val direccion: String,
    val materialesAceptados: List<TipoMaterial>
) {
    // Registro de materiales recibidos: Material -> Cantidad total en kg
    private val materialesRecibidos = mutableMapOf<String, Double>()
    
    fun aceptaMaterial(material: TipoMaterial): Boolean {
        return materialesAceptados.contains(material)
    }
    
    /**
     * Recibe un material reciclable en este punto de reciclaje
     * @param material El material a recibir
     * @param cantidad La cantidad en kilogramos
     * @return true si el material fue aceptado, false si no
     */
    fun recibirMaterial(material: MaterialReciclable, cantidad: Double): Boolean {
        // Verificar si el punto acepta este tipo de material
        if (!aceptaMaterial(material.tipo)) {
            return false
        }
        
        // Validar que la cantidad sea positiva
        if (cantidad <= 0) {
            return false
        }
        
        // Registrar el material recibido
        val clave = "${material.nombre} (${material.tipo})"
        val cantidadActual = materialesRecibidos.getOrDefault(clave, 0.0)
        materialesRecibidos[clave] = cantidadActual + cantidad
        
        return true
    }
    
    /**
     * Muestra el historial de materiales recibidos en este punto
     */
    fun verHistorial() {
        println("\n--- HISTORIAL DE MATERIALES RECIBIDOS ---")
        println("Punto: $nombre")
        
        if (materialesRecibidos.isEmpty()) {
            println("No se han recibido materiales aún.")
        } else {
            println("Materiales recibidos:")
            materialesRecibidos.forEach { (material, cantidad) ->
                println("  • $material: ${"%.2f".format(cantidad)} kg")
            }
            
            val totalKg = materialesRecibidos.values.sum()
            println("\nTotal recibido: ${"%.2f".format(totalKg)} kg")
        }
    }
    
    /**
     * Obtiene la cantidad total de un material específico recibido
     */
    fun obtenerCantidadRecibida(nombreMaterial: String, tipo: TipoMaterial): Double {
        val clave = "$nombreMaterial ($tipo)"
        return materialesRecibidos.getOrDefault(clave, 0.0)
    }

    companion object {
        fun crear(puntos: MutableList<PuntoDeReciclaje>, scanner: Scanner) {
            println("\n--- CREAR PUNTO DE RECICLAJE ---")
            
            print("Nombre del punto: ")
            val nombre = scanner.nextLine()
            
            print("Dirección: ")
            val direccion = scanner.nextLine()
            
            println("\nSelecciona los materiales aceptados (separados por coma, ej: 1,2,3):")
            TipoMaterial.values().forEachIndexed { index, tipo ->
                println("${index + 1}. $tipo")
            }
            print("Selección: ")
            val seleccion = scanner.nextLine()
            
            val materialesAceptados = seleccion.split(",")
                .mapNotNull { it.trim().toIntOrNull() }
                .mapNotNull { TipoMaterial.values().getOrNull(it - 1) }
            
            if (materialesAceptados.isEmpty()) {
                println("\n❌ No se seleccionaron materiales válidos")
                return
            }
            
            val punto = PuntoDeReciclaje(nombre, direccion, materialesAceptados)
            puntos.add(punto)
            
            println("\n✅ Punto de reciclaje creado exitosamente:")
            println(punto)
        }

        fun verTodos(puntos: List<PuntoDeReciclaje>) {
            println("\n--- PUNTOS DE RECICLAJE ---")
            if (puntos.isEmpty()) {
                println("No hay puntos de reciclaje creados aún.")
            } else {
                puntos.forEach { punto ->
                    println(punto)
                    println("  Materiales aceptados:")
                    punto.materialesAceptados.forEach { tipo ->
                        println("  - $tipo")
                    }
                    println()
                }
            }
        }
    }
}