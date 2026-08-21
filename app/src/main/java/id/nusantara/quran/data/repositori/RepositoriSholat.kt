package id.nusantara.quran.data.repositori

import id.nusantara.quran.data.remote.api.ApiJadwalSholat
import id.nusantara.quran.domain.model.ModelWaktuSholat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repositori jadwal sholat. Mengambil jadwal harian dari MyQuran (data Kemenag)
 * dengan cadangan jadwal statis Jakarta bila jaringan tidak tersedia.
 */
@Singleton
class RepositoriSholat @Inject constructor(private val api: ApiJadwalSholat) {

    /** Mengambil jadwal sholat satu kota untuk tanggal hari ini. */
    suspend fun jadwalHariIni(idKota: String): ModelWaktuSholat {
        val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        return runCatching {
            api.jadwal(idKota, tanggal).data?.jadwal?.let {
                ModelWaktuSholat(it.subuh, it.dzuhur, it.ashar, it.maghrib, it.isya)
            }
        }.getOrNull() ?: JADWAL_CADANGAN
    }

    /** Menghitung waktu sholat berikutnya beserta sisa menitnya. */
    fun sholatBerikutnya(waktu: ModelWaktuSholat): Triple<String, String, Long>? {
        val sekarang = System.currentTimeMillis()
        var terpilih: Triple<String, String, Long>? = null
        for ((nama, jam) in waktu.sebagaiDaftar()) {
            val milis = keMilis(jam) ?: continue
            if (milis > sekarang) {
                terpilih = Triple(nama, jam, (milis - sekarang) / 60_000L)
                break
            }
        }
        // Bila seluruh waktu hari ini sudah lewat, berarti berikutnya Subuh esok.
        if (terpilih == null) {
            val milisSubuh = keMilis(waktu.subuh)
            if (milisSubuh != null) {
                val sisa = (milisSubuh + 86_400_000L - sekarang) / 60_000L
                terpilih = Triple("Subuh", waktu.subuh, sisa)
            }
        }
        return terpilih
    }

    /** Mengubah teks "HH:mm" hari ini menjadi milidetik. */
    private fun keMilis(jam: String): Long? {
        val bagian = jam.split(":")
        if (bagian.size != 2) return null
        val kalender = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, bagian[0].toIntOrNull() ?: return null)
            set(java.util.Calendar.MINUTE, bagian[1].toIntOrNull() ?: return null)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return kalender.timeInMillis
    }

    companion object {
        /** Cadangan jadwal Jakarta bila perangkat sedang luring. */
        private val JADWAL_CADANGAN = ModelWaktuSholat("04:35", "12:04", "15:26", "18:01", "19:14")
    }
}
