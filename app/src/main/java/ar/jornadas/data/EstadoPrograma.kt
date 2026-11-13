package ar.jornadas.data

sealed interface EstadoPrograma {
    data object Normal : EstadoPrograma
    data object UsandoGuardados : EstadoPrograma
    data object SinEdicion : EstadoPrograma
}
