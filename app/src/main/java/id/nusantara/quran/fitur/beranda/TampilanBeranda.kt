package id.nusantara.quran.fitur.beranda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Topic
import androidx.compose.material.icons.rounded.TrackChanges
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.inti.ui.tema.EmasTerang
import id.nusantara.quran.inti.ui.tema.HijauZamrud
import id.nusantara.quran.inti.ui.tema.HijauZamrudTua
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Beranda QuranKu: header identitas, kartu Ayat Hari Ini berlatar ilustrasi masjid,
 * progres tilawah harian, lanjutkan membaca, hitung mundur waktu sholat,
 * pintasan fitur berskala seragam, bacaan unggulan, dan riwayat bacaan.
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
    bukaAsmaulHusna: () -> Unit = {},
    bukaDzikir: () -> Unit = {},
    model: ModelTampilanBeranda = hiltViewModel(),
) {
    val ayatHarian by model.ayatHarian.collectAsState()
    val surahAyatHarian by model.surahAyatHarian.collectAsState()
    val posisiBaca by model.posisiBaca.collectAsState()
    val sholatBerikutnya by model.sholatBerikutnya.collectAsState()
    val namaKota by model.namaKota.collectAsState()
    val riwayatTerakhir by model.riwayatTerakhir.collectAsState()
    val unggulan by model.unggulan.collectAsState()
    val bacaanHariIni by model.bacaanHariIni.collectAsState()
    val targetHarian by model.targetHarian.collectAsState()
    var dialogTarget by remember { mutableStateOf(false) }

    val tanggalHariIni = remember {
        SimpleDateFormat("EEEE, d MMMM yyyy", Locale("id", "ID")).format(Date())
    }

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
    ) {
        // Header Beranda: logo resmi QuranKu, salam, tanggal, dan pintasan cepat.
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.size(46.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.quranku_icon_transparan),
                        contentDescription = "Logo QuranKu",
                        modifier = Modifier.size(34.dp),
                    )
                }
            }
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(
                    "Assalamu'alaikum",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    "Ruang teduh untuk membaca",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                IconButton(onClick = bukaPencarian, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = "Cari ayat",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                IconButton(onClick = bukaBookmark, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Rounded.Bookmark,
                        contentDescription = "Koleksi bookmark",
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        // Baris tanggal dan lokasi aktif.
        Row(
            Modifier.fillMaxWidth().padding(top = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Rounded.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(15.dp),
            )
            Text(
                " $tanggalHariIni  ·  $namaKota",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        // Kartu utama Ayat Hari Ini dengan latar foto masjid offline & siluet vektor.
        Spacer(Modifier.height(10.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { ayatHarian?.let { bukaMushafDi(it.surah, it.nomor) } },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(HijauZamrudTua, HijauZamrud),
                        ),
                    ),
            ) {
                Image(
                    painter = painterResource(R.drawable.foto_masjid_banner),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.16f,
                    modifier = Modifier.matchParentSize(),
                )
                Image(
                    painter = painterResource(R.drawable.ic_masjid_nusantara),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .width(200.dp)
                        .height(110.dp),
                )
                Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.14f),
                        ) {
                            Text(
                                "AYAT HARI INI",
                                style = MaterialTheme.typography.labelSmall,
                                color = EmasTerang,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            )
                        }
                        val rujukan = ayatHarian?.let {
                            "QS. ${surahAyatHarian?.namaLatin ?: ""}: ${it.nomor}"
                        } ?: ""
                        Text(
                            rujukan,
                            color = EmasTerang,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        ayatHarian?.arab ?: "...",
                        color = Color.White,
                        fontFamily = KeluargaHurufArab,
                        fontSize = 23.sp,
                        lineHeight = 38.sp,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        ayatHarian?.terjemahan ?: "Memuat ayat pilihan hari ini…",
                        color = Color.White.copy(alpha = 0.92f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Buka ayat di Mushaf",
                            color = EmasTerang,
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Spacer(Modifier.width(4.dp))
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = EmasTerang,
                            modifier = Modifier.size(14.dp),
                        )
                    }
                }
            }
        }

        // Baris 2 kolom seragam: Lanjutkan Membaca & Menuju Waktu Sholat.
        Spacer(Modifier.height(12.dp))
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Kartu Lanjutkan Membaca (tinggi seragam 108.dp)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(108.dp)
                    .clickable { bukaMushafDi(posisiBaca.first, posisiBaca.second) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Column(
                    Modifier.fillMaxSize().padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(34.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.MenuBook,
                                    contentDescription = "Lanjutkan membaca",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                        Text(
                            "TERAKHIR BACA",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Column {
                        Text(
                            posisiBaca.third,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            "Ayat ${posisiBaca.second} · Ketuk lanjut",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            // Kartu Waktu Sholat Berikutnya (tinggi seragam 108.dp)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(108.dp)
                    .clickable(onClick = bukaNusantara),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                val infoSholat = sholatBerikutnya
                Column(
                    Modifier.fillMaxSize().padding(14.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(34.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.Schedule,
                                    contentDescription = "Jadwal sholat",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                        Text(
                            "SHOLAT & KIBLAT",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                    Column {
                        Text(
                            if (infoSholat != null) "${infoSholat.first} · ${infoSholat.second}"
                            else "Jadwal Sholat",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            if (infoSholat != null) {
                                "${infoSholat.third / 60}j ${infoSholat.third % 60}m lagi"
                            } else {
                                namaKota
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }

        // Kartu Target Tilawah Harian
        Spacer(Modifier.height(10.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { dialogTarget = true },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 11.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.TrackChanges,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            "  Target Tilawah Hari Ini",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    Text(
                        "$bacaanHariIni / $targetHarian ayat",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (bacaanHariIni.toFloat() / targetHarian.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                )
            }
        }

        // Fitur Disarankan: seluruh kartu memiliki ukuran seragam (108.dp x 110.dp).
        Spacer(Modifier.height(14.dp))
        Text("Fitur Disarankan", style = MaterialTheme.typography.titleMedium)
        LazyRow(
            Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { KartuDisarankanVektor(Icons.Rounded.Explore, "Sholat & Kiblat", bukaNusantara) }
            item { KartuDisarankanLukis(painterResource(R.drawable.ic_tasbih_butir), "Dzikir & Tasbih", bukaDzikir) }
            item { KartuDisarankanLukis(painterResource(R.drawable.ic_asmaul_husna), "Asmaul Husna", bukaAsmaulHusna) }
            item { KartuDisarankanVektor(Icons.Rounded.WorkspacePremium, "Bacaan Unggulan", bukaUnggulan) }
            item { KartuDisarankanVektor(Icons.Rounded.Science, "Quran & Sains", bukaSains) }
            item { KartuDisarankanVektor(Icons.Rounded.Topic, "Penjelajah Topik", bukaTopik) }
            item { KartuDisarankanVektor(Icons.Rounded.AutoStories, "Kajian Tematik", bukaTematik) }
            item { KartuDisarankanVektor(Icons.Rounded.Mic, "Cari Suara", bukaPencarian) }
            item { KartuDisarankanVektor(Icons.Rounded.History, "Riwayat Bacaan", bukaRiwayat) }
            item { KartuDisarankanVektor(Icons.Rounded.GraphicEq, "Murottal Audio", bukaAudio) }
        }

        // Bacaan unggulan: seluruh kartu seragam 224.dp x 124.dp.
        if (unggulan.isNotEmpty()) {
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Bacaan Unggulan",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = bukaUnggulan) { Text("Lihat semua") }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(unggulan, key = { it.id }) { bacaan ->
                    Card(
                        Modifier
                            .width(224.dp)
                            .height(124.dp)
                            .clickable { bukaMushafDi(bacaan.surah, if (bacaan.ayat > 0) bacaan.ayat else 1) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Column(
                            Modifier.fillMaxSize().padding(14.dp),
                            verticalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                            ) {
                                Text(
                                    bacaan.rekomendasi,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                )
                            }
                            Column {
                                Text(
                                    bacaan.judul,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    bacaan.deskripsi,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }

        // Riwayat bacaan terakhir.
        if (riwayatTerakhir.isNotEmpty()) {
            Spacer(Modifier.height(22.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Riwayat Bacaan",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                TextButton(onClick = bukaRiwayat) { Text("Lihat semua") }
            }
            Card(
                Modifier.fillMaxWidth().padding(top = 4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Column(Modifier.padding(vertical = 4.dp)) {
                    riwayatTerakhir.forEachIndexed { indeks, butir ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { bukaMushafDi(butir.surah, butir.ayat) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(32.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.History,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp),
                                    )
                                }
                            }
                            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(
                                    "QS. ${butir.namaSurah.ifBlank { butir.surah.toString() }}",
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Text(
                                    "Ayat ${butir.ayat}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp),
                            )
                        }
                        if (indeks < riwayatTerakhir.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = bukaPencarian,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Icon(Icons.Rounded.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("  Cari Ayat", style = MaterialTheme.typography.labelLarge)
            }
            OutlinedButton(
                onClick = bukaBookmark,
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Icon(Icons.Rounded.Bookmark, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("  Bookmark", style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer(Modifier.height(16.dp))
    }

    if (dialogTarget) {
        AlertDialog(
            onDismissRequest = { dialogTarget = false },
            title = { Text("Target Tilawah Harian") },
            text = {
                Column {
                    Text(
                        "Pilih target jumlah ayat yang ingin Anda baca setiap hari:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf(10, 20, 50, 100).forEach { pilihan ->
                            FilterChip(
                                selected = targetHarian == pilihan,
                                onClick = {
                                    model.ubahTargetHarian(pilihan)
                                    dialogTarget = false
                                },
                                label = { Text("$pilihan") },
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { dialogTarget = false }) { Text("Tutup") }
            },
        )
    }
}

/** Kartu seragam berukuran 108x110 dp pada baris Fitur Disarankan (ikon ImageVector). */
@Composable
private fun KartuDisarankanVektor(ikon: ImageVector, label: String, aksi: () -> Unit) {
    Card(
        modifier = Modifier
            .width(108.dp)
            .height(110.dp)
            .clickable(onClick = aksi),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            Modifier.fillMaxSize().padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        ikon,
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/** Kartu seragam berukuran 108x110 dp pada baris Fitur Disarankan (ikon Painter aset vektor). */
@Composable
private fun KartuDisarankanLukis(pelukis: Painter, label: String, aksi: () -> Unit) {
    Card(
        modifier = Modifier
            .width(108.dp)
            .height(110.dp)
            .clickable(onClick = aksi),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            Modifier.fillMaxSize().padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = pelukis,
                        contentDescription = label,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
