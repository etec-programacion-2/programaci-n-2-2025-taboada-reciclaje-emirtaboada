package org.example

/**
 * Suite de pruebas para verificar el funcionamiento del GestorDeReciclaje
 * Ejecuta este archivo para validar que todo funciona correctamente
 */
fun main() {
    println("╔════════════════════════════════════════════════╗")
    println("║   SUITE DE PRUEBAS - GESTOR DE RECICLAJE      ║")
    println("╚════════════════════════════════════════════════╝\n")

    var testsPasados = 0
    var testsTotales = 0

    // Limpiar datos previos
    RepositorioRegistros.limpiar()

    // Test 1: Registrar reciclaje exitoso
    testsTotales++
    println("Test 1: Registrar reciclaje exitoso")
    try {
        val usuario = Usuario("Test User", "test@example.com")
        val material = MaterialReciclable("Botella", "PET", TipoMaterial.PLASTICO, 1.0)
        val punto = PuntoDeReciclaje("Test Point", "Test Address", listOf(TipoMaterial.PLASTICO))

        val resultado = GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0)

        assert(resultado.exitoso) { "El reciclaje debería ser exitoso" }
        assert(resultado.puntosGanados == 5) { "Debería ganar 5 puntos (1kg * 5)" }
        assert(usuario.puntos == 5) { "El usuario debería tener 5 puntos" }
        assert(resultado.registro != null) { "Debe existir un registro" }

        println("✅ PASÓ - Reciclaje registrado correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 2: Rechazar material no aceptado
    testsTotales++
    println("Test 2: Rechazar material no aceptado")
    try {
        val usuario = Usuario("Test User 2", "test2@example.com")
        val material = MaterialReciclable("Lata", "Aluminio", TipoMaterial.METAL, 0.5)
        val punto = PuntoDeReciclaje("Test Point 2", "Address", listOf(TipoMaterial.PLASTICO))

        val resultado = GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 0.5)

        assert(!resultado.exitoso) { "El reciclaje debería fallar" }
        assert(resultado.puntosGanados == 0) { "No debería ganar puntos" }
        assert(usuario.puntos == 0) { "El usuario no debería tener puntos" }

        println("✅ PASÓ - Material rechazado correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 3: Validar cantidad negativa
    testsTotales++
    println("Test 3: Validar cantidad negativa")
    try {
        val usuario = Usuario("Test User 3", "test3@example.com")
        val material = MaterialReciclable("Papel", "Periódico", TipoMaterial.PAPEL, 1.0)
        val punto = PuntoDeReciclaje("Test Point 3", "Address", listOf(TipoMaterial.PAPEL))

        val resultado = GestorDeReciclaje.registrarReciclaje(usuario, material, punto, -1.0)

        assert(!resultado.exitoso) { "El reciclaje debería fallar con cantidad negativa" }
        assert(resultado.mensaje.contains("mayor a 0")) { "Debe indicar el error" }

        println("✅ PASÓ - Cantidad negativa rechazada\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 4: Validar cantidad cero
    testsTotales++
    println("Test 4: Validar cantidad cero")
    try {
        val usuario = Usuario("Test User 4", "test4@example.com")
        val material = MaterialReciclable("Vidrio", "Botella", TipoMaterial.VIDRIO, 1.0)
        val punto = PuntoDeReciclaje("Test Point 4", "Address", listOf(TipoMaterial.VIDRIO))

        val resultado = GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 0.0)

        assert(!resultado.exitoso) { "El reciclaje debería fallar con cantidad cero" }

        println("✅ PASÓ - Cantidad cero rechazada\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 5: Calcular estadísticas de usuario
    testsTotales++
    println("Test 5: Calcular estadísticas de usuario")
    try {
        RepositorioRegistros.limpiar()

        val usuario = Usuario("Stats User", "stats@example.com")
        val material1 = MaterialReciclable("Plástico", "PET", TipoMaterial.PLASTICO, 1.0)
        val material2 = MaterialReciclable("Papel", "Periódico", TipoMaterial.PAPEL, 2.0)
        val punto = PuntoDeReciclaje("Stats Point", "Address",
            listOf(TipoMaterial.PLASTICO, TipoMaterial.PAPEL))

        GestorDeReciclaje.registrarReciclaje(usuario, material1, punto, 1.0)
        GestorDeReciclaje.registrarReciclaje(usuario, material2, punto, 2.0)

        val stats = GestorDeReciclaje.calcularEstadisticasUsuario(usuario)

        assert(stats.totalReciclajes == 2) { "Debe tener 2 reciclajes" }
        assert(stats.totalKgReciclados == 3.0) { "Debe tener 3.0 kg reciclados" }
        assert(stats.materialesPorTipo[TipoMaterial.PLASTICO] == 1.0) { "1 kg de plástico" }
        assert(stats.materialesPorTipo[TipoMaterial.PAPEL] == 2.0) { "2 kg de papel" }

        println("✅ PASÓ - Estadísticas calculadas correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 6: Calcular estadísticas de punto
    testsTotales++
    println("Test 6: Calcular estadísticas de punto")
    try {
        RepositorioRegistros.limpiar()

        val usuario1 = Usuario("User 1", "user1@example.com")
        val usuario2 = Usuario("User 2", "user2@example.com")
        val material = MaterialReciclable("Metal", "Lata", TipoMaterial.METAL, 0.5)
        val punto = PuntoDeReciclaje("Popular Point", "Address", listOf(TipoMaterial.METAL))

        GestorDeReciclaje.registrarReciclaje(usuario1, material, punto, 0.5)
        GestorDeReciclaje.registrarReciclaje(usuario2, material, punto, 0.5)
        GestorDeReciclaje.registrarReciclaje(usuario1, material, punto, 0.3)

        val stats = GestorDeReciclaje.calcularEstadisticasPunto(punto)

        assert(stats.totalReciclajes == 3) { "Debe tener 3 reciclajes" }
        assert(stats.totalKgRecibidos == 1.3) { "Debe tener 1.3 kg recibidos" }
        assert(stats.usuariosUnicos == 2) { "Debe tener 2 usuarios únicos" }

        println("✅ PASÓ - Estadísticas de punto calculadas correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 7: Validar reciclaje antes de ejecutar
    testsTotales++
    println("Test 7: Validar reciclaje antes de ejecutar")
    try {
        val material = MaterialReciclable("Orgánico", "Restos", TipoMaterial.ORGANICO, 1.0)
        val punto = PuntoDeReciclaje("Validation Point", "Address", listOf(TipoMaterial.PLASTICO))

        val validacion = GestorDeReciclaje.validarReciclaje(material, punto, 1.0)

        assert(!validacion.valido) { "La validación debería fallar" }
        assert(validacion.errores.isNotEmpty()) { "Debe tener errores" }

        println("✅ PASÓ - Validación funciona correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 8: Obtener historial de usuario
    testsTotales++
    println("Test 8: Obtener historial de usuario")
    try {
        RepositorioRegistros.limpiar()

        val usuario = Usuario("History User", "history@example.com")
        val material = MaterialReciclable("Vidrio", "Botella", TipoMaterial.VIDRIO, 1.0)
        val punto = PuntoDeReciclaje("History Point", "Address", listOf(TipoMaterial.VIDRIO))

        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0)
        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 0.5)

        val historial = GestorDeReciclaje.obtenerHistorialUsuario(usuario)

        assert(historial.size == 2) { "Debe tener 2 registros" }
        assert(historial[0].usuario == usuario) { "El registro debe ser del usuario correcto" }

        println("✅ PASÓ - Historial recuperado correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 9: Cálculo de puntos por diferentes materiales
    testsTotales++
    println("Test 9: Cálculo de puntos por diferentes materiales")
    try {
        RepositorioRegistros.limpiar()

        val usuario = Usuario("Points User", "points@example.com")
        val punto = PuntoDeReciclaje("Multi Point", "Address",
            listOf(TipoMaterial.PLASTICO, TipoMaterial.VIDRIO, TipoMaterial.PAPEL,
                TipoMaterial.METAL, TipoMaterial.ORGANICO))

        // PLASTICO: 1kg * 5 = 5 puntos
        val plastico = MaterialReciclable("Plástico", "PET", TipoMaterial.PLASTICO, 1.0)
        val r1 = GestorDeReciclaje.registrarReciclaje(usuario, plastico, punto, 1.0)
        assert(r1.puntosGanados == 5) { "Plástico: 5 puntos" }

        // METAL: 1kg * 4 = 4 puntos
        val metal = MaterialReciclable("Metal", "Aluminio", TipoMaterial.METAL, 1.0)
        val r2 = GestorDeReciclaje.registrarReciclaje(usuario, metal, punto, 1.0)
        assert(r2.puntosGanados == 4) { "Metal: 4 puntos" }

        // VIDRIO: 1kg * 3 = 3 puntos
        val vidrio = MaterialReciclable("Vidrio", "Botella", TipoMaterial.VIDRIO, 1.0)
        val r3 = GestorDeReciclaje.registrarReciclaje(usuario, vidrio, punto, 1.0)
        assert(r3.puntosGanados == 3) { "Vidrio: 3 puntos" }

        // PAPEL: 1kg * 2 = 2 puntos
        val papel = MaterialReciclable("Papel", "Periódico", TipoMaterial.PAPEL, 1.0)
        val r4 = GestorDeReciclaje.registrarReciclaje(usuario, papel, punto, 1.0)
        assert(r4.puntosGanados == 2) { "Papel: 2 puntos" }

        // ORGANICO: 1kg * 1 = 1 punto
        val organico = MaterialReciclable("Orgánico", "Restos", TipoMaterial.ORGANICO, 1.0)
        val r5 = GestorDeReciclaje.registrarReciclaje(usuario, organico, punto, 1.0)
        assert(r5.puntosGanados == 1) { "Orgánico: 1 punto" }

        assert(usuario.puntos == 15) { "Total: 15 puntos (5+4+3+2+1)" }

        println("✅ PASÓ - Puntos calculados correctamente para todos los materiales\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Test 10: Múltiples reciclajes acumulan puntos
    testsTotales++
    println("Test 10: Múltiples reciclajes acumulan puntos")
    try {
        RepositorioRegistros.limpiar()

        val usuario = Usuario("Accumulator", "acc@example.com")
        val material = MaterialReciclable("Plástico", "PET", TipoMaterial.PLASTICO, 1.0)
        val punto = PuntoDeReciclaje("Acc Point", "Address", listOf(TipoMaterial.PLASTICO))

        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0) // +5 = 5
        assert(usuario.puntos == 5) { "Primera vez: 5 puntos" }

        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 2.0) // +10 = 15
        assert(usuario.puntos == 15) { "Segunda vez: 15 puntos" }

        GestorDeReciclaje.registrarReciclaje(usuario, material, punto, 1.0) // +5 = 20
        assert(usuario.puntos == 20) { "Tercera vez: 20 puntos" }

        println("✅ PASÓ - Puntos se acumulan correctamente\n")
        testsPasados++
    } catch (e: AssertionError) {
        println("❌ FALLÓ - ${e.message}\n")
    }

    // Resumen final
    println("\n╔════════════════════════════════════════════════╗")
    println("║              RESUMEN DE PRUEBAS                ║")
    println("╚════════════════════════════════════════════════╝")
    println("Tests ejecutados: $testsTotales")
    println("Tests pasados: $testsPasados")
    println("Tests fallidos: ${testsTotales - testsPasados}")

    if (testsPasados == testsTotales) {
        println("\n🎉 ¡TODOS LOS TESTS PASARON! 🎉")
        println("El GestorDeReciclaje funciona correctamente.")
    } else {
        println("\n⚠️ Algunos tests fallaron. Revisa la implementación.")
    }
}