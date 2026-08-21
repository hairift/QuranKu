package id.nusantara.quran.data.repositori

import id.nusantara.quran.data.lokal.dao.DaoQuran
import id.nusantara.quran.data.lokal.entitas.EntitasAyat
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.data.lokal.entitas.EntitasSurah
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Repositori yang mengutamakan Room lalu siap diperluas ke sumber remote. */
class RepositoriQuran @Inject constructor(private val dao: DaoQuran) {
    fun daftarSurah(): Flow<List<EntitasSurah>> = dao.alurSurah()
    suspend fun detailAyat(nomor: Int): List<EntitasAyat> = dao.ayatSurah(nomor)
    suspend fun cari(kata: String): List<EntitasAyat> = dao.cariAyat(kata)
    fun bookmark(): Flow<List<EntitasBookmark>> = dao.alurBookmark()
    suspend fun simpanBookmark(data: EntitasBookmark) = dao.simpanBookmark(data)
}
