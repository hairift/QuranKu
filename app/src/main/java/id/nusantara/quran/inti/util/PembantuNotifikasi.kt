package id.nusantara.quran.inti.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

/**
 * Pembantu pembuatan kanal dan pengiriman notifikasi.
 * Seluruh notifikasi QuranKu memakai ikon aplikasi dan membuka layar utama saat disentuh.
 */
object PembantuNotifikasi {
    const val KANAL_AZAN = "kanal_azan"
    const val KANAL_AYAT_HARIAN = "kanal_ayat_harian"

    /** Mendaftarkan kanal notifikasi; aman dipanggil berulang kali. */
    fun daftarkanKanal(konteks: Context) {
        val pengelola = konteks.getSystemService(NotificationManager::class.java) ?: return
        val kanalAzan = NotificationChannel(
            KANAL_AZAN, "Pengingat Azan", NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Notifikasi saat waktu sholat tiba" }
        val kanalAyat = NotificationChannel(
            KANAL_AYAT_HARIAN, "Ayat Harian", NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "Pengingat ayat pilihan setiap hari" }
        pengelola.createNotificationChannels(listOf(kanalAzan, kanalAyat))
    }

    /** Memeriksa izin notifikasi pada Android 13 ke atas; di bawahnya selalu diizinkan. */
    fun izinNotifikasiAda(konteks: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(konteks, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

    /** Menampilkan notifikasi sederhana yang membuka aplikasi saat disentuh. */
    fun tampilkan(konteks: Context, kanal: String, idNotifikasi: Int, judul: String, isi: String) {
        if (!izinNotifikasiAda(konteks)) return
        daftarkanKanal(konteks)
        val niatBuka = konteks.packageManager.getLaunchIntentForPackage(konteks.packageName)?.let {
            PendingIntent.getActivity(
                konteks, idNotifikasi, it,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
        val notifikasi = NotificationCompat.Builder(konteks, kanal)
            .setSmallIcon(konteks.applicationInfo.icon)
            .setContentTitle(judul)
            .setContentText(isi)
            .setStyle(NotificationCompat.BigTextStyle().bigText(isi))
            .setAutoCancel(true)
            .apply { niatBuka?.let { setContentIntent(it) } }
            .build()
        runCatching { NotificationManagerCompat.from(konteks).notify(idNotifikasi, notifikasi) }
    }
}
