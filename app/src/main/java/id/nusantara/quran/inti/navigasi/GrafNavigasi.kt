package id.nusantara.quran.inti.navigasi

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import id.nusantara.quran.fitur.beranda.TampilanBeranda
import id.nusantara.quran.fitur.mushaf.TampilanMushaf
import id.nusantara.quran.fitur.pengaturan.TampilanPengaturan
import id.nusantara.quran.fitur.audio.TampilanAudio
import id.nusantara.quran.fitur.pencarian.TampilanPencarian
import id.nusantara.quran.fitur.bookmark.TampilanBookmark
import id.nusantara.quran.fitur.tematik.TampilanTematik

/** Navigasi utama empat ruang baca aplikasi. */
@Composable
fun GrafNavigasi(gelap: Boolean, onGantiTema: () -> Unit) {
    var tab by remember { mutableStateOf(0) }
    var ruang by remember { mutableStateOf("beranda") }
    val judul = listOf("Beranda", "Mushaf", "Audio", "Saya")
    Scaffold(bottomBar = {
        NavigationBar {
            val ikon = listOf(Icons.Rounded.Home, Icons.Rounded.Book, Icons.Rounded.GraphicEq, Icons.Rounded.Person)
            judul.forEachIndexed { indeks, nama ->
                NavigationBarItem(
                    selected = tab == indeks,
                    onClick = { tab = indeks; ruang = judul[indeks].lowercase() },
                    icon = { Icon(ikon[indeks], contentDescription = nama) },
                    label = { Text(nama) }
                )
            }
        }
    }) { bantalan ->
        when (ruang) {
            "beranda" -> TampilanBeranda(Modifier.padding(bantalan), bukaMushaf = { tab = 1; ruang = "mushaf" }, bukaAudio = { tab = 2; ruang = "audio" }, bukaPencarian = { ruang = "pencarian" }, bukaBookmark = { ruang = "bookmark" }, bukaTematik = { ruang = "tematik" })
            "mushaf" -> TampilanMushaf(Modifier.padding(bantalan))
            "audio" -> TampilanAudio(Modifier.padding(bantalan))
            "pencarian" -> TampilanPencarian(Modifier.padding(bantalan), onKembali = { ruang = "beranda" })
            "bookmark" -> TampilanBookmark(Modifier.padding(bantalan), onKembali = { ruang = "beranda" })
            "tematik" -> TampilanTematik(Modifier.padding(bantalan), onKembali = { ruang = "beranda" })
            else -> TampilanPengaturan(Modifier.padding(bantalan), gelap, onGantiTema)
        }
    }
}
