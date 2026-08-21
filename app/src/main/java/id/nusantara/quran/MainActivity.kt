package id.nusantara.quran

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import id.nusantara.quran.inti.navigasi.GrafNavigasi
import id.nusantara.quran.inti.ui.tema.TemaNusantara

/** Aktivitas utama dengan satu alur Compose. */
class MainActivity : ComponentActivity() {
    override fun onCreate(keadaan: Bundle?) {
        super.onCreate(keadaan)
        setContent {
            var gelap by remember { mutableStateOf(false) }
            TemaNusantara(gelap = gelap) {
                Surface { GrafNavigasi(gelap = gelap, onGantiTema = { gelap = !gelap }) }
            }
        }
    }
}
