package id.nusantara.quran

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.inti.navigasi.GrafNavigasi
import id.nusantara.quran.inti.ui.tema.TemaNusantara
import javax.inject.Inject

/** Aktivitas utama dengan satu alur Compose; tema mengikuti preferensi pengguna. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var pengaturan: PengaturanAplikasi

    override fun onCreate(keadaan: Bundle?) {
        super.onCreate(keadaan)
        setContent {
            val gelap by pengaturan.temaGelap.collectAsState(initial = false)
            TemaNusantara(gelap = gelap) {
                Surface { GrafNavigasi() }
            }
        }
    }
}
