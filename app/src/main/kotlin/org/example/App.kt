package org.example

import org.example.MaterialReciclable
import org.example.TipoMaterial
import org.example.PuntoDeReciclaje

fun main() {
    val plastico = MaterialReciclable(
        "Botella PET",
        "Botella de plástico reciclable",
        TipoMaterial.PLASTICO
    )

    val vidrio = MaterialReciclable(
        "Botella de vidrio",
        "Vidrio transparente reciclable",
        TipoMaterial.VIDRIO
    )

    val papel = MaterialReciclable(
        "Caja de cartón",
        "Cartón reciclable",
        TipoMaterial.PAPEL
    )

    val metal = MaterialReciclable(
        "Lata de aluminio",
        "Lata de gaseosa reciclable",
        TipoMaterial.METAL
    )

    val organico = MaterialReciclable(
        "Cáscara de banana",
        "Residuo orgánico biodegradable",
        TipoMaterial.ORGANICO
    )
   println(plastico)
    println(vidrio)
    println(papel)
    println(metal)
    println(organico)

    val punto1 = PuntoDeReciclaje(
        "Punto Verde Plaza Central",
        "Av. Siempre Viva 123",
        listOf(TipoMaterial.PLASTICO, TipoMaterial.VIDRIO, TipoMaterial.PAPEL)
    )

    println(punto1)
    println("¿Acepta plástico? ${punto1.aceptaMaterial(TipoMaterial.PLASTICO)}")
    println("¿Acepta metal? ${punto1.aceptaMaterial(TipoMaterial.METAL)}")
}
