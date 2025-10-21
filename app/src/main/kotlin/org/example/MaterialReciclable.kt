package org.example

import java.util.Scanner

data class MaterialReciclable(
    val nombre: String,
    val descripcion: String,
    val tipo: TipoMaterial,
    val pesoKg: Double
) {
    fun calcularPuntos(): Int {
        return when (tipo) {
            TipoMaterial.PLASTICO -> (pesoKg * 5).toInt()
            TipoMaterial.VIDRIO -> (pesoKg * 3).toInt()
            TipoMaterial.PAPEL -> (pesoKg * 2).toInt()
            TipoMaterial.METAL -> (pesoKg * 4).toInt()
            TipoMaterial.ORGANICO -> (pesoKg * 1).toInt()
        }
    }

    companion object {
        fun crear(materiales: MutableList<MaterialReciclable>, scanner: Scanner) {
            println("\n--- CREAR MATERIAL RECICLABLE ---")
            
            print("Nombre: ")
            val nombre = scanner.nextLine()
            
            print("Descripción: ")
            val descripcion = scanner.nextLine()
            
            println("\nSelecciona el tipo de material:")
            TipoMaterial.values().forEachIndexed { index, tipo ->
                println("${index + 1}. $tipo")
            }
            print("Opción: ")
            val tipoSeleccionado = TipoMaterial.values()
                .getOrNull((scanner.nextLine().toIntOrNull() ?: 1) - 1) ?: TipoMaterial.PLASTICO

            print("Peso (kg): ")
            val peso = scanner.nextLine().toDoubleOrNull() ?: 0.5

            val material = MaterialReciclable(nombre, descripcion, tipoSeleccionado, peso)
            materiales.add(material)
            
            println("\n✅ Material creado exitosamente:")
            println(material)
        }

        fun verTodos(materiales: List<MaterialReciclable>) {
            println("\n--- MATERIALES REGISTRADOS ---")
            if (materiales.isEmpty()) {
                println("No hay materiales aún.")
            } else {
                materiales.forEach {
                    println("${it.nombre} - ${it.tipo} (${it.descripcion}) - ${it.pesoKg} kg")
                }
            }
        }
    }
}