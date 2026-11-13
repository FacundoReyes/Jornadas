package ar.jornadas.data

object ReglasPrograma {
    fun conservarMarcas(nuevas: List<Charla>, marcadas: Set<String>): List<Charla> =
        nuevas.map { it.copy(favorita = it.id in marcadas) }

    fun hayEdicion(descargadaAlgunaVez: Boolean, charlasGuardadas: Int): Boolean =
        descargadaAlgunaVez || charlasGuardadas > 0
}
