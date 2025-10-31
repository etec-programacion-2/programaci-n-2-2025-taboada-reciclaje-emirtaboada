package org.example

/**
 * Gestor centralizado para las operaciones de reciclaje.
 * Implementa el patrón Facade para simplificar la interacción entre múltiples componentes.
 * 
 * Este gestor orquesta la colaboración entre Usuario, MaterialReciclable, 
 * PuntoDeReciclaje, CalculadoraPuntos y RepositorioRegistros.
 * 
 * Responsabilidades:
 * - Coordinar el flujo completo de una operación de reciclaje
 * - Validar que las operaciones sean posibles
 * - Calcular y asignar puntos
 * - Registrar las transacciones
 * - Proveer estadísticas centralizadas
 */
object GestorDeReciclaje {
    
    /**
     * Registra una operación completa de reciclaje.
     * 
     * @param usuario El usuario que realiza el reciclaje
     * @param material El material a reciclar
     * @param punto El punto de reciclaje donde se entrega el material
     * @param cantidad La cantidad en kilogramos del material a reciclar
     * @return ResultadoReciclaje con el resultado de la operación
     */
    fun registrarReciclaje(
        usuario: Usuario,
        material: MaterialReciclable,
        punto: PuntoDeReciclaje,
        cantidad: Double
    ): ResultadoReciclaje {
        // Validar cantidad
        if (cantidad <= 0) {
            return ResultadoReciclaje(
                exitoso = false,
                mensaje = "La cantidad debe ser mayor a 0",
                puntosGanados = 0
            )
        }
        
        // Validar que el punto acepte el material
        if (!punto.aceptaMaterial(material.tipo)) {
            return ResultadoReciclaje(
                exitoso = false,
                mensaje = "El punto '${punto.nombre}' no acepta ${material.tipo}. " +
                         "Materiales aceptados: ${punto.materialesAceptados}",
                puntosGanados = 0
            )
        }
        
        // Intentar recibir el material en el punto
        val materialRecibido = punto.recibirMaterial(material, cantidad)
        
        if (!materialRecibido) {
            return ResultadoReciclaje(
                exitoso = false,
                mensaje = "Error al procesar el material en el punto de reciclaje",
                puntosGanados = 0
            )
        }
        
        // Calcular puntos ganados
        val puntosGanados = CalculadoraPuntos.calcularPuntos(material, cantidad)
        
        // Asignar puntos al usuario
        usuario.sumarPuntos(puntosGanados)
        
        // Crear y registrar el registro de reciclaje
        val registro = RegistroReciclaje(
            usuario = usuario,
            material = material,
            puntoDeReciclaje = punto,
            cantidad = cantidad
        )
        RepositorioRegistros.agregar(registro)
        
        return ResultadoReciclaje(
            exitoso = true,
            mensaje = "${usuario.nombre} recicló ${"%.2f".format(cantidad)} kg de '${material.nombre}' " +
                     "en '${punto.nombre}'",
            puntosGanados = puntosGanados,
            registro = registro
        )
    }
    
    /**
     * Obtiene el historial de reciclajes de un usuario específico
     */
    fun obtenerHistorialUsuario(usuario: Usuario): List<RegistroReciclaje> {
        return RepositorioRegistros.obtenerPorUsuario(usuario)
    }
    
    /**
     * Obtiene el historial de un punto de reciclaje específico
     */
    fun obtenerHistorialPunto(punto: PuntoDeReciclaje): List<RegistroReciclaje> {
        return RepositorioRegistros.obtenerPorPunto(punto)
    }
    
    /**
     * Calcula estadísticas para un usuario
     */
    fun calcularEstadisticasUsuario(usuario: Usuario): EstadisticasUsuario {
        val registros = obtenerHistorialUsuario(usuario)
        val totalReciclajes = registros.size
        val totalKg = registros.sumOf { it.cantidad }
        val materialesPorTipo = registros.groupBy { it.material.tipo }
            .mapValues { (_, regs) -> regs.sumOf { it.cantidad } }
        
        return EstadisticasUsuario(
            usuario = usuario,
            totalReciclajes = totalReciclajes,
            totalKgReciclados = totalKg,
            materialesPorTipo = materialesPorTipo
        )
    }
    
    /**
     * Calcula estadísticas para un punto de reciclaje
     */
    fun calcularEstadisticasPunto(punto: PuntoDeReciclaje): EstadisticasPunto {
        val registros = obtenerHistorialPunto(punto)
        val totalReciclajes = registros.size
        val totalKg = registros.sumOf { it.cantidad }
        val usuariosUnicos = registros.map { it.usuario }.distinct().size
        
        return EstadisticasPunto(
            punto = punto,
            totalReciclajes = totalReciclajes,
            totalKgRecibidos = totalKg,
            usuariosUnicos = usuariosUnicos
        )
    }
    
    /**
     * Valida si un reciclaje es posible antes de ejecutarlo
     */
    fun validarReciclaje(
        material: MaterialReciclable,
        punto: PuntoDeReciclaje,
        cantidad: Double
    ): ValidacionReciclaje {
        val errores = mutableListOf<String>()
        
        if (cantidad <= 0) {
            errores.add("La cantidad debe ser mayor a 0")
        }
        
        if (!punto.aceptaMaterial(material.tipo)) {
            errores.add("El punto no acepta este tipo de material (${material.tipo})")
        }
        
        return ValidacionReciclaje(
            valido = errores.isEmpty(),
            errores = errores
        )
    }
}

/**
 * Representa el resultado de una operación de reciclaje
 */
data class ResultadoReciclaje(
    val exitoso: Boolean,
    val mensaje: String,
    val puntosGanados: Int,
    val registro: RegistroReciclaje? = null
)

/**
 * Estadísticas de un usuario
 */
data class EstadisticasUsuario(
    val usuario: Usuario,
    val totalReciclajes: Int,
    val totalKgReciclados: Double,
    val materialesPorTipo: Map<TipoMaterial, Double>
)

/**
 * Estadísticas de un punto de reciclaje
 */
data class EstadisticasPunto(
    val punto: PuntoDeReciclaje,
    val totalReciclajes: Int,
    val totalKgRecibidos: Double,
    val usuariosUnicos: Int
)

/**
 * Resultado de validación de un reciclaje
 */
data class ValidacionReciclaje(
    val valido: Boolean,
    val errores: List<String>
)