package ar.jornadas.data

import android.content.SharedPreferences

interface EstadoEdicion {
    var descargadaAlgunaVez: Boolean
}

class PreferenciasEdicion(private val prefs: SharedPreferences) : EstadoEdicion {
    override var descargadaAlgunaVez: Boolean
        get() = prefs.getBoolean(CLAVE, false)
        set(value) { prefs.edit().putBoolean(CLAVE, value).apply() }

    private companion object {
        const val CLAVE = "edicion_descargada"
    }
}
