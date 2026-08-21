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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
 * Beranda QuranKu: sapaan, ayat harian nyata, lanjutkan baca,
 * hitung mundur sholat, dan pintasan seluruh fitur.
 */
@Composable
fun TampilanBeranda(
    modifier: Modifier = Modifier,
    bukaMushaf: () -> Unit = {},
    bukaMushafDi: (Int, Int) -> Unit = { _, _ -> },
    bukaAudio: () -> Unit = {},
    bukaPencarian: () -> Unit = {},
    bukaBookmark: () -> Unit = {},
    bukaTematik: () -> Unit = {},
    bukaNusantara: () -> Unit = {},
    model: ModelTampilanBeranda = hiltViewModel(),
) {
    val ayatHarian by model.ayatHarian.collectAsState()
    val surahAyatHarian by model.surahAyatHarian.collectAsState()
    val posisiBaca by model.posisiBaca.collectAsState()
    val sholatBerikutnya by model.sholatBerikutnya.collectAsState()
    val namaKota by model.namaKota.collectAsState()

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

        // Kartu ayat harian dari data lokal, berganti setiap hari.
        Spacer(Modifier.height(20.dp))
        Card(
            Modifier.fillMaxWidth(),
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

        // Kartu lanjutkan membaca dari posisi terakhir yang tersimpan.
        Spacer(Modifier.height(14.dp))
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

        // Pintasan fitur-fitur utama.
        Spacer(Modifier.height(22.dp))
        Text("Pintasan", style = MaterialTheme.typography.titleMedium)
        Row(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Pintasan(Icons.Rounded.Schedule, "Sholat", bukaNusantara, Modifier.weight(1f))
            Pintasan(Icons.Rounded.Explore, "Kiblat", bukaNusantara, Modifier.weight(1f))
            Pintasan(Icons.Rounded.GraphicEq, "Murottal", bukaAudio, Modifier.weight(1f))
            Pintasan(Icons.Rounded.AutoStories, "Tematik", bukaTematik, Modifier.weight(1f))
        }
        Row(
            Modifier.fillMaxWidth().padding(top = 16.dp),
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

/** Kartu pintasan kecil berisi ikon dan label. */
@Composable
private fun Pintasan(ikon: ImageVector, label: String, aksi: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier.clickable(onClick = aksi),
        shape = RoundedCornerShape(18.dp),
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(ikon, contentDescription = label, tint = MaterialTheme.colorScheme.primary)
            Text(label, style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
