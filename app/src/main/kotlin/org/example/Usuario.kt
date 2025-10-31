package org.example

import java.util.Scanner

data class Usuario(
    val nombre: String,
    val email: String,
    var puntos: Int = 0
) {
    fun agregarPuntos(cantidad: Int) {
        if (cantidad > 0) {
            puntos += cantidad
        }
    }
    
    /**
     * Suma puntos al usuario (alias de agregarPuntos para cumplir con la consigna).
     * @param puntos La cantidad de puntos a sumar
     */
    fun sumarPuntos(puntos: Int) {
        agregarPuntos(puntos)
    }

    fun verPerfil() {
        println("\n--- MI PERFIL ---")
        println("Nombre: $nombre")
        println("Email: $email")
        println("🌟 Puntos acumulados: $puntos")
        
        val nivel = when {
            puntos < 50 -> "Principiante 🌱"
            puntos < 150 -> "Intermedio 🌿"
            puntos < 300 -> "Avanzado 🌳"
            else -> "Experto 🏆"
        }
        println("Nivel: $nivel")
        
        val misRegistros = RepositorioRegistros.obtenerPorUsuario(this)
        println("\n📊 Mis estadísticas:")
        println("  • Total de reciclajes: ${misRegistros.size}")
        if (misRegistros.isNotEmpty()) {
            val totalKg = misRegistros.sumOf { it.cantidad }
            println("  • Total reciclado: ${"%.2f".format(totalKg)} kg")
        }
    }
    
    /**
     * Muestra el historial completo de reciclajes del usuario
     */
    fun verHistorialReciclaje() {
        println("\n═══════════════════════════════════════")
        println("    HISTORIAL DE $nombre")
        println("═══════════════════════════════════════")
        
        val misRegistros = RepositorioRegistros.obtenerPorUsuario(this)
        
        if (misRegistros.isEmpty()) {
            println("Aún no has reciclado nada.")
        } else {
            println("Total de reciclajes: ${misRegistros.size}")
            println("\n--- Registros ---")
            misRegistros.forEachIndexed { index, registro ->
                println("\n#${index + 1}")
                registro.mostrar()
            }
        }
        println("═══════════════════════════════════════")
    }

    fun reciclar(materiales: List<MaterialReciclable>, puntos: List<PuntoDeReciclaje>, scanner: Scanner) {
        println("\n--- RECICLAR MATERIAL ---")
        
        if (materiales.isEmpty()) {
            println("❌ No hay materiales disponibles")
            return
        }
        
        if (puntos.isEmpty()) {
            println("❌ No hay puntos de reciclaje disponibles")
            return
        }
        
        println("Materiales disponibles:")
        materiales.forEachIndexed { index, material ->
            println("${index + 1}. ${material.nombre} (${material.tipo}) - ${material.pesoKg} kg")
        }
        print("Selecciona el material a reciclar: ")
        val materialIndex = (scanner.nextLine().toIntOrNull() ?: 1) - 1
        val materialSeleccionado = materiales.getOrNull(materialIndex)
        
        if (materialSeleccionado == null) {
            println("\n❌ Material no válido")
            return
        }
        
        println("\nPuntos de reciclaje disponibles:")
        puntos.forEachIndexed { index, punto ->
            println("${index + 1}. ${punto.nombre} - Acepta: ${punto.materialesAceptados}")
        }
        print("Selecciona el punto de reciclaje: ")
        val puntoIndex = (scanner.nextLine().toIntOrNull() ?: 1) - 1
        val puntoSeleccionado = puntos.getOrNull(puntoIndex)
        
        if (puntoSeleccionado == null) {
            println("\n❌ Punto de reciclaje no válido")
            return
        }
        
        print("Cantidad a reciclar (kg) [${materialSeleccionado.pesoKg}]: ")
        val cantidadInput = scanner.nextLine()
        val cantidad = if (cantidadInput.isBlank()) {
            materialSeleccionado.pesoKg
        } else {
            cantidadInput.toDoubleOrNull() ?: materialSeleccionado.pesoKg
        }
        
        // Intentar recibir el material en el punto de reciclaje
        if (puntoSeleccionado.recibirMaterial(materialSeleccionado, cantidad)) {
            // Calcular puntos usando CalculadoraPuntos
            val puntosGanados = CalculadoraPuntos.calcularPuntos(materialSeleccionado, cantidad)
            sumarPuntos(puntosGanados)

            // ✅ NUEVO: Registrar la transacción en el repositorio
            RepositorioRegistros.agregar(
                RegistroReciclaje(this, materialSeleccionado, puntoSeleccionado, cantidad)
            )
            
            println("\n✅ ¡Reciclaje exitoso!")
            println("$nombre recicló $cantidad kg de '${materialSeleccionado.nombre}' en '${puntoSeleccionado.nombre}'")
            println("🌟 Ganaste $puntosGanados puntos")
            println("📊 Puntos totales: ${this.puntos}")
        } else {
            println("\n❌ El punto '${puntoSeleccionado.nombre}' no acepta ${materialSeleccionado.tipo}")
            println("Materiales aceptados: ${puntoSeleccionado.materialesAceptados}")
        }
    }

    companion object {
        fun crearOSeleccionar(usuarios: MutableList<Usuario>, scanner: Scanner): Usuario {
            println("\n--- USUARIO ---")
            
            if (usuarios.isNotEmpty()) {
                println("Usuarios existentes:")
                usuarios.forEachIndexed { index, usuario ->
                    println("${index + 1}. ${usuario.nombre} (${usuario.email}) - ${usuario.puntos} puntos")
                }
                print("¿Seleccionar usuario existente? (S/N): ")
                if (scanner.nextLine().uppercase() == "S") {
                    print("Número de usuario: ")
                    val index = (scanner.nextLine().toIntOrNull() ?: 1) - 1
                    usuarios.getOrNull(index)?.let {
                        println("\n✅ Usuario seleccionado: ${it.nombre}")
                        return it
                    }
                }
            }
            
            print("Nombre: ")
            val nombre = scanner.nextLine()
            
            print("Email: ")
            val email = scanner.nextLine()
            
            val usuario = Usuario(nombre, email)
            usuarios.add(usuario)
            
            println("\n✅ Usuario creado exitosamente:")
            println(usuario)
            
            return usuario
        }

        fun verTodos(usuarios: List<Usuario>) {
            println("\n--- USUARIOS REGISTRADOS ---")
            if (usuarios.isEmpty()) {
                println("No hay usuarios registrados aún.")
            } else {
                usuarios.forEach { println(it) }
            }
        }
    }
}

