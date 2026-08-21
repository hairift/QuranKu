package id.nusantara.quran.fitur.pengingat

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.inti.util.PembantuNotifikasi
import java.util.Calendar
import java.util.concurrent.TimeUnit

/**
 * Pekerja latar yang mengirim ayat harian lewat notifikasi.
 * Dijadwalkan berkala dengan WorkManager pada jam pilihan pengguna.
 */
class PekerjaAyatHarian(
    konteks: Context,
    parameter: WorkerParameters,
) : CoroutineWorker(konteks, parameter) {

    override suspend fun doWork(): Result {
        val ayat = SumberDataLokal(applicationContext).ayatHarian() ?: return Result.success()
        PembantuNotifikasi.tampilkan(
            konteks = applicationContext,
            kanal = PembantuNotifikasi.KANAL_AYAT_HARIAN,
            idNotifikasi = ID_NOTIFIKASI,
            judul = "Ayat Hari Ini",
            isi = "${ayat.terjemahan} (QS ${ayat.surah}:${ayat.nomor})",
        )
        return Result.success()
    }

    companion object {
        private const val NAMA_PEKERJAAN = "pengingat_ayat_harian"
        private const val ID_NOTIFIKASI = 9001

        /** Mengaktifkan pengingat harian pada jam tertentu (0-23). */
        fun aktifkan(konteks: Context, jam: Int) {
            val sekarang = Calendar.getInstance()
            val sasaran = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, jam.coerceIn(0, 23))
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }
            if (sasaran.before(sekarang)) sasaran.add(Calendar.DAY_OF_YEAR, 1)
            val tundaAwal = sasaran.timeInMillis - sekarang.timeInMillis
            val permintaan = PeriodicWorkRequestBuilder<PekerjaAyatHarian>(1, TimeUnit.DAYS)
                .setInitialDelay(tundaAwal, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(konteks).enqueueUniquePeriodicWork(
                NAMA_PEKERJAAN, ExistingPeriodicWorkPolicy.UPDATE, permintaan,
            )
        }

        /** Mematikan pengingat harian. */
        fun matikan(konteks: Context) {
            WorkManager.getInstance(konteks).cancelUniqueWork(NAMA_PEKERJAAN)
        }
    }
}
