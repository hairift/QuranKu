package id.nusantara.quran.inti.util

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.data.lokal.entitas.EntitasRiwayat
import id.nusantara.quran.data.repositori.RepositoriQuran
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pengelola ekspor dan impor cadangan data pengguna dalam satu berkas JSON:
 * pengaturan aplikasi, bookmark beserta catatan, dan riwayat bacaan.
 */
@Singleton
class PengelolaCadangan @Inject constructor(
    @ApplicationContext private val konteks: Context,
    private val repositori: RepositoriQuran,
    private val pengaturan: PengaturanAplikasi,
) {

    /** Menulis seluruh data pengguna ke URI tujuan. Mengembalikan true bila berhasil. */
    suspend fun ekspor(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val akar = JSONObject()
                .put("aplikasi", "QuranKu")
                .put("versiEkspor", 1)
                .put("waktuEkspor", System.currentTimeMillis())

            // Preferensi pengguna.
            akar.put("pengaturan", JSONObject()
                .put("temaGelap", pengaturan.temaGelap.first())
                .put("idKota", pengaturan.idKota.first())
                .put("namaKota", pengaturan.namaKota.first())
                .put("latitud", pengaturan.latitud.first())
                .put("longitud", pengaturan.longitud.first())
                .put("pengingatAzan", pengaturan.pengingatAzan.first())
                .put("pengingatAyat", pengaturan.pengingatAyat.first())
                .put("jamPengingatAyat", pengaturan.jamPengingatAyat.first())
                .put("qari", pengaturan.qariPilihan.first())
                .put("surahTerakhir", pengaturan.surahTerakhir.first())
                .put("ayatTerakhir", pengaturan.ayatTerakhir.first())
                .put("ukuranArab", pengaturan.ukuranArab.first())
                .put("ukuranTerjemahan", pengaturan.ukuranTerjemahan.first())
                .put("bahasa", pengaturan.bahasa.first()))

            // Bookmark beserta catatannya.
            val larikBookmark = JSONArray()
            repositori.semuaBookmark().forEach { b ->
                larikBookmark.put(JSONObject()
                    .put("idAyatAwal", b.idAyatAwal)
                    .put("idAyatAkhir", b.idAyatAkhir ?: JSONObject.NULL)
                    .put("judulCatatan", b.judulCatatan)
                    .put("isiCatatan", b.isiCatatan)
                    .put("warnaTag", b.warnaTag)
                    .put("tanggalDibuat", b.tanggalDibuat))
            }
            akar.put("bookmark", larikBookmark)

            // Riwayat bacaan.
            val larikRiwayat = JSONArray()
            repositori.semuaRiwayat().forEach { r ->
                larikRiwayat.put(JSONObject()
                    .put("surah", r.surah)
                    .put("ayat", r.ayat)
                    .put("namaSurah", r.namaSurah)
                    .put("waktu", r.waktu))
            }
            akar.put("riwayat", larikRiwayat)

            konteks.contentResolver.openOutputStream(uri)?.use { aliran ->
                aliran.write(akar.toString(2).toByteArray(Charsets.UTF_8))
            } ?: error("Aliran keluaran tidak tersedia")
        }.isSuccess
    }

    /** Membaca berkas cadangan dari URI lalu menggabungkannya ke aplikasi. */
    suspend fun impor(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        runCatching {
            val teks = konteks.contentResolver.openInputStream(uri)?.use { aliran ->
                aliran.bufferedReader(Charsets.UTF_8).readText()
            } ?: error("Aliran masukan tidak tersedia")
            val akar = JSONObject(teks)
            require(akar.optString("aplikasi") == "QuranKu") { "Berkas bukan cadangan QuranKu" }

            // Pulihkan preferensi bila ada.
            akar.optJSONObject("pengaturan")?.let { p ->
                pengaturan.aturTemaGelap(p.optBoolean("temaGelap", false))
                pengaturan.aturKota(p.optString("idKota", "1301"), p.optString("namaKota", "Kota Jakarta"))
                pengaturan.aturKoordinat(p.optDouble("latitud", -6.2), p.optDouble("longitud", 106.8))
                pengaturan.aturPengingatAzan(p.optBoolean("pengingatAzan", false))
                pengaturan.aturPengingatAyat(p.optBoolean("pengingatAyat", false))
                pengaturan.aturJamPengingatAyat(p.optInt("jamPengingatAyat", 6))
                pengaturan.aturQari(p.optString("qari", "Alafasy_128kbps"))
                pengaturan.aturPosisiBaca(p.optInt("surahTerakhir", 1), p.optInt("ayatTerakhir", 1))
                pengaturan.aturUkuranTeks(p.optInt("ukuranArab", 30), p.optInt("ukuranTerjemahan", 16))
                pengaturan.aturBahasa(p.optString("bahasa", "id"))
            }

            // Ganti seluruh bookmark dengan isi cadangan agar tidak ganda.
            akar.optJSONArray("bookmark")?.let { larik ->
                repositori.bersihkanBookmark()
                for (indeks in 0 until larik.length()) {
                    val b = larik.getJSONObject(indeks)
                    repositori.simpanBookmark(
                        EntitasBookmark(
                            idAyatAwal = b.getInt("idAyatAwal"),
                            idAyatAkhir = if (b.isNull("idAyatAkhir")) null else b.getInt("idAyatAkhir"),
                            judulCatatan = b.optString("judulCatatan", ""),
                            isiCatatan = b.optString("isiCatatan", ""),
                            warnaTag = b.optString("warnaTag", "#C8A951"),
                            tanggalDibuat = b.optLong("tanggalDibuat", System.currentTimeMillis()),
                        ),
                    )
                }
            }

            // Gabungkan riwayat; kunci surah+ayat membuatnya bebas duplikat.
            akar.optJSONArray("riwayat")?.let { larik ->
                for (indeks in 0 until larik.length()) {
                    val r = larik.getJSONObject(indeks)
                    repositori.simpanRiwayat(
                        EntitasRiwayat(
                            surah = r.getInt("surah"),
                            ayat = r.getInt("ayat"),
                            namaSurah = r.optString("namaSurah", ""),
                            waktu = r.optLong("waktu", System.currentTimeMillis()),
                        ),
                    )
                }
            }
        }.isSuccess
    }
}
