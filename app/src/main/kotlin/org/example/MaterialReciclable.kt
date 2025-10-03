package org.example

data class MaterialReciclable(
    val nombre: String,
    val descripcion: String,
    val tipo: TipoMaterial
) {
    fun calcularPuntos(): Int {
        return when (tipo) {
            TipoMaterial.PLASTICO -> 10
            TipoMaterial.VIDRIO -> 15
            TipoMaterial.PAPEL -> 8
            TipoMaterial.METAL -> 20
            TipoMaterial.ORGANICO -> 5
        }
    }

    companion object {
        fun crear(materiales: MutableList<MaterialReciclable>) {
            println("\n--- CREAR MATERIAL RECICLABLE ---")
            
            print("Nombre del material: ")
            val nombre = readLine() ?: ""
            
            print("Descripción: ")
            val descripcion = readLine() ?: ""
            
            println("\nTipos de material disponibles:")
            TipoMaterial.values().forEachIndexed { index, tipo ->
                println("${index + 1}. $tipo")
            }
            print("Selecciona el tipo (1-${TipoMaterial.values().size}): ")
            val tipoIndex = (readLine()?.toIntOrNull() ?: 1) - 1
            
            val tipo = TipoMaterial.values().getOrNull(tipoIndex) ?: TipoMaterial.PLASTICO
            
            val material = MaterialReciclable(nombre, descripcion, tipo)
            materiales.add(material)
            
            println("\n✅ Material creado exitosamente:")
            println(material)
        }

        fun verTodos(materiales: List<MaterialReciclable>) {
            println("\n--- MATERIALES RECICLABLES ---")
            if (materiales.isEmpty()) {
                println("No hay materiales creados aún.")
            } else {
                materiales.forEach { println(it) }
            }
        }
    }
}
