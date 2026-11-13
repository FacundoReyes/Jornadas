package ar.jornadas

import android.app.Application

class JornadasApp : Application() {
    val servicios by lazy { Servicios(this) }
}
