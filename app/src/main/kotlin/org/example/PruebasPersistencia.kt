package org.example

import java.io.File

/**
 * Suite de pruebas para verificar el sistema de persistencia
 */
fun main() {
    println("╔════════════════════════════════════════════════╗")
    println("║      PRUEBAS - SISTEMA DE PERSISTENCIA        ║")
    println("╚════════════════════════════════════════════════╝\n")

    var testsPasados = 0
    var testsTotales = 0

    // Limpiar archivos previos
    limpiarArchivos()
    RepositorioRegistros.limpiar()

    // Test 1: Guardar y cargar usuarios
    testsTotales++
    println("Test 1: Guardar y cargar usuarios")
    try {
        val usuarios = listOf(
            Usuario("Test User 1", "test1@mail.com", 50),
            Usuario("Test User 2", "test2@mail.com", 100)
        )

        GestorPersistencia.guardarTodo(usuarios, emptyList(), emptyList())
        val datos = GestorPersistencia.cargarTodo()

        assert(datos.usuarios.size == 2) { "Debe cargar 2 usuarios" }
        assert(datos.usuarios[0].nombre == "Test User 1") { "Nombre correcto" }
        assert(datos.usuarios[0].email == "test1@mail.com") { "Email correcto" }
        assert(datos.usuarios[0].puntos == 50) { "Puntos correctos" }
        assert(datos.usuarios[1].puntos == 100) { "Puntos usuario 2" }

        println("✅ PASÓ - Usuarios guardados y cargados correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 2: Guardar y cargar puntos de reciclaje
    testsTotales++
    println("Test 2: Guardar y cargar puntos de reciclaje")
    try {
        limpiarArchivos()

        val puntos = listOf(
            PuntoDeReciclaje("Punto 1", "Dirección 1", listOf(TipoMaterial.PLASTICO, TipoMaterial.VIDRIO)),
            PuntoDeReciclaje("Punto 2", "Dirección 2", listOf(TipoMaterial.METAL))
        )

        GestorPersistencia.guardarTodo(emptyList(), puntos, emptyList())
        val datos = GestorPersistencia.cargarTodo()

        assert(datos.puntos.size == 2) { "Debe cargar 2 puntos" }
        assert(datos.puntos[0].nombre == "Punto 1") { "Nombre correcto" }
        assert(datos.puntos[0].direccion == "Dirección 1") { "Dirección correcta" }
        assert(datos.puntos[0].materialesAceptados.size == 2) { "2 materiales aceptados" }
        assert(datos.puntos[1].materialesAceptados[0] == TipoMaterial.METAL) { "Material METAL" }

        println("✅ PASÓ - Puntos guardados y cargados correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 3: Guardar y cargar registros con relaciones
    testsTotales++
    println("Test 3: Guardar y cargar registros con relaciones")
    try {
        limpiarArchivos()
        RepositorioRegistros.limpiar()

        val usuario = Usuario("Juan", "juan@mail.com", 0)
        val punto = PuntoDeReciclaje("EcoPunto", "Av. Principal", listOf(TipoMaterial.PLASTICO))
        val material = MaterialReciclable("Botella", "PET", TipoMaterial.PLASTICO, 1.0)

        val resultado = GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0)
        assert(resultado.exitoso) { "Reciclaje debe ser exitoso" }

        GestorPersistencia.guardarTodo(
            listOf(usuario),
            listOf(punto),
            RepositorioRegistros.obtenerTodos()
        )

        RepositorioRegistros.limpiar()
        val datos = GestorPersistencia.cargarTodo()

        assert(datos.registros.size == 1) { "Debe cargar 1 registro" }
        assert(datos.registros[0].usuario.email == "juan@mail.com") { "Usuario correcto" }
        assert(datos.registros[0].puntoDeReciclaje.nombre == "EcoPunto") { "Punto correcto" }
        assert(datos.registros[0].cantidad == 1.0) { "Cantidad correcta" }

        println("✅ PASÓ - Registros con relaciones guardados correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
        e.printStackTrace()
    }

    // Test 4: Verificar archivos JSON se crean correctamente
    testsTotales++
    println("Test 4: Verificar creación de archivos JSON")
    try {
        limpiarArchivos()

        val usuario = Usuario("Test", "test@mail.com", 50)
        GestorPersistencia.guardarTodo(listOf(usuario), emptyList(), emptyList())

        val archivo = File("usuarios.json")
        assert(archivo.exists()) { "Archivo usuarios.json debe existir" }

        val contenido = archivo.readText()
        assert(contenido.contains("\"nombre\"")) { "Debe contener clave 'nombre'" }
        assert(contenido.contains("\"Test\"")) { "Debe contener el nombre" }
        assert(contenido.contains("\"puntos\": 50")) { "Debe contener los puntos" }

        println("✅ PASÓ - Archivos JSON creados correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 5: Manejo de caracteres especiales
    testsTotales++
    println("Test 5: Escape de caracteres especiales")
    try {
        limpiarArchivos()

        val usuario = Usuario("Juan \"El Reciclador\"", "juan@mail.com", 50)
        GestorPersistencia.guardarTodo(listOf(usuario), emptyList(), emptyList())

        val datos = GestorPersistencia.cargarTodo()
        assert(datos.usuarios[0].nombre == "Juan \"El Reciclador\"") { "Comillas escapadas correctamente" }

        println("✅ PASÓ - Caracteres especiales manejados correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 6: Cargar con archivos inexistentes
    testsTotales++
    println("Test 6: Comportamiento con archivos inexistentes")
    try {
        limpiarArchivos()

        val datos = GestorPersistencia.cargarTodo()
        assert(datos.usuarios.isEmpty()) { "Usuarios vacío" }
        assert(datos.puntos.isEmpty()) { "Puntos vacío" }
        assert(datos.registros.isEmpty()) { "Registros vacío" }

        println("✅ PASÓ - Manejo correcto de archivos inexistentes\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 7: Múltiples guardar/cargar no duplican datos
    testsTotales++
    println("Test 7: Múltiples guardados/cargados")
    try {
        limpiarArchivos()

        val usuario = Usuario("Test", "test@mail.com", 100)
        GestorPersistencia.guardarTodo(listOf(usuario), emptyList(), emptyList())

        val datos1 = GestorPersistencia.cargarTodo()
        assert(datos1.usuarios.size == 1) { "Primera carga: 1 usuario" }

        // Cargar de nuevo sin limpiar
        val datos2 = GestorPersistencia.cargarTodo()
        assert(datos2.usuarios.size == 1) { "Segunda carga: sigue siendo 1 usuario" }

        println("✅ PASÓ - No hay duplicación de datos\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 8: Persistencia mantiene puntos acumulados
    testsTotales++
    println("Test 8: Puntos se mantienen después de guardar/cargar")
    try {
        limpiarArchivos()
        RepositorioRegistros.limpiar()

        val usuario = Usuario("Acumulador", "acum@mail.com", 0)
        val punto = PuntoDeReciclaje("Punto", "Dir", listOf(TipoMaterial.PLASTICO))
        val material = MaterialReciclable("Mat", "Desc", TipoMaterial.PLASTICO, 1.0)

        // Primera recarga: +5 puntos
        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0)
        assert(usuario.puntos == 5) { "Primera: 5 puntos" }

        // Guardar
        GestorPersistencia.guardarTodo(
            listOf(usuario),
            listOf(punto),
            RepositorioRegistros.obtenerTodos()
        )

        // Cargar
        val datos = GestorPersistencia.cargarTodo()
        assert(datos.usuarios[0].puntos == 5) { "Puntos mantenidos: 5" }

        // Segunda recarga con usuario cargado: +5 = 10
        val usuarioCargado = datos.usuarios[0]
        RepositorioRegistros.limpiar()
        datos.registros.forEach { RepositorioRegistros.agregar(it) }

        GestorDeReciclaje.registrarReciclaje(usuarioCargado, material, datos.puntos[0], 1.0)
        assert(usuarioCargado.puntos == 10) { "Segunda: 10 puntos acumulados" }

        println("✅ PASÓ - Puntos se acumulan correctamente tras persistencia\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
        e.printStackTrace()
    }

    // Test 9: Formato de fecha ISO
    testsTotales++
    println("Test 9: Fechas en formato ISO correcto")
    try {
        limpiarArchivos()
        RepositorioRegistros.limpiar()

        val usuario = Usuario("Test", "test@mail.com", 0)
        val punto = PuntoDeReciclaje("Punto", "Dir", listOf(TipoMaterial.PAPEL))
        val material = MaterialReciclable("Papel", "Desc", TipoMaterial.PAPEL, 1.0)

        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0)
        GestorPersistencia.guardarTodo(
            listOf(usuario),
            listOf(punto),
            RepositorioRegistros.obtenerTodos()
        )

        val archivo = File("registros.json")
        val contenido = archivo.readText()

        // Verificar formato ISO: YYYY-MM-DDTHH:MM:SS
        val regexISO = Regex("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}")
        assert(regexISO.containsMatchIn(contenido)) { "Debe contener fecha en formato ISO" }

        println("✅ PASÓ - Fechas en formato ISO correcto\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
    }

    // Test 10: Integración completa
    testsTotales++
    println("Test 10: Integración completa del sistema")
    try {
        limpiarArchivos()
        RepositorioRegistros.limpiar()

        // Crear datos completos
        val usuarios = mutableListOf(
            Usuario("User1", "user1@mail.com", 0),
            Usuario("User2", "user2@mail.com", 0)
        )
        val puntos = mutableListOf(
            PuntoDeReciclaje("P1", "Dir1", listOf(TipoMaterial.PLASTICO)),
            PuntoDeReciclaje("P2", "Dir2", listOf(TipoMaterial.VIDRIO))
        )
        val material1 = MaterialReciclable("M1", "D1", TipoMaterial.PLASTICO, 1.0)
        val material2 = MaterialReciclable("M2", "D2", TipoMaterial.VIDRIO, 1.0)

        // Realizar reciclajes
        GestorDeReciclaje.registrarReciclaje(usuarios[0], material1, puntos[0], 1.0)
        GestorDeReciclaje.registrarReciclaje(usuarios[1], material2, puntos[1], 1.0)
        GestorDeReciclaje.registrarReciclaje(usuarios[0], material1, puntos[0], 2.0)

        // Guardar todo
        GestorPersistencia.guardarTodo(usuarios, puntos, RepositorioRegistros.obtenerTodos())

        // Limpiar y recargar
        RepositorioRegistros.limpiar()
        val datos = GestorPersistencia.cargarTodo()

        // Verificaciones finales
        assert(datos.usuarios.size == 2) { "2 usuarios" }
        assert(datos.puntos.size == 2) { "2 puntos" }
        assert(datos.registros.size == 3) { "3 registros" }
        assert(datos.usuarios[0].puntos == 15) { "User1: 15 puntos (5+10)" }
        assert(datos.usuarios[1].puntos == 3) { "User2: 3 puntos" }

        println("✅ PASÓ - Integración completa funciona correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    } catch (e: Exception) {
        println("❌ ERROR - ${e.message}\n")
        e.printStackTrace()
    }

    // Limpieza final
    println("🧹 Limpiando archivos de prueba...")
    limpiarArchivos()

    // Resumen final
    println("\n╔════════════════════════════════════════════════╗")
    println("║              RESUMEN DE PRUEBAS                ║")
    println("╚════════════════════════════════════════════════╝")
    println("Tests ejecutados: $testsTotales")
    println("Tests pasados: $testsPasados")
    println("Tests fallidos: ${testsTotales - testsPasados}")

    if (testsPasados == testsTotales) {
        println("\n🎉 ¡TODOS LOS TESTS PASARON! 🎉")
        println("El sistema de persistencia funciona correctamente.")
    } else {
        println("\n⚠️ Algunos tests fallaron. Revisa la implementación.")
    }
}

/**
 * Elimina los archivos JSON de prueba
 */
private fun limpiarArchivos() {
    File("usuarios.json").delete()
    File("puntos_reciclaje.json").delete()
    File("registros.json").delete()
}