package org.example

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Gestor de persistencia de datos del sistema de reciclaje.
 *
 * FORMATO ELEGIDO: JSON (JavaScript Object Notation)
 *
 * ¿Por qué JSON?
 * - Legible por humanos: Fácil de inspeccionar y debuggear
 * - Estructurado: Soporta objetos anidados y listas
 * - Estándar: Ampliamente usado y compatible
 * - Tipado: Mantiene tipos de datos (strings, números, booleanos)
 *
 * PROCESO DE SERIALIZACIÓN:
 * 1. Convertir objetos Kotlin a formato JSON (String)
 * 2. Escribir el String JSON en archivos .json
 *
 * PROCESO DE DESERIALIZACIÓN:
 * 1. Leer el contenido del archivo .json como String
 * 2. Parsear el String JSON a objetos Kotlin
 * 3. Reconstruir las relaciones entre objetos
 *
 * ARCHIVOS GENERADOS:
 * - usuarios.json: Lista de usuarios con sus puntos
 * - puntos_reciclaje.json: Puntos de reciclaje con materiales aceptados
 * - registros.json: Historial completo de reciclajes
 */
object GestorPersistencia {

    private const val ARCHIVO_USUARIOS = "usuarios.json"
    private const val ARCHIVO_PUNTOS = "puntos_reciclaje.json"
    private const val ARCHIVO_REGISTROS = "registros.json"

