package ar.jornadas.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasProgramaTest {
    private fun charla(id: String, titulo: String = "t$id", favorita: Boolean = false) =
        Charla(id, titulo, "Android", 1, "09:00", "Auditorio", "Ponente", "Resumen", favorita)

    @Test
    fun `conserva la marca de una charla vigente aunque cambie el titulo`() {
        val nuevas = listOf(charla("11", titulo = "Compose en produccion avanzada"), charla("12"))
        val resultado = ReglasPrograma.conservarMarcas(nuevas, setOf("11"))
        assertTrue(resultado.first { it.id == "11" }.favorita)
        assertEquals("Compose en produccion avanzada", resultado.first { it.id == "11" }.titulo)
        assertFalse(resultado.first { it.id == "12" }.favorita)
    }

    @Test
    fun `una charla que no estaba marcada llega sin marcar`() {
        val resultado = ReglasPrograma.conservarMarcas(listOf(charla("90")), setOf("11"))
        assertFalse(resultado.single().favorita)
    }

    @Test
    fun `una charla que regresa tras haber sido retirada llega sin marcar`() {
        val marcadasTrasRetiro = emptySet<String>()
        val resultado = ReglasPrograma.conservarMarcas(listOf(charla("11")), marcadasTrasRetiro)
        assertFalse(resultado.single().favorita)
    }

    @Test
    fun `una lista vacia no produce charlas`() {
        assertTrue(ReglasPrograma.conservarMarcas(emptyList(), setOf("11")).isEmpty())
    }

    @Test
    fun `hay edicion si se descargo alguna vez aunque no haya charlas`() {
        assertTrue(ReglasPrograma.hayEdicion(descargadaAlgunaVez = true, charlasGuardadas = 0))
    }

    @Test
    fun `hay edicion si hay charlas guardadas de una version anterior de la app`() {
        assertTrue(ReglasPrograma.hayEdicion(descargadaAlgunaVez = false, charlasGuardadas = 12))
    }

    @Test
    fun `no hay edicion sin descarga previa ni charlas`() {
        assertFalse(ReglasPrograma.hayEdicion(descargadaAlgunaVez = false, charlasGuardadas = 0))
    }
}
