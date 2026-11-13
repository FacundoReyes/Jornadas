package ar.jornadas.data

object ReglasPrograma {
    const val TEMA_TODOS = "Todos"
    const val AMBOS_DIAS = 0

    fun filtrar(charlas: List<Charla>, dia: Int, tema: String): List<Charla> =
        charlas.filter { (dia == AMBOS_DIAS || it.dia == dia) && (tema == TEMA_TODOS || it.tema == tema) }

    fun conservarMarcas(nuevas: List<Charla>, marcadas: Set<String>): List<Charla> =
        nuevas.map { it.copy(favorita = it.id in marcadas) }

    fun hayEdicion(descargadaAlgunaVez: Boolean, charlasGuardadas: Int): Boolean =
        descargadaAlgunaVez || charlasGuardadas > 0
}
