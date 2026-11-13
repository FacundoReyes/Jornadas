package ar.jornadas.data

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ControlDescargasTest {
    @Test
    fun `la carga automatica ocurre una sola vez`() {
        val control = ControlDescargas()
        assertTrue(control.iniciarAutomatica())
        control.terminar()
        assertFalse(control.iniciarAutomatica())
    }

    @Test
    fun `dos toques durante una peticion agregan un solo intento`() {
        val control = ControlDescargas()
        assertTrue(control.iniciarExplicita())
        assertFalse(control.iniciarExplicita())
    }

    @Test
    fun `tras terminar se puede volver a pedir`() {
        val control = ControlDescargas()
        assertTrue(control.iniciarExplicita())
        control.terminar()
        assertTrue(control.iniciarExplicita())
    }

    @Test
    fun `una actualizacion explicita es posible despues de la automatica`() {
        val control = ControlDescargas()
        assertTrue(control.iniciarAutomatica())
        control.terminar()
        assertTrue(control.iniciarExplicita())
    }

    @Test
    fun `no se puede pedir mientras corre la automatica`() {
        val control = ControlDescargas()
        assertTrue(control.iniciarAutomatica())
        assertFalse(control.iniciarExplicita())
    }

    @Test
    fun `la carga automatica no arranca si hay una peticion en curso`() {
        val control = ControlDescargas()
        assertTrue(control.iniciarExplicita())
        assertFalse(control.iniciarAutomatica())
    }
}
