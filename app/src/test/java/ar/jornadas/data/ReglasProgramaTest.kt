package ar.jornadas.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReglasProgramaTest {
    private fun charla(id: String, titulo: String = "t$id", favorita: Boolean = false) =
        Charla(id, titulo, "Android", 1, "09:00", "Auditorio", "Ponente", "Resumen", favorita)

    private fun charlaDe(id: String, tema: String, dia: Int) =
        Charla(id, "t$id", tema, dia, "09:00", "Auditorio", "Ponente", "Resumen")

    private val catalogo = listOf(
        charlaDe("13", "Android", 2),
        charlaDe("23", "Datos", 2),
        charlaDe("24", "Datos", 2),
        charlaDe("21", "Datos", 1),
        charlaDe("31", "Calidad", 1)
    )

    @Test
    fun `dia 2 y tema Datos dejan dos resultados`() {
        val ids = ReglasPrograma.filtrar(catalogo, dia = 2, tema = "Datos").map { it.id }
        assertEquals(listOf("23", "24"), ids)
    }

    @Test
    fun `ambos dias y todos los temas no filtran nada`() {
        val resultado = ReglasPrograma.filtrar(catalogo, ReglasPrograma.AMBOS_DIAS, ReglasPrograma.TEMA_TODOS)
        assertEquals(catalogo, resultado)
    }

    @Test
    fun `solo tema filtra en ambos dias`() {
        val ids = ReglasPrograma.filtrar(catalogo, ReglasPrograma.AMBOS_DIAS, "Datos").map { it.id }
        assertEquals(listOf("23", "24", "21"), ids)
    }

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
