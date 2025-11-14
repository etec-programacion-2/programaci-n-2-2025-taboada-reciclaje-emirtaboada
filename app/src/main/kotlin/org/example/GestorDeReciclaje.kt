package org.example

/**
 * ============================================
 * PRINCIPIO D - DEPENDENCY INVERSION
 * ============================================
 * GestorDeReciclaje ahora:
 * 1. Depende de abstracciones (interfaces) NO de clases concretas
 * 2. Las dependencias se INYECTAN por constructor
 * 3. Implementa interfaces específicas (Principio I)
 */
class GestorDeReciclajeImpl(
    private val calculadora: ICalculadoraPuntos,
    private val repositorio: IRepositorioRegistros
) : IGestorReciclaje, IValidadorReciclaje, ICalculadorEstadisticas {

    /**
     * Registra una operación completa de reciclaje.
     * Usa las dependencias INYECTADAS en lugar de llamar a objetos globales.
     */
    override fun registrarReciclaje(
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

        // ✅ Usa la interfaz inyectada (NO clase concreta)
        val puntosGanados = calculadora.calcularPuntos(material, cantidad)

        // Asignar puntos al usuario
        usuario.sumarPuntos(puntosGanados)

        // Crear y registrar el registro de reciclaje
        val registro = RegistroReciclaje(
            usuario = usuario,
            material = material,
            puntoDeReciclaje = punto,
            cantidad = cantidad
        )

        // ✅ Usa la interfaz inyectada (NO clase concreta)
        repositorio.agregar(registro)

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
        return repositorio.obtenerPorUsuario(usuario)
    }

    /**
     * Obtiene el historial de un punto de reciclaje específico
     */
    fun obtenerHistorialPunto(punto: PuntoDeReciclaje): List<RegistroReciclaje> {
        return repositorio.obtenerPorPunto(punto)
    }

    /**
     * Calcula estadísticas para un usuario
     */
    override fun calcularEstadisticasUsuario(usuario: Usuario): EstadisticasUsuario {
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
    override fun calcularEstadisticasPunto(punto: PuntoDeReciclaje): EstadisticasPunto {
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
    override fun validar(
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
 * Object singleton para mantener compatibilidad con código existente
 */
object GestorDeReciclaje {
    // ✅ Inyección de dependencias: usa interfaces, no implementaciones
    private val instance: GestorDeReciclajeImpl = GestorDeReciclajeImpl(
        calculadora = CalculadoraPuntos.getInstance(),
        repositorio = RepositorioRegistros.getInstance()
    )

    fun registrarReciclaje(
        usuario: Usuario,
        material: MaterialReciclable,
        punto: PuntoDeReciclaje,
        cantidad: Double
    ): ResultadoReciclaje {
        return instance.registrarReciclaje(usuario, material, punto, cantidad)
    }

    fun obtenerHistorialUsuario(usuario: Usuario): List<RegistroReciclaje> {
        return instance.obtenerHistorialUsuario(usuario)
    }

    fun obtenerHistorialPunto(punto: PuntoDeReciclaje): List<RegistroReciclaje> {
        return instance.obtenerHistorialPunto(punto)
    }

    fun calcularEstadisticasUsuario(usuario: Usuario): EstadisticasUsuario {
        return instance.calcularEstadisticasUsuario(usuario)
    }

    fun calcularEstadisticasPunto(punto: PuntoDeReciclaje): EstadisticasPunto {
        return instance.calcularEstadisticasPunto(punto)
    }

    fun validarReciclaje(
        material: MaterialReciclable,
        punto: PuntoDeReciclaje,
        cantidad: Double
    ): ValidacionReciclaje {
        return instance.validar(material, punto, cantidad)
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