    /**
     * Guarda todos los datos del sistema en archivos JSON
     */
    fun guardarTodo(
        usuarios: List<Usuario>,
        puntos: List<PuntoDeReciclaje>,
        registros: List<RegistroReciclaje>
    ) {
        try {
            guardarUsuarios(usuarios)
            guardarPuntosReciclaje(puntos)
            guardarRegistros(registros)
            println("\n💾 Datos guardados exitosamente")
        } catch (e: Exception) {
            println("\n❌ Error al guardar datos: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Carga todos los datos desde archivos JSON
     * Retorna una estructura con usuarios, puntos y registros
     */
    fun cargarTodo(): DatosCargados {
        try {
            val usuarios = cargarUsuarios()
            val puntos = cargarPuntosReciclaje()
            val registros = cargarRegistros(usuarios, puntos)

            if (usuarios.isNotEmpty() || puntos.isNotEmpty() || registros.isNotEmpty()) {
                println("\n📂 Datos cargados exitosamente:")
                println("   • ${usuarios.size} usuarios")
                println("   • ${puntos.size} puntos de reciclaje")
                println("   • ${registros.size} registros")
            }

            return DatosCargados(usuarios, puntos, registros)
        } catch (e: Exception) {
            println("\n⚠️ No se pudieron cargar datos previos (archivos no encontrados o corruptos)")
            return DatosCargados(emptyList(), emptyList(), emptyList())
        }
    }

    /**
     * SERIALIZACIÓN DE USUARIOS
     * Convierte la lista de usuarios a formato JSON manual
     */
    private fun guardarUsuarios(usuarios: List<Usuario>) {
        val json = buildString {
            appendLine("[")
            usuarios.forEachIndexed { index, usuario ->
                appendLine("  {")
                appendLine("    \"nombre\": \"${escaparJson(usuario.nombre)}\",")
                appendLine("    \"email\": \"${escaparJson(usuario.email)}\",")
                appendLine("    \"puntos\": ${usuario.puntos}")
                append("  }")
                if (index < usuarios.size - 1) appendLine(",")
                else appendLine()
            }
            append("]")
        }

        File(ARCHIVO_USUARIOS).writeText(json)
    }

    /**
     * DESERIALIZACIÓN DE USUARIOS
     * Lee el archivo JSON y reconstruye los objetos Usuario
     */
    private fun cargarUsuarios(): MutableList<Usuario> {
        val file = File(ARCHIVO_USUARIOS)
        if (!file.exists()) return mutableListOf()

        val usuarios = mutableListOf<Usuario>()
        val contenido = file.readText()

        // Parser JSON manual simple
        val objetos = extraerObjetosJson(contenido)
        objetos.forEach { obj ->
            val nombre = extraerValorJson(obj, "nombre")
            val email = extraerValorJson(obj, "email")
            val puntos = extraerValorJson(obj, "puntos").toIntOrNull() ?: 0

            if (nombre.isNotEmpty() && email.isNotEmpty()) {
                usuarios.add(Usuario(nombre, email, puntos))
            }
        }

        return usuarios
    }

    /**
     * SERIALIZACIÓN DE PUNTOS DE RECICLAJE
     */
    private fun guardarPuntosReciclaje(puntos: List<PuntoDeReciclaje>) {
        val json = buildString {
            appendLine("[")
            puntos.forEachIndexed { index, punto ->
                appendLine("  {")
                appendLine("    \"nombre\": \"${escaparJson(punto.nombre)}\",")
                appendLine("    \"direccion\": \"${escaparJson(punto.direccion)}\",")
                append("    \"materialesAceptados\": [")
                punto.materialesAceptados.forEachIndexed { i, material ->
                    append("\"$material\"")
                    if (i < punto.materialesAceptados.size - 1) append(", ")
                }
                appendLine("]")
                append("  }")
                if (index < puntos.size - 1) appendLine(",")
                else appendLine()
            }
            append("]")
        }

        File(ARCHIVO_PUNTOS).writeText(json)
    }

    /**
     * DESERIALIZACIÓN DE PUNTOS DE RECICLAJE
     */
    private fun cargarPuntosReciclaje(): MutableList<PuntoDeReciclaje> {
        val file = File(ARCHIVO_PUNTOS)
        if (!file.exists()) return mutableListOf()

        val puntos = mutableListOf<PuntoDeReciclaje>()
        val contenido = file.readText()

        val objetos = extraerObjetosJson(contenido)
        objetos.forEach { obj ->
            val nombre = extraerValorJson(obj, "nombre")
            val direccion = extraerValorJson(obj, "direccion")
            val materialesStr = extraerArrayJson(obj, "materialesAceptados")

            val materiales = materialesStr.mapNotNull { mat ->
                try {
                    TipoMaterial.valueOf(mat)
                } catch (e: Exception) {
                    null
                }
            }

            if (nombre.isNotEmpty() && materiales.isNotEmpty()) {
                puntos.add(PuntoDeReciclaje(nombre, direccion, materiales))
            }
        }

        return puntos
    }

    /**
     * SERIALIZACIÓN DE REGISTROS
     * Guarda referencias a usuarios y puntos por nombre/email
     */
    private fun guardarRegistros(registros: List<RegistroReciclaje>) {
        val json = buildString {
            appendLine("[")
            registros.forEachIndexed { index, registro ->
                appendLine("  {")
                appendLine("    \"usuarioEmail\": \"${escaparJson(registro.usuario.email)}\",")
                appendLine("    \"materialNombre\": \"${escaparJson(registro.material.nombre)}\",")
                appendLine("    \"materialDescripcion\": \"${escaparJson(registro.material.descripcion)}\",")
                appendLine("    \"materialTipo\": \"${registro.material.tipo}\",")
                appendLine("    \"materialPeso\": ${registro.material.pesoKg},")
                appendLine("    \"puntoNombre\": \"${escaparJson(registro.puntoDeReciclaje.nombre)}\",")
                appendLine("    \"cantidad\": ${registro.cantidad},")
                appendLine("    \"fecha\": \"${registro.fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)}\"")
                append("  }")
                if (index < registros.size - 1) appendLine(",")
                else appendLine()
            }
            append("]")
        }

        File(ARCHIVO_REGISTROS).writeText(json)
    }

    /**
     * DESERIALIZACIÓN DE REGISTROS
     * Reconstruye las relaciones con usuarios y puntos existentes
     */
    private fun cargarRegistros(
        usuarios: List<Usuario>,
        puntos: List<PuntoDeReciclaje>
    ): List<RegistroReciclaje> {
        val file = File(ARCHIVO_REGISTROS)
        if (!file.exists()) return emptyList()

        val registros = mutableListOf<RegistroReciclaje>()
        val contenido = file.readText()

        val objetos = extraerObjetosJson(contenido)
        objetos.forEach { obj ->
            try {
                val usuarioEmail = extraerValorJson(obj, "usuarioEmail")
                val materialNombre = extraerValorJson(obj, "materialNombre")
                val materialDesc = extraerValorJson(obj, "materialDescripcion")
                val materialTipoStr = extraerValorJson(obj, "materialTipo")
                val materialPeso = extraerValorJson(obj, "materialPeso").toDoubleOrNull() ?: 0.0
                val puntoNombre = extraerValorJson(obj, "puntoNombre")
                val cantidad = extraerValorJson(obj, "cantidad").toDoubleOrNull() ?: 0.0
                val fechaStr = extraerValorJson(obj, "fecha")

                // Buscar usuario y punto correspondientes
                val usuario = usuarios.find { it.email == usuarioEmail }
                val punto = puntos.find { it.nombre == puntoNombre }
                val materialTipo = TipoMaterial.valueOf(materialTipoStr)
                val fecha = LocalDateTime.parse(fechaStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

                if (usuario != null && punto != null) {
                    val material = MaterialReciclable(
                        materialNombre,
                        materialDesc,
                        materialTipo,
                        materialPeso
                    )

                    registros.add(RegistroReciclaje(usuario, material, punto, cantidad, fecha))
                }
            } catch (e: Exception) {
                println("⚠️ Error al cargar registro: ${e.message}")
            }
        }

        return registros
    }

    // ===== UTILIDADES DE PARSING JSON =====

    /**
     * Escapa caracteres especiales para JSON
     */
    private fun escaparJson(str: String): String {
        return str.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t")
    }

    /**
     * Extrae objetos JSON de un array JSON
     */
    private fun extraerObjetosJson(json: String): List<String> {
        val objetos = mutableListOf<String>()
        var nivel = 0
        var inicio = -1

        for (i in json.indices) {
            when (json[i]) {
                '{' -> {
                    if (nivel == 0) inicio = i
                    nivel++
                }
                '}' -> {
                    nivel--
                    if (nivel == 0 && inicio != -1) {
                        objetos.add(json.substring(inicio, i + 1))
                        inicio = -1
                    }
                }
            }
        }

        return objetos
    }

    /**
     * Extrae el valor de una propiedad JSON
     */
    private fun extraerValorJson(json: String, clave: String): String {
        val patron = "\"$clave\"\\s*:\\s*\"([^\"]*)\"|\"$clave\"\\s*:\\s*([^,}\\s]+)"
        val regex = Regex(patron)
        val match = regex.find(json)
        return match?.groupValues?.get(1)?.ifEmpty { match.groupValues[2] } ?: ""
    }

    /**
     * Extrae un array JSON como lista de strings
     */
    private fun extraerArrayJson(json: String, clave: String): List<String> {
        val patron = "\"$clave\"\\s*:\\s*\\[([^\\]]*)\\]"
        val regex = Regex(patron)
        val match = regex.find(json)
        val contenido = match?.groupValues?.get(1) ?: return emptyList()

        return contenido.split(",")
            .map { it.trim().removeSurrounding("\"") }
            .filter { it.isNotEmpty() }
    }

    /**
     * Verifica si existen archivos de datos previos
     */
    fun existenDatosPrevios(): Boolean {
        return File(ARCHIVO_USUARIOS).exists() ||
                File(ARCHIVO_PUNTOS).exists() ||
                File(ARCHIVO_REGISTROS).exists()
    }
}

/**
 * Estructura para retornar los datos cargados
 */
data class DatosCargados(
    val usuarios: List<Usuario>,
    val puntos: List<PuntoDeReciclaje>,
    val registros: List<RegistroReciclaje>
)