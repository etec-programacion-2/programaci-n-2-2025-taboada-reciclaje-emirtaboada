package org.example

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
}