package id.nusantara.quran

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.HiltAndroidApp
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.inti.util.PembantuNotifikasi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/** Titik masuk aplikasi QuranKu. */
@HiltAndroidApp
class AplikasiNusantara : Application() {

    @Inject
    lateinit var pengaturan: PengaturanAplikasi

    override fun onCreate() {
        super.onCreate()
        // Kanal notifikasi dibuat sejak awal agar suara azan ikut terdaftar.
        PembantuNotifikasi.daftarkanKanal(this)
        // Terapkan kembali bahasa pilihan pengguna pada Android di bawah 13,
        // karena per-app locale bawaan sistem baru ada sejak Android 13.
        val kodeBahasa = runBlocking { pengaturan.bahasa.first() }
        if (kodeBahasa != "id") {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(kodeBahasa))
        }
    }
}
