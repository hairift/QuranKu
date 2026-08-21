package id.nusantara.quran.data.lokal

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelButirTematik
import id.nusantara.quran.domain.model.ModelKategoriTematik
import id.nusantara.quran.domain.model.ModelKota
import id.nusantara.quran.domain.model.ModelSurah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pemuat berkas JSON di folder assets dengan cache di memori.
 * Seluruh bacaan berat dikerjakan di thread IO dan hanya diparsing sekali.
 */
@Singleton
class SumberDataLokal @Inject constructor(@ApplicationContext private val konteks: Context) {

    private val kunci = Mutex()
    private var cacheAyat: List<ModelAyat>? = null
    private var cacheSurah: List<ModelSurah>? = null
    private var cacheKota: List<ModelKota>? = null
    private var cacheTematik: List<ModelKategoriTematik>? = null

    /** Membaca isi berkas dari assets sebagai teks. */
    private fun bacaAset(nama: String): String =
        konteks.assets.open("data-lokal/$nama").bufferedReader(Charsets.UTF_8).use { it.readText() }

    /** Seluruh 6.236 ayat beserta teks Arab, terjemahan, dan nomor juz. */
    suspend fun semuaAyat(): List<ModelAyat> = withContext(Dispatchers.IO) {
        cacheAyat ?: kunci.withLock {
            cacheAyat ?: run {
                val larik = JSONObject(bacaAset("quran_lokal.json")).getJSONArray("ayat")
                val daftar = ArrayList<ModelAyat>(larik.length())
                for (indeks in 0 until larik.length()) {
                    val butir = larik.getJSONObject(indeks)
                    val surah = butir.getInt("sura")
                    val nomor = butir.getInt("aya")
                    daftar += ModelAyat(
                        id = surah * 1000 + nomor,
                        surah = surah,
                        nomor = nomor,
                        arab = butir.getString("arabic_text"),
                        terjemahan = butir.getString("translation"),
                        catatanKaki = butir.optString("footnotes", ""),
                        juz = butir.optInt("juz", 1),
                    )
                }
                cacheAyat = daftar
                daftar
            }
        }
    }

    /** Metadata 114 surah. */
    suspend fun daftarSurah(): List<ModelSurah> = withContext(Dispatchers.IO) {
        cacheSurah ?: kunci.withLock {
            cacheSurah ?: run {
                val larik = org.json.JSONArray(bacaAset("surah_info.json"))
                val daftar = ArrayList<ModelSurah>(larik.length())
                for (indeks in 0 until larik.length()) {
                    val butir = larik.getJSONObject(indeks)
                    daftar += ModelSurah(
                        nomor = butir.getInt("nomor"),
                        namaArab = butir.getString("namaArab"),
                        namaLatin = butir.getString("namaLatin"),
                        arti = butir.getString("arti"),
                        jumlahAyat = butir.getInt("jumlahAyat"),
                        tipe = butir.getString("tipe"),
                    )
                }
                cacheSurah = daftar
                daftar
            }
        }
    }

    /** Ayat-ayat milik satu surah, diurutkan sesuai nomor ayat. */
    suspend fun ayatSurah(nomorSurah: Int): List<ModelAyat> =
        semuaAyat().filter { it.surah == nomorSurah }

    /** Satu ayat berdasarkan pasangan surah dan nomor ayat. */
    suspend fun ayatTunggal(surah: Int, nomor: Int): ModelAyat? =
        semuaAyat().firstOrNull { it.surah == surah && it.nomor == nomor }

    /** Daftar 518 kota/kabupaten Indonesia untuk jadwal sholat. */
    suspend fun daftarKota(): List<ModelKota> = withContext(Dispatchers.IO) {
        cacheKota ?: kunci.withLock {
            cacheKota ?: run {
                val larik = org.json.JSONArray(bacaAset("kota.json"))
                val daftar = ArrayList<ModelKota>(larik.length())
                for (indeks in 0 until larik.length()) {
                    val butir = larik.getJSONObject(indeks)
                    daftar += ModelKota(id = butir.getString("id"), nama = butir.getString("nama"))
                }
                cacheKota = daftar
                daftar
            }
        }
    }

    /** Konten tematik: doa, solusi, adab, dosa besar, dan para nabi. */
    suspend fun daftarTematik(): List<ModelKategoriTematik> = withContext(Dispatchers.IO) {
        cacheTematik ?: kunci.withLock {
            cacheTematik ?: run {
                val larikKategori = JSONObject(bacaAset("tematik.json")).getJSONArray("kategori")
                val daftar = ArrayList<ModelKategoriTematik>(larikKategori.length())
                for (indeks in 0 until larikKategori.length()) {
                    val butir = larikKategori.getJSONObject(indeks)
                    val larikItem = butir.getJSONArray("item")
                    val item = ArrayList<ModelButirTematik>(larikItem.length())
                    for (urutan in 0 until larikItem.length()) {
                        val butirItem = larikItem.getJSONObject(urutan)
                        val rujukan = ArrayList<Pair<Int, Int>>()
                        val larikAyat = butirItem.getJSONArray("ayat")
                        for (posisi in 0 until larikAyat.length()) {
                            val pasangan = larikAyat.getJSONArray(posisi)
                            rujukan += pasangan.getInt(0) to pasangan.getInt(1)
                        }
                        item += ModelButirTematik(judul = butirItem.getString("judul"), rujukan = rujukan)
                    }
                    daftar += ModelKategoriTematik(
                        id = butir.getString("id"),
                        judul = butir.getString("judul"),
                        deskripsi = butir.getString("deskripsi"),
                        item = item,
                    )
                }
                cacheTematik = daftar
                daftar
            }
        }
    }

    /** Ayat harian yang berganti setiap hari secara deterministik. */
    suspend fun ayatHarian(): ModelAyat? {
        val daftar = semuaAyat()
        if (daftar.isEmpty()) return null
        val hari = (System.currentTimeMillis() / 86_400_000L).toInt()
        return daftar[hari % daftar.size]
    }
}
