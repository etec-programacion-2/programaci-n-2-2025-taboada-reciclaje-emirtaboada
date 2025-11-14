package org.example

/**
 * Interfaz para calcular puntos de reciclaje
 */
interface ICalculadoraPuntos {
    fun calcularPuntos(material: MaterialReciclable, cantidad: Double): Int
    fun obtenerFactorPuntos(tipo: TipoMaterial): Int
}

/**
 * Interfaz para gestionar el repositorio de registros
 */
interface IRepositorioRegistros {
    fun agregar(registro: RegistroReciclaje)
    fun obtenerTodos(): List<RegistroReciclaje>
    fun obtenerPorUsuario(usuario: Usuario): List<RegistroReciclaje>
    fun obtenerPorPunto(punto: PuntoDeReciclaje): List<RegistroReciclaje>
    fun limpiar()
}

/**
 * Interfaz para gestionar operaciones de reciclaje
 */
interface IGestorReciclaje {
    fun registrarReciclaje(
        usuario: Usuario,
        material: MaterialReciclable,
        punto: PuntoDeReciclaje,
        cantidad: Double
    ): ResultadoReciclaje
}

/**
 * Interfaz para validar operaciones de reciclaje
 */
interface IValidadorReciclaje {
    fun validar(
        material: MaterialReciclable,
        punto: PuntoDeReciclaje,
        cantidad: Double
    ): ValidacionReciclaje
}

/**
 * Interfaz para calcular estadísticas
 */
interface ICalculadorEstadisticas {
    fun calcularEstadisticasUsuario(usuario: Usuario): EstadisticasUsuario
    fun calcularEstadisticasPunto(punto: PuntoDeReciclaje): EstadisticasPunto
}