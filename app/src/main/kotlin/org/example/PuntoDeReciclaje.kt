package org.example

data class PuntoDeReciclaje(
    val nombre: String,
    val direccion: String,
    val materialesAceptados: List<TipoMaterial>
) {
    fun aceptaMaterial(material: TipoMaterial): Boolean {
        return materialesAceptados.contains(material)
    }
}
