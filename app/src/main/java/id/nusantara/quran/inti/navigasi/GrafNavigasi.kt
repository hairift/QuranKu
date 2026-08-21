package id.nusantara.quran.inti.navigasi

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import id.nusantara.quran.fitur.audio.TampilanAudio
import id.nusantara.quran.fitur.beranda.TampilanBeranda
import id.nusantara.quran.fitur.bookmark.TampilanBookmark
import id.nusantara.quran.fitur.mushaf.TampilanMushaf
import id.nusantara.quran.fitur.nusantara.TampilanNusantara
import id.nusantara.quran.fitur.pencarian.TampilanPencarian
import id.nusantara.quran.fitur.pengaturan.TampilanPengaturan
import id.nusantara.quran.fitur.tematik.TampilanTematik

/**
 * Navigasi utama empat tab: Beranda, Mushaf, Audio, dan Saya.
 * Antar-layar seperti pencarian, bookmark, dan tematik dapat
 * melompat langsung ke ayat tertentu di mushaf.
 */
@Composable
fun GrafNavigasi(gelap: Boolean, onGantiTema: () -> Unit) {
    var tab by remember { mutableIntStateOf(0) }
    var ruang by remember { mutableStateOf("beranda") }

    // Tujuan lompat mushaf: (surah, ayat); 0 berarti tidak ada tujuan.
    var surahTujuan by remember { mutableIntStateOf(0) }
    var ayatTujuan by remember { mutableIntStateOf(0) }

    /** Melompat ke ayat tertentu di tab mushaf. */
    fun lompatKeAyat(surah: Int, ayat: Int) {
        surahTujuan = surah
        ayatTujuan = ayat
        tab = 1
        ruang = "mushaf"
    }

    val judul = listOf("Beranda", "Mushaf", "Audio", "Saya")
    val ikon = listOf(
        Icons.Rounded.Home,
        Icons.Rounded.Book,
        Icons.Rounded.GraphicEq,
        Icons.Rounded.Person,
    )

    Scaffold(bottomBar = {
        NavigationBar {
            judul.forEachIndexed { indeks, nama ->
                NavigationBarItem(
                    selected = tab == indeks,
                    onClick = { tab = indeks; ruang = judul[indeks].lowercase() },
                    icon = { Icon(ikon[indeks], contentDescription = nama) },
                    label = { Text(nama) },
                )
            }
        }
    }) { bantalan ->
        when (ruang) {
            "beranda" -> TampilanBeranda(
                Modifier.padding(bantalan),
                bukaMushaf = { tab = 1; ruang = "mushaf" },
                bukaMushafDi = { surah, ayat -> lompatKeAyat(surah, ayat) },
                bukaAudio = { tab = 2; ruang = "audio" },
                bukaPencarian = { ruang = "pencarian" },
                bukaBookmark = { ruang = "bookmark" },
                bukaTematik = { ruang = "tematik" },
                bukaNusantara = { ruang = "nusantara" },
            )
            "mushaf" -> TampilanMushaf(
                Modifier.padding(bantalan),
                surahTujuan = surahTujuan,
                ayatTujuan = ayatTujuan,
                onTujuanSelesai = { surahTujuan = 0; ayatTujuan = 0 },
            )
            "audio" -> TampilanAudio(Modifier.padding(bantalan))
            "pencarian" -> TampilanPencarian(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "bookmark" -> TampilanBookmark(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "tematik" -> TampilanTematik(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
            )
            "nusantara" -> TampilanNusantara(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
            )
            else -> TampilanPengaturan(Modifier.padding(bantalan), gelap, onGantiTema)
        }
    }
}
