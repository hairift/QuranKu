package id.nusantara.quran.inti.navigasi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.outlined.GraphicEq
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import id.nusantara.quran.fitur.asmaulhusna.TampilanAsmaulHusna
import id.nusantara.quran.fitur.audio.TampilanAudio
import id.nusantara.quran.fitur.beranda.TampilanBeranda
import id.nusantara.quran.fitur.bookmark.TampilanBookmark
import id.nusantara.quran.fitur.dzikir.TampilanDzikir
import id.nusantara.quran.fitur.mushaf.TampilanMushaf
import id.nusantara.quran.fitur.nusantara.TampilanNusantara
import id.nusantara.quran.fitur.pencarian.TampilanPencarian
import id.nusantara.quran.fitur.pengaturan.TampilanPengaturan
import id.nusantara.quran.fitur.riwayat.TampilanRiwayat
import id.nusantara.quran.fitur.sains.TampilanSains
import id.nusantara.quran.fitur.tematik.TampilanTematik
import id.nusantara.quran.fitur.tentang.TampilanTentang
import id.nusantara.quran.fitur.topik.TampilanTopik
import id.nusantara.quran.fitur.unggulan.TampilanUnggulan

/**
 * Navigasi utama empat tab: Beranda, Mushaf, Audio, dan Saya.
 * Status tab aktif dan tidak aktif dibedakan secara tegas dengan indikator
 * zamrud pekat, ikon terisi vs garis luar, serta ketebalan tipografi.
 */
@Composable
fun GrafNavigasi() {
    var tab by remember { mutableIntStateOf(0) }
    var ruang by remember { mutableStateOf("beranda") }

    // Tujuan lompat mushaf: (surah, ayat); 0 berarti tidak ada tujuan.
    var surahTujuan by remember { mutableIntStateOf(0) }
    var ayatTujuan by remember { mutableIntStateOf(0) }

    /** Melompat langsung ke surah dan ayat tertentu di tab Mushaf. */
    fun lompatKeAyat(surah: Int, ayat: Int) {
        surahTujuan = surah
        ayatTujuan = ayat
        tab = 1
        ruang = "mushaf"
    }

    // Tangani tombol kembali perangkat saat berada di sub-layar.
    val diSubLayar = ruang !in listOf("beranda", "mushaf", "audio", "saya")
    BackHandler(enabled = diSubLayar || tab != 0) {
        when {
            ruang == "tentang" -> {
                tab = 3
                ruang = "saya"
            }
            diSubLayar -> {
                tab = 0
                ruang = "beranda"
            }
            tab != 0 -> {
                tab = 0
                ruang = "beranda"
            }
        }
    }

    val judul = listOf("Beranda", "Mushaf", "Audio", "Saya")
    val ikonAktif = listOf(
        Icons.Rounded.Home,
        Icons.AutoMirrored.Rounded.MenuBook,
        Icons.Rounded.GraphicEq,
        Icons.Rounded.Person,
    )
    val ikonNonaktif = listOf(
        Icons.Outlined.Home,
        Icons.AutoMirrored.Outlined.MenuBook,
        Icons.Outlined.GraphicEq,
        Icons.Outlined.Person,
    )

    Scaffold(
        bottomBar = {
            Column {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                )
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                ) {
                    judul.forEachIndexed { indeks, nama ->
                        val aktif = tab == indeks
                        NavigationBarItem(
                            selected = aktif,
                            onClick = {
                                tab = indeks
                                ruang = judul[indeks].lowercase()
                            },
                            icon = {
                                Icon(
                                    imageVector = if (aktif) ikonAktif[indeks] else ikonNonaktif[indeks],
                                    contentDescription = nama,
                                )
                            },
                            label = {
                                Text(
                                    text = nama,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (aktif) FontWeight.Bold else FontWeight.Medium,
                                    ),
                                )
                            },
                            alwaysShowLabel = true,
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.62f),
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f),
                            ),
                        )
                    }
                }
            }
        },
    ) { bantalan ->
        when (ruang) {
            "beranda" -> TampilanBeranda(
                Modifier.padding(bantalan),
                bukaMushafDi = { surah, ayat -> lompatKeAyat(surah, ayat) },
                bukaAudio = { tab = 2; ruang = "audio" },
                bukaPencarian = { ruang = "pencarian" },
                bukaBookmark = { ruang = "bookmark" },
                bukaTematik = { ruang = "tematik" },
                bukaNusantara = { ruang = "nusantara" },
                bukaRiwayat = { ruang = "riwayat" },
                bukaUnggulan = { ruang = "unggulan" },
                bukaSains = { ruang = "sains" },
                bukaTopik = { ruang = "topik" },
                bukaAsmaulHusna = { ruang = "asmaul_husna" },
                bukaDzikir = { ruang = "dzikir" },
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
            "riwayat" -> TampilanRiwayat(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "unggulan" -> TampilanUnggulan(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "sains" -> TampilanSains(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "topik" -> TampilanTopik(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "tematik" -> TampilanTematik(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
                bukaAyat = { surah, ayat -> lompatKeAyat(surah, ayat) },
            )
            "nusantara" -> TampilanNusantara(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
            )
            "asmaul_husna" -> TampilanAsmaulHusna(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
            )
            "dzikir" -> TampilanDzikir(
                Modifier.padding(bantalan),
                onKembali = { ruang = "beranda" },
            )
            "tentang" -> TampilanTentang(
                Modifier.padding(bantalan),
                onKembali = { tab = 3; ruang = "saya" },
            )
            else -> TampilanPengaturan(
                Modifier.padding(bantalan),
                bukaTentang = { ruang = "tentang" },
                bukaNusantara = { ruang = "nusantara" },
            )
        }
    }
}
