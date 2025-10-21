package org.example

import java.util.Scanner

data class PuntoDeReciclaje(
    val nombre: String,
    val direccion: String,
    val materialesAceptados: List<TipoMaterial>
) {
    fun aceptaMaterial(material: TipoMaterial): Boolean {
        return materialesAceptados.contains(material)
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