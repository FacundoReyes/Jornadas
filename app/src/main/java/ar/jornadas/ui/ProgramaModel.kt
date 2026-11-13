package ar.jornadas.ui

import android.app.Application
import androidx.lifecycle.*
import ar.jornadas.JornadasApp
import ar.jornadas.data.ReglasPrograma
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

class ProgramaModel(app: Application, private val guardado: SavedStateHandle) : AndroidViewModel(app) {
    private val servicios = getApplication<JornadasApp>().servicios
    val mesa = servicios.mesa
    private val repo = servicios.agenda
    private val descargas = servicios.descargas
    val charlas = repo.programa.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    val dia: StateFlow<Int> = guardado.getStateFlow(CLAVE_DIA, ReglasPrograma.AMBOS_DIAS)
    val tema: StateFlow<String> = guardado.getStateFlow(CLAVE_TEMA, ReglasPrograma.TEMA_TODOS)
    val aviso = MutableStateFlow("")
    val cargando = MutableStateFlow(false)

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
            cargando.value = true
            aviso.value = ""
            try {
                repo.sincronizar()
            } catch (e: IOException) {
                aviso.value = "No se pudo consultar el programa"
            } finally {
                cargando.value = false
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
