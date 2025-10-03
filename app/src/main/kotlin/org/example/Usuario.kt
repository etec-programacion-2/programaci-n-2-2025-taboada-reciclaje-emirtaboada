package org.example

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
    }

    fun reciclar(materiales: List<MaterialReciclable>, puntos: List<PuntoDeReciclaje>) {
        println("\n--- RECICLAR MATERIAL ---")
        println("Materiales disponibles:")
        materiales.forEachIndexed { index, material ->
            println("${index + 1}. ${material.nombre} (${material.tipo})")
        }
        print("Selecciona el material a reciclar: ")
        val materialIndex = (readLine()?.toIntOrNull() ?: 1) - 1
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
        val puntoIndex = (readLine()?.toIntOrNull() ?: 1) - 1
        val puntoSeleccionado = puntos.getOrNull(puntoIndex)
        
        if (puntoSeleccionado == null) {
            println("\n❌ Punto de reciclaje no válido")
            return
        }
        
        if (puntoSeleccionado.aceptaMaterial(materialSeleccionado.tipo)) {
            val puntosGanados = materialSeleccionado.calcularPuntos()
            agregarPuntos(puntosGanados)
            println("\n✅ ¡Reciclaje exitoso!")
            println("$nombre recicló '${materialSeleccionado.nombre}' en '${puntoSeleccionado.nombre}'")
            println("🌟 Ganaste $puntosGanados puntos")
            println("📊 Puntos totales: $puntos")
        } else {
            println("\n❌ El punto '${puntoSeleccionado.nombre}' no acepta ${materialSeleccionado.tipo}")
            println("Materiales aceptados: ${puntoSeleccionado.materialesAceptados}")
        }
    }

    companion object {
        fun crearOSeleccionar(usuarios: MutableList<Usuario>): Usuario {
            println("\n--- USUARIO ---")
            
            if (usuarios.isNotEmpty()) {
                println("Usuarios existentes:")
                usuarios.forEachIndexed { index, usuario ->
                    println("${index + 1}. ${usuario.nombre} (${usuario.email}) - ${usuario.puntos} puntos")
                }
                print("¿Seleccionar usuario existente? (S/N): ")
                if (readLine()?.uppercase() == "S") {
                    print("Número de usuario: ")
                    val index = (readLine()?.toIntOrNull() ?: 1) - 1
                    usuarios.getOrNull(index)?.let {
                        println("\n✅ Usuario seleccionado: ${it.nombre}")
                        return it
                    }
                }
            }
            
            print("Nombre: ")
            val nombre = readLine() ?: ""
            
            print("Email: ")
            val email = readLine() ?: ""
            
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