package id.nusantara.quran.fitur.sholat

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.remote.api.ApiJadwalSholat
import id.nusantara.quran.domain.model.ModelWaktuSholat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Penjadwal notifikasi azan memakai AlarmManager.
 * Alarm dipasang per waktu sholat dan dipasang ulang setiap kali berbunyi
 * atau setiap kali pengguna mengganti kota/menyalakan pengingat.
 */
object PenjadwalAzan {
    const val TAMBAHAN_NAMA_WAKTU = "nama_waktu_sholat"
    private const val KODE_DASAR = 4000

    /** Memasang alarm untuk seluruh waktu sholat hari ini dan esok. */
    fun jadwalkan(konteks: Context, waktu: ModelWaktuSholat) {
        val pengelola = konteks.getSystemService(AlarmManager::class.java) ?: return
        waktu.sebagaiDaftar().forEachIndexed { indeks, (nama, jam) ->
            val waktuPicu = waktuBerikutnya(jam) ?: return@forEachIndexed
            val niat = Intent(konteks, PenerimaAzan::class.java).apply {
                putExtra(TAMBAHAN_NAMA_WAKTU, nama)
            }
            val pending = PendingIntent.getBroadcast(
                konteks, KODE_DASAR + indeks, niat,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            // Alarm inexact agar hemat baterai dan tidak butuh izin khusus.
            pengelola.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, waktuPicu, pending)
        }
    }

    /** Membatalkan seluruh alarm azan yang pernah dipasang. */
    fun batalkan(konteks: Context) {
        val pengelola = konteks.getSystemService(AlarmManager::class.java) ?: return
        val daftarNama = listOf("Subuh", "Dzuhur", "Ashar", "Maghrib", "Isya")
        daftarNama.forEachIndexed { indeks, nama ->
            val niat = Intent(konteks, PenerimaAzan::class.java).apply {
                putExtra(TAMBAHAN_NAMA_WAKTU, nama)
            }
            val pending = PendingIntent.getBroadcast(
                konteks, KODE_DASAR + indeks, niat,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            pengelola.cancel(pending)
        }
    }

    /**
     * Membaca kota tersimpan lalu menjadwalkan ulang alarm berdasarkan
     * jadwal terbaru dari layanan MyQuran. Dipanggil dari penerima alarm.
     */
    fun jadwalkanUlangDariPreferensi(konteks: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val preferensi = PengaturanAplikasi(konteks)
            if (!preferensi.pengingatAzan.first()) return@launch
            val idKota = preferensi.idKota.first()
            val tanggal = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            val waktu = runCatching {
                Retrofit.Builder()
                    .baseUrl("https://api.myquran.com/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                    .create(ApiJadwalSholat::class.java)
                    .jadwal(idKota, tanggal).data?.jadwal
            }.getOrNull() ?: return@launch
            jadwalkan(
                konteks,
                ModelWaktuSholat(waktu.subuh, waktu.dzuhur, waktu.ashar, waktu.maghrib, waktu.isya),
            )
        }
    }

    /** Mengubah teks "HH:mm" menjadi milidetik waktu berikutnya (hari ini atau esok). */
    private fun waktuBerikutnya(jam: String): Long? {
        val bagian = jam.split(":")
        if (bagian.size != 2) return null
        val jamInt = bagian[0].toIntOrNull() ?: return null
        val menitInt = bagian[1].toIntOrNull() ?: return null
        val kalender = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, jamInt)
            set(Calendar.MINUTE, menitInt)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (kalender.timeInMillis <= System.currentTimeMillis()) {
            kalender.add(Calendar.DAY_OF_YEAR, 1)
        }
        return kalender.timeInMillis
    }
}
