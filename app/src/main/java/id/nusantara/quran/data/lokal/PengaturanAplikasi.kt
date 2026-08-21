package id.nusantara.quran.data.lokal

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Penyimpanan preferensi aplikasi berbasis DataStore. */
private val Context.penyimpanan by preferencesDataStore(name = "pengaturan_quranku")

/**
 * Gerbang tunggal seluruh preferensi pengguna: tema, kota sholat,
 * koordinat kiblat, pengingat, qari, dan posisi baca terakhir.
 */
@Singleton
class PengaturanAplikasi @Inject constructor(@ApplicationContext private val konteks: Context) {

    companion object {
        private val KUNCI_GELAP = booleanPreferencesKey("tema_gelap")
        private val KUNCI_ID_KOTA = stringPreferencesKey("id_kota")
        private val KUNCI_NAMA_KOTA = stringPreferencesKey("nama_kota")
        private val KUNCI_LATITUD = doublePreferencesKey("latitud")
        private val KUNCI_LONGITUD = doublePreferencesKey("longitud")
        private val KUNCI_PENGINGAT_AZAN = booleanPreferencesKey("pengingat_azan")
        private val KUNCI_PENGINGAT_AYAT = booleanPreferencesKey("pengingat_ayat")
        private val KUNCI_JAM_PENGINGAT = intPreferencesKey("jam_pengingat_ayat")
        private val KUNCI_QARI = stringPreferencesKey("qari_pilihan")
        private val KUNCI_SURAH_TERAKHIR = intPreferencesKey("surah_terakhir")
        private val KUNCI_AYAT_TERAKHIR = intPreferencesKey("ayat_terakhir")
        private val KUNCI_UKURAN_ARAB = intPreferencesKey("ukuran_arab")
        private val KUNCI_UKURAN_TERJEMAHAN = intPreferencesKey("ukuran_terjemahan")
        private val KUNCI_BAHASA = stringPreferencesKey("bahasa_aplikasi")
    }

    val temaGelap: Flow<Boolean> = konteks.penyimpanan.data.map { it[KUNCI_GELAP] ?: false }
    val idKota: Flow<String> = konteks.penyimpanan.data.map { it[KUNCI_ID_KOTA] ?: "1301" }
    val namaKota: Flow<String> = konteks.penyimpanan.data.map { it[KUNCI_NAMA_KOTA] ?: "Kota Jakarta" }
    val latitud: Flow<Double> = konteks.penyimpanan.data.map { it[KUNCI_LATITUD] ?: -6.2 }
    val longitud: Flow<Double> = konteks.penyimpanan.data.map { it[KUNCI_LONGITUD] ?: 106.8 }
    val pengingatAzan: Flow<Boolean> = konteks.penyimpanan.data.map { it[KUNCI_PENGINGAT_AZAN] ?: false }
    val pengingatAyat: Flow<Boolean> = konteks.penyimpanan.data.map { it[KUNCI_PENGINGAT_AYAT] ?: false }
    val jamPengingatAyat: Flow<Int> = konteks.penyimpanan.data.map { it[KUNCI_JAM_PENGINGAT] ?: 6 }
    val qariPilihan: Flow<String> = konteks.penyimpanan.data.map { it[KUNCI_QARI] ?: "Alafasy_128kbps" }
    val surahTerakhir: Flow<Int> = konteks.penyimpanan.data.map { it[KUNCI_SURAH_TERAKHIR] ?: 1 }
    val ayatTerakhir: Flow<Int> = konteks.penyimpanan.data.map { it[KUNCI_AYAT_TERAKHIR] ?: 1 }
    val ukuranArab: Flow<Int> = konteks.penyimpanan.data.map { it[KUNCI_UKURAN_ARAB] ?: 30 }
    val ukuranTerjemahan: Flow<Int> = konteks.penyimpanan.data.map { it[KUNCI_UKURAN_TERJEMAHAN] ?: 16 }

    /** Kode bahasa aplikasi: "id" (bawaan), "en", atau "ar". */
    val bahasa: Flow<String> = konteks.penyimpanan.data.map { it[KUNCI_BAHASA] ?: "id" }

    suspend fun aturTemaGelap(gelap: Boolean) = konteks.penyimpanan.edit { it[KUNCI_GELAP] = gelap }

    suspend fun aturKota(id: String, nama: String) = konteks.penyimpanan.edit {
        it[KUNCI_ID_KOTA] = id
        it[KUNCI_NAMA_KOTA] = nama
    }

    suspend fun aturKoordinat(lat: Double, lng: Double) = konteks.penyimpanan.edit {
        it[KUNCI_LATITUD] = lat
        it[KUNCI_LONGITUD] = lng
    }

    suspend fun aturPengingatAzan(aktif: Boolean) = konteks.penyimpanan.edit { it[KUNCI_PENGINGAT_AZAN] = aktif }
    suspend fun aturPengingatAyat(aktif: Boolean) = konteks.penyimpanan.edit { it[KUNCI_PENGINGAT_AYAT] = aktif }
    suspend fun aturJamPengingatAyat(jam: Int) = konteks.penyimpanan.edit { it[KUNCI_JAM_PENGINGAT] = jam }
    suspend fun aturQari(idQari: String) = konteks.penyimpanan.edit { it[KUNCI_QARI] = idQari }

    suspend fun aturPosisiBaca(surah: Int, ayat: Int) = konteks.penyimpanan.edit {
        it[KUNCI_SURAH_TERAKHIR] = surah
        it[KUNCI_AYAT_TERAKHIR] = ayat
    }

    suspend fun aturUkuranTeks(arab: Int, terjemahan: Int) = konteks.penyimpanan.edit {
        it[KUNCI_UKURAN_ARAB] = arab.coerceIn(22, 44)
        it[KUNCI_UKURAN_TERJEMAHAN] = terjemahan.coerceIn(12, 24)
    }

    suspend fun aturBahasa(kode: String) = konteks.penyimpanan.edit { it[KUNCI_BAHASA] = kode }
}
