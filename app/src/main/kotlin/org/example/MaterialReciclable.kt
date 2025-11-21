package org.example

import java.util.Scanner

/**
 * CLASE BASE ABSTRACTA - MATERIAL RECICLABLE
 * Implementa HERENCIA y POLIMORFISMO
 * Cumple con Open-Closed: Abierta a extensión, cerrada a modificación
 */
abstract class MaterialReciclableBase(
    open val nombre: String,
    open val descripcion: String,
    open val tipo: TipoMaterial,
    open val pesoKg: Double
) {
    /**
     * Método abstracto que cada subclase debe implementar (POLIMORFISMO)
     */
    abstract fun calcularImpactoAmbiental(): Double

    /**
     * Método que puede ser sobrescrito (POLIMORFISMO)
     */
    open fun obtenerCategoria(): String {
        return when {
            pesoKg < 0.5 -> "Ligero"
            pesoKg < 2.0 -> "Medio"
            else -> "Pesado"
        }
    }

    /**
     * Método template (puede ser usado por todas las subclases)
     */
    fun calcularPuntos(): Int {
        return CalculadoraPuntos.calcularPuntos(this.toMaterialReciclable(), pesoKg)
    }

    /**
     * Método de conversión para compatibilidad
     */
    fun toMaterialReciclable(): MaterialReciclable {
        return MaterialReciclable(nombre, descripcion, tipo, pesoKg)
    }
}

/**
 * SUBCLASE: Material de Plástico
 * Demuestra HERENCIA y SOBRESCRITURA (POLIMORFISMO)
 */
class MaterialPlastico(
    override val nombre: String,
    override val descripcion: String,
    override val pesoKg: Double
) : MaterialReciclableBase(nombre, descripcion, TipoMaterial.PLASTICO, pesoKg) {

    override fun calcularImpactoAmbiental(): Double {
        // Plástico: mayor impacto, tarda 100-500 años en degradarse
        return pesoKg * 450.0 // Factor de degradación promedio
    }

    override fun obtenerCategoria(): String {
        return "Plástico - ${super.obtenerCategoria()}"
    }
}

/**
 * SUBCLASE: Material de Vidrio
 */
class MaterialVidrio(
    override val nombre: String,
    override val descripcion: String,
    override val pesoKg: Double
) : MaterialReciclableBase(nombre, descripcion, TipoMaterial.VIDRIO, pesoKg) {

    override fun calcularImpactoAmbiental(): Double {
        // Vidrio: tarda 4000 años en degradarse
        return pesoKg * 4000.0
    }

    override fun obtenerCategoria(): String {
        return "Vidrio - ${super.obtenerCategoria()}"
    }
}

/**
 * SUBCLASE: Material de Papel
 */
class MaterialPapel(
    override val nombre: String,
    override val descripcion: String,
    override val pesoKg: Double
) : MaterialReciclableBase(nombre, descripcion, TipoMaterial.PAPEL, pesoKg) {

    override fun calcularImpactoAmbiental(): Double {
        // Papel: se degrada en 2-6 meses
        return pesoKg * 4.0 // Meses promedio
    }

    override fun obtenerCategoria(): String {
        return "Papel - ${super.obtenerCategoria()}"
    }
}

/**
 * SUBCLASE: Material de Metal
 */
class MaterialMetal(
    override val nombre: String,
    override val descripcion: String,
    override val pesoKg: Double
) : MaterialReciclableBase(nombre, descripcion, TipoMaterial.METAL, pesoKg) {

    override fun calcularImpactoAmbiental(): Double {
        // Metal: tarda 10-100 años en degradarse
        return pesoKg * 50.0
    }

    override fun obtenerCategoria(): String {
        return "Metal - ${super.obtenerCategoria()}"
    }
}

/**
 * SUBCLASE: Material Orgánico
 */
class MaterialOrganico(
    override val nombre: String,
    override val descripcion: String,
    override val pesoKg: Double
) : MaterialReciclableBase(nombre, descripcion, TipoMaterial.ORGANICO, pesoKg) {

    override fun calcularImpactoAmbiental(): Double {
        // Orgánico: se degrada en semanas
        return pesoKg * 0.5 // Semanas
    }

    override fun obtenerCategoria(): String {
        return "Orgánico - ${super.obtenerCategoria()}"
    }
}

/**
 * FACTORY PATTERN + POLIMORFISMO
 * Crea instancias específicas según el tipo
 */
