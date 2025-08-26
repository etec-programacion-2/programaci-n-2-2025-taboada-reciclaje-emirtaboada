package org.example

data class MaterialReciclable(
    val nombre: String,
    val descripcion: String,
    val tipo: String
)

fun main() {
    val plastico = MaterialReciclable("Botella PET", "Botella de plástico reciclable", "Plástico")
    val vidrio = MaterialReciclable("Botella de vidrio", "Vidrio transparente reciclable", "Vidrio")

    println(plastico)
    println(vidrio)
}
