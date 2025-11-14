package org.example

/**
 * ============================================
 * IMPLEMENTACIÓN DEL PRINCIPIO D
 * ============================================
 * Ahora CalculadoraPuntos implementa la interfaz ICalculadoraPuntos
 */
class CalculadoraPuntos : ICalculadoraPuntos {

    private val factoresPuntos = mapOf(
        TipoMaterial.PLASTICO to 5,
        TipoMaterial.VIDRIO to 3,
        TipoMaterial.PAPEL to 2,
        TipoMaterial.METAL to 4,
        TipoMaterial.ORGANICO to 1
    )

    override fun calcularPuntos(material: MaterialReciclable, cantidad: Double): Int {
        if (cantidad <= 0) {
            return 0
        }

        val factor = factoresPuntos[material.tipo] ?: 1
        return (cantidad * factor).toInt()
    }

    override fun obtenerFactorPuntos(tipo: TipoMaterial): Int {
        return factoresPuntos[tipo] ?: 1
    }

    companion object {
        private val instance: ICalculadoraPuntos = CalculadoraPuntos()

        fun getInstance(): ICalculadoraPuntos = instance

        // Métodos de compatibilidad para código existente
        fun calcularPuntos(material: MaterialReciclable, cantidad: Double): Int {
            return instance.calcularPuntos(material, cantidad)
        }

        fun obtenerFactorPuntos(tipo: TipoMaterial): Int {
            return instance.obtenerFactorPuntos(tipo)
        }

        fun mostrarTablaPuntos() {
            println("\n╔════════════════════════════════════════╗")
            println("║    TABLA DE PUNTOS POR MATERIAL       ║")
            println("╚════════════════════════════════════════╝")
            println("\nTipo de Material    | Puntos por kg")
            println("--------------------|---------------")
            TipoMaterial.values().forEach { tipo ->
                val puntos = instance.obtenerFactorPuntos(tipo)
                println("${tipo.name.padEnd(19)} | $puntos")
            }
            println()
        }
    }
}