class MaterialFactory {
    companion object {
        fun crear(nombre: String, descripcion: String, tipo: TipoMaterial, peso: Double): MaterialReciclableBase {
            return when (tipo) {
                TipoMaterial.PLASTICO -> MaterialPlastico(nombre, descripcion, peso)
                TipoMaterial.VIDRIO -> MaterialVidrio(nombre, descripcion, peso)
                TipoMaterial.PAPEL -> MaterialPapel(nombre, descripcion, peso)
                TipoMaterial.METAL -> MaterialMetal(nombre, descripcion, peso)
                TipoMaterial.ORGANICO -> MaterialOrganico(nombre, descripcion, peso)
            }
        }
    }
}

/**
 * CLASE DE DATOS ORIGINAL (para compatibilidad)
 * Mantiene la estructura existente
 */
data class MaterialReciclable(
    val nombre: String,
    val descripcion: String,
    val tipo: TipoMaterial,
    val pesoKg: Double
) {
    /**
     * Convierte a MaterialReciclableBase (con polimorfismo)
     */
    fun toMaterialBase(): MaterialReciclableBase {
        return MaterialFactory.crear(nombre, descripcion, tipo, pesoKg)
    }

    fun calcularPuntos(): Int {
        return CalculadoraPuntos.calcularPuntos(this, pesoKg)
    }

    companion object {
        fun crear(materiales: MutableList<MaterialReciclable>, scanner: Scanner) {
            val creador = CreadorMaterial(materiales, scanner)
            creador.crear()
        }

        fun verTodos(materiales: List<MaterialReciclable>) {
            val visualizador = VisualizadorMateriales(materiales)
            visualizador.mostrar()
        }
    }
}

/**
 * CREADOR DE MATERIAL
 * Responsabilidad única: Crear nuevos materiales
 */
class CreadorMaterial(
    private val materiales: MutableList<MaterialReciclable>,
    private val scanner: Scanner
) {

    fun crear() {
        println("\n--- CREAR MATERIAL RECICLABLE ---")

        print("Nombre: ")
        val nombre = scanner.nextLine()

        print("Descripción: ")
        val descripcion = scanner.nextLine()

        val tipo = seleccionarTipo()
        val peso = solicitarPeso()

        val material = MaterialReciclable(nombre, descripcion, tipo, peso)
        materiales.add(material)

        mostrarResumen(material)
    }

    private fun seleccionarTipo(): TipoMaterial {
        println("\nSelecciona el tipo de material:")
        TipoMaterial.values().forEachIndexed { index, tipo ->
            val factor = CalculadoraPuntos.obtenerFactorPuntos(tipo)
            println("${index + 1}. $tipo ($factor puntos/kg)")
        }
        print("Opción: ")
        val opcion = (scanner.nextLine().toIntOrNull() ?: 1) - 1
        return TipoMaterial.values().getOrNull(opcion) ?: TipoMaterial.PLASTICO
    }

    private fun solicitarPeso(): Double {
        print("Peso (kg): ")
        return scanner.nextLine().toDoubleOrNull() ?: 0.5
    }

    private fun mostrarResumen(material: MaterialReciclable) {
        println("\n✅ Material creado exitosamente:")
        println(material)
        println("💰 Puntos potenciales: ${material.calcularPuntos()}")

        // Demostrar POLIMORFISMO
        val materialBase = material.toMaterialBase()
        println("🌍 Impacto ambiental: ${materialBase.calcularImpactoAmbiental()} (escala de degradación)")
        println("📊 Categoría: ${materialBase.obtenerCategoria()}")
    }
}

/**
 * VISUALIZADOR DE MATERIALES
 * Responsabilidad única: Mostrar lista de materiales
 */
class VisualizadorMateriales(private val materiales: List<MaterialReciclable>) {

    fun mostrar() {
        println("\n--- MATERIALES REGISTRADOS ---")
        if (materiales.isEmpty()) {
            println("No hay materiales aún.")
        } else {
            materiales.forEach { mostrarMaterial(it) }
        }
    }

    private fun mostrarMaterial(material: MaterialReciclable) {
        println("${material.nombre} - ${material.tipo} (${material.descripcion}) - ${material.pesoKg} kg")
        println("  💰 Puntos potenciales: ${material.calcularPuntos()}")

        // Demostrar POLIMORFISMO
        val materialBase = material.toMaterialBase()
        println("  🌍 Impacto ambiental: ${materialBase.calcularImpactoAmbiental()}")
        println("  📊 ${materialBase.obtenerCategoria()}")
    }
}