package ar.jornadas.ui

import android.app.Application
import androidx.lifecycle.*
import ar.jornadas.JornadasApp
import ar.jornadas.data.EstadoPrograma
import ar.jornadas.data.ReglasPrograma
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProgramaModel(app: Application, private val guardado: SavedStateHandle) : AndroidViewModel(app) {
    private val servicios = getApplication<JornadasApp>().servicios
    val mesa = servicios.mesa
    private val repo = servicios.agenda
    private val descargas = servicios.descargas
    val charlas = repo.programa.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val dia: StateFlow<Int> = guardado.getStateFlow(CLAVE_DIA, ReglasPrograma.AMBOS_DIAS)
    val tema: StateFlow<String> = guardado.getStateFlow(CLAVE_TEMA, ReglasPrograma.TEMA_TODOS)

    private val _estado = MutableStateFlow<EstadoPrograma>(EstadoPrograma.Normal)
    val estado: StateFlow<EstadoPrograma> = _estado.asStateFlow()
    private val _hayEdicion = MutableStateFlow(false)
    val hayEdicion: StateFlow<Boolean> = _hayEdicion.asStateFlow()
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    init {
        viewModelScope.launch { _hayEdicion.value = repo.hayEdicion() }
    }

    fun elegirDia(valor: Int) { guardado[CLAVE_DIA] = valor }

    fun elegirTema(valor: String) { guardado[CLAVE_TEMA] = valor }

    fun cargarAlAbrir() {
        if (descargas.iniciarAutomatica()) descargar()
    }

    fun recargar() {
        if (descargas.iniciarExplicita()) descargar()
    }

    private fun descargar() {
        viewModelScope.launch {
            _cargando.value = true
            try {
                repo.sincronizar()
                _estado.value = EstadoPrograma.Normal
                _hayEdicion.value = true
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val hayEdicion = repo.hayEdicion()
                _hayEdicion.value = hayEdicion
                _estado.value = ReglasPrograma.estadoTrasFalla(hayEdicion)
            } finally {
                _cargando.value = false
                descargas.terminar()
            }
        }
    }

    fun marcar(id: String) { viewModelScope.launch { repo.cambiarFavorita(id) } }

    private companion object {
        const val CLAVE_DIA = "filtro_dia"
        const val CLAVE_TEMA = "filtro_tema"
    }
}
