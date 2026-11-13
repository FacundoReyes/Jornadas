package ar.jornadas.data

class AgendaRepository(
    private val api: ProgramaApi,
    private val dao: CarteleraDao,
    private val edicion: EstadoEdicion
) {
    val programa = dao.observar()

    suspend fun sincronizar() {
        val fichas = api.consultar().map {
            Charla(it.id, it.title, it.track, it.day, it.time, it.room, it.speaker, it.summary)
        }
        dao.reemplazarPrograma(fichas)
        edicion.descargadaAlgunaVez = true
    }

    suspend fun hayEdicion(): Boolean =
        ReglasPrograma.hayEdicion(edicion.descargadaAlgunaVez, dao.cantidad())

    suspend fun cambiarFavorita(id: String) = dao.alternar(id)
}
