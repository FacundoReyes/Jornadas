package ar.jornadas.data

import androidx.room.*
import retrofit2.http.GET
import kotlinx.coroutines.flow.Flow

data class FichaRemota(val id: String, val title: String, val track: String, val day: Int,
    val time: String, val room: String, val speaker: String, val summary: String)

interface ProgramaApi {
    @GET("programa") suspend fun consultar(): List<FichaRemota>
}

@Entity(tableName = "charlas")
data class Charla(@PrimaryKey val id: String, val titulo: String, val tema: String, val dia: Int,
    val hora: String, val sala: String, val ponente: String, val resumen: String,
    val favorita: Boolean = false)

@Dao
abstract class CarteleraDao {
    @Query("SELECT * FROM charlas ORDER BY dia, hora, sala")
    abstract fun observar(): Flow<List<Charla>>

    @Query("SELECT COUNT(*) FROM charlas")
    abstract suspend fun cantidad(): Int

    @Query("SELECT id FROM charlas WHERE favorita = 1")
    protected abstract suspend fun marcadas(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun guardar(fichas: List<Charla>)

    @Query("DELETE FROM charlas WHERE id NOT IN (:vigentes)")
    protected abstract suspend fun retirarFueraDe(vigentes: List<String>)

    @Query("DELETE FROM charlas")
    protected abstract suspend fun vaciar()

    @Transaction
    open suspend fun reemplazarPrograma(fichas: List<Charla>) {
        if (fichas.isEmpty()) {
            vaciar()
            return
        }
        val marcadas = marcadas().toSet()
        guardar(ReglasPrograma.conservarMarcas(fichas, marcadas))
        retirarFueraDe(fichas.map { it.id })
    }

    @Query("UPDATE charlas SET favorita = NOT favorita WHERE id = :id")
    abstract suspend fun alternar(id: String)
}

@Database(entities = [Charla::class], version = 1, exportSchema = false)
abstract class Archivo : RoomDatabase() {
    abstract fun cartelera(): CarteleraDao
}
