package ar.jornadas.data

class ControlDescargas {
    private var automaticaSolicitada = false
    private var enCurso = false

    @Synchronized
    fun iniciarAutomatica(): Boolean {
        if (automaticaSolicitada || enCurso) return false
        automaticaSolicitada = true
        enCurso = true
        return true
    }

    @Synchronized
    fun iniciarExplicita(): Boolean {
        if (enCurso) return false
        enCurso = true
        return true
    }

    @Synchronized
    fun terminar() {
        enCurso = false
    }
}
