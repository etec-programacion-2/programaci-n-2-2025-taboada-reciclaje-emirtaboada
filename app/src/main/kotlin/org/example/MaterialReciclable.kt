package org.example

import java.util.Scanner

data class MaterialReciclable(
    val nombre: String,
    val descripcion: String,
    val tipo: TipoMaterial,
    val pesoKg: Double
) {
    /**
     * Calcula los puntos que otorgaría reciclar este material.
     * Delega el cálculo a CalculadoraPuntos.
     */
    fun calcularPuntos(): Int {
        return CalculadoraPuntos.calcularPuntos(this, pesoKg)
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
                val factor = CalculadoraPuntos.obtenerFactorPuntos(tipo)
                println("${index + 1}. $tipo ($factor puntos/kg)")
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
            println("💰 Puntos potenciales: ${material.calcularPuntos()}")
        }

        fun verTodos(materiales: List<MaterialReciclable>) {
            println("\n--- MATERIALES REGISTRADOS ---")
            if (materiales.isEmpty()) {
                println("No hay materiales aún.")
            } else {
                materiales.forEach {
                    println("${it.nombre} - ${it.tipo} (${it.descripcion}) - ${it.pesoKg} kg")
                    println("  💰 Puntos potenciales: ${it.calcularPuntos()}")
                }
            }
        }
    }
}