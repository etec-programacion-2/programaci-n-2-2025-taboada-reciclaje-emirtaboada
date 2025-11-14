package org.example

object CalculadoraPuntos {
    

    private val factoresPuntos = mapOf(
        TipoMaterial.PLASTICO to 5,
        TipoMaterial.VIDRIO to 3,
        TipoMaterial.PAPEL to 2,
        TipoMaterial.METAL to 4,
        TipoMaterial.ORGANICO to 1
    )
    

    fun calcularPuntos(material: MaterialReciclable, cantidad: Double): Int {
        if (cantidad <= 0) {
            return 0
        }
        
        val factor = factoresPuntos[material.tipo] ?: 1
        return (cantidad * factor).toInt()
    }

     
    fun obtenerFactorPuntos(tipo: TipoMaterial): Int {
        return factoresPuntos[tipo] ?: 1
    }
    

    fun mostrarTablaPuntos() {
        println("\n--- TABLA DE PUNTOS POR MATERIAL ---")
        println("Tipo de Material    | Puntos por kg")
        println("--------------------|--------------")
        factoresPuntos.forEach { (tipo, puntos) ->
            println("${tipo.name.padEnd(19)} | $puntos")
        }
        println()
    }
}