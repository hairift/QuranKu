package id.nusantara.quran.fitur.beranda

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Topic
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Beranda QuranKu: ayat hari ini, lanjutkan baca, hitung mundur sholat,
 * fitur disarankan, bacaan unggulan, dan riwayat bacaan.
 */
@Composable
fun TampilanBeranda(
    modifier: Modifier = Modifier,
    bukaMushafDi: (Int, Int) -> Unit = { _, _ -> },
    bukaAudio: () -> Unit = {},
    bukaPencarian: () -> Unit = {},
    bukaBookmark: () -> Unit = {},
    bukaTematik: () -> Unit = {},
    bukaNusantara: () -> Unit = {},
    bukaRiwayat: () -> Unit = {},
    bukaUnggulan: () -> Unit = {},
    bukaSains: () -> Unit = {},
    bukaTopik: () -> Unit = {},
    model: ModelTampilanBeranda = hiltViewModel(),
) {
    val ayatHarian by model.ayatHarian.collectAsState()
    val surahAyatHarian by model.surahAyatHarian.collectAsState()
    val posisiBaca by model.posisiBaca.collectAsState()
    val sholatBerikutnya by model.sholatBerikutnya.collectAsState()
    val namaKota by model.namaKota.collectAsState()
    val riwayatTerakhir by model.riwayatTerakhir.collectAsState()
    val unggulan by model.unggulan.collectAsState()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text("Assalamu'alaikum", style = MaterialTheme.typography.titleMedium)
        Text(
            "Ruang teduh untuk membaca",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 4.dp),
        )

        // Kartu ayat hari ini dari data lokal, berganti setiap hari.
        Spacer(Modifier.height(20.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { ayatHarian?.let { bukaMushafDi(it.surah, it.nomor) } },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Column(Modifier.padding(22.dp)) {
                Text(
                    "AYAT HARI INI",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    ayatHarian?.arab ?: "...",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = KeluargaHurufArab,
                    fontSize = 26.sp,
                    lineHeight = 44.sp,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    ayatHarian?.terjemahan ?: "Memuat ayat pilihan hari ini…",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                val rujukan = ayatHarian?.let { "QS. ${surahAyatHarian?.namaLatin ?: ""}: ${it.nomor}" } ?: ""
                Text(
                    rujukan,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        }

        // Fitur disarankan: pintasan geser ke seluruh fitur utama.
        Spacer(Modifier.height(22.dp))
        Text("Fitur Disarankan", style = MaterialTheme.typography.titleMedium)
        LazyRow(
            Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { KartuDisarankan(Icons.Rounded.WorkspacePremium, "Bacaan Unggulan", bukaUnggulan) }
            item { KartuDisarankan(Icons.Rounded.Science, "Quran & Sains", bukaSains) }
            item { KartuDisarankan(Icons.Rounded.Topic, "Penjelajah Topik", bukaTopik) }
            item { KartuDisarankan(Icons.Rounded.Mic, "Cari Suara", bukaPencarian) }
            item { KartuDisarankan(Icons.Rounded.History, "Riwayat Bacaan", bukaRiwayat) }
            item { KartuDisarankan(Icons.Rounded.AutoStories, "Tematik", bukaTematik) }
            item { KartuDisarankan(Icons.Rounded.Explore, "Kiblat", bukaNusantara) }
            item { KartuDisarankan(Icons.Rounded.GraphicEq, "Murottal", bukaAudio) }
        }

        // Kartu lanjutkan membaca dari posisi terakhir yang tersimpan.
        Spacer(Modifier.height(18.dp))
        Card(
            Modifier.fillMaxWidth().clickable { bukaMushafDi(posisiBaca.first, posisiBaca.second) },
            shape = RoundedCornerShape(20.dp),
        ) {
            Row(
                Modifier.fillMaxWidth().padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(Modifier.weight(1f)) {
                    Text("Lanjutkan membaca", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${posisiBaca.third} · Ayat ${posisiBaca.second}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Icon(
                    Icons.AutoMirrored.Rounded.MenuBook,
                    contentDescription = "Lanjutkan membaca",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        // Kartu waktu sholat berikutnya dengan hitung mundur.
        sholatBerikutnya?.let { (nama, jam, sisaMenit) ->
            Spacer(Modifier.height(14.dp))
            Card(
                Modifier.fillMaxWidth().clickable(onClick = bukaNusantara),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Menuju $nama · $jam",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            "${sisaMenit / 60} jam ${sisaMenit % 60} menit lagi · $namaKota",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                    Icon(
                        Icons.Rounded.Schedule,
                        contentDescription = "Jadwal sholat",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        // Bacaan unggulan: surah/ayat pilihan untuk waktu terbaik.
        if (unggulan.isNotEmpty()) {
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Bacaan Unggulan", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                TextButton(onClick = bukaUnggulan) { Text("Lihat semua") }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(unggulan, key = { it.id }) { bacaan ->
                    Card(
                        Modifier
                            .width(200.dp)
                            .clickable { bukaMushafDi(bacaan.surah, if (bacaan.ayat > 0) bacaan.ayat else 1) },
                        shape = RoundedCornerShape(18.dp),
                    ) {
                        Column(Modifier.fillMaxWidth().padding(14.dp)) {
                            Text(bacaan.judul, style = MaterialTheme.typography.titleSmall)
                            Text(
                                bacaan.rekomendasi,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                        }
                    }
                }
            }
        }

        // Riwayat bacaan terakhir.
        if (riwayatTerakhir.isNotEmpty()) {
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Riwayat Bacaan", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                TextButton(onClick = bukaRiwayat) { Text("Lihat semua") }
            }
            Card(Modifier.fillMaxWidth().padding(top = 4.dp), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(vertical = 6.dp)) {
                    riwayatTerakhir.forEach { butir ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { bukaMushafDi(butir.surah, butir.ayat) }
                                .padding(horizontal = 18.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Rounded.History,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                            )
                            Text(
                                "QS ${butir.namaSurah.ifBlank { butir.surah.toString() }}: ${butir.ayat}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 12.dp),
                            )
                        }
                    }
                }
            }
        }

        // Pintasan pencarian dan bookmark.
        Spacer(Modifier.height(22.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OutlinedButton(onClick = bukaPencarian, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.Search, contentDescription = null)
                Text("  Cari Ayat")
            }
            OutlinedButton(onClick = bukaBookmark, modifier = Modifier.weight(1f)) {
                Icon(Icons.Rounded.Bookmark, contentDescription = null)
                Text("  Bookmark")
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

/** Kartu kecil pada baris Fitur Disarankan. */
@Composable
private fun KartuDisarankan(ikon: ImageVector, label: String, aksi: () -> Unit) {
    Card(
        Modifier.width(104.dp).clickable(onClick = aksi),
        shape = RoundedCornerShape(18.dp),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(ikon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
