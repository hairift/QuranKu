package id.nusantara.quran.domain.usecase

import id.nusantara.quran.data.lokal.entitas.EntitasAyat
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.data.repositori.RepositoriQuran
import javax.inject.Inject

/** Use case daftar surah. */
class AmbilDaftarSurah @Inject constructor(private val repositori: RepositoriQuran) { operator fun invoke() = repositori.daftarSurah() }
/** Use case detail ayat satu surah. */
class AmbilDetailAyat @Inject constructor(private val repositori: RepositoriQuran) { suspend operator fun invoke(nomor: Int): List<EntitasAyat> = repositori.detailAyat(nomor) }
/** Use case pencarian Arab secara lokal. */
class CariAyat @Inject constructor(private val repositori: RepositoriQuran) { suspend operator fun invoke(kata: String) = repositori.cari(kata) }
/** Use case pencatatan bookmark. */
class KelolaBookmark @Inject constructor(private val repositori: RepositoriQuran) { suspend fun simpan(data: EntitasBookmark) = repositori.simpanBookmark(data); fun daftar() = repositori.bookmark() }
/** Use case pemutaran yang menjadi titik integrasi Media3. */
class PutarAudio @Inject constructor() { fun alamat(qari: String, surah: Int, ayat: Int): String = "https://everyayah.com/data/$qari/${surah.toString().padStart(3, '0')}${ayat.toString().padStart(3, '0')}.mp3" }
/** Penjadwal pengingat harian disiapkan untuk dihubungkan ke WorkManager. */
class AturPengingatHarian @Inject constructor() { fun jamValid(jam: Int, menit: Int) = jam in 0..23 && menit in 0..59 }
