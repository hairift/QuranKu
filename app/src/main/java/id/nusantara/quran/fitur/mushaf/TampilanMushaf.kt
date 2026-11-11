package id.nusantara.quran.fitur.mushaf

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelSurah
import id.nusantara.quran.inti.ui.tema.EmasTerang
import id.nusantara.quran.inti.ui.tema.HijauZamrud
import id.nusantara.quran.inti.ui.tema.HijauZamrudTua
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab
import kotlinx.coroutines.launch

/**
 * Layar mushaf: direktori 114 surah dan 30 juz, serta pembaca ayat-per-ayat
 * dengan bingkai nomor Rub el Hizb, mode fokus Arab, audio per ayat,
 * catatan kaki Kemenag, salin/bagikan ayat, dan pinch-to-zoom.
 */
@Composable
fun TampilanMushaf(
    modifier: Modifier = Modifier,
    surahTujuan: Int = 0,
    ayatTujuan: Int = 0,
    onTujuanSelesai: () -> Unit = {},
    model: ModelTampilanMushaf = hiltViewModel(),
) {
    val surahAktif by model.surahAktif.collectAsState()
    var ayatTertunda by remember { mutableIntStateOf(0) }

    // Buka langsung surah tertentu bila datang dari pencarian/bookmark/tematik/beranda.
    LaunchedEffect(surahTujuan) {
        if (surahTujuan in 1..114) {
            ayatTertunda = ayatTujuan
            model.bukaSurah(surahTujuan)
            onTujuanSelesai()
        }
    }

    val terpilih = surahAktif
    if (terpilih == null) {
        DaftarSurah(
            modifier = modifier,
            model = model,
            onPilihJuz = { surah, ayat ->
                ayatTertunda = ayat
                model.bukaSurah(surah)
            },
        )
    } else {
        PembacaAyat(
            modifier = modifier,
            surah = terpilih,
            ayatTujuan = ayatTertunda,
            model = model,
            onGulirSelesai = { ayatTertunda = 0 },
        )
    }
}

/** Daftar seluruh surah dan direktori 30 juz dengan pencarian serta penyaring tipe. */
@Composable
private fun DaftarSurah(
    modifier: Modifier,
    model: ModelTampilanMushaf,
    onPilihJuz: (Int, Int) -> Unit,
) {
    val daftar by model.daftarSurah.collectAsState()
    val daftarJuz by model.daftarJuz.collectAsState()
    val sedangMemuat by model.sedangMemuat.collectAsState()
    var kata by remember { mutableStateOf("") }
    var filterAktif by remember { mutableStateOf("Semua") }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(18.dp))
        Text("Mushaf Al-Quran", style = MaterialTheme.typography.headlineMedium)
        Text(
            "114 surah · 30 juz · Rasm Utsmani & terjemahan resmi Indonesia",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = kata,
            onValueChange = { kata = it },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Cari surah") },
            placeholder = { Text("Cari nama, arti, atau nomor surah…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )

        // Baris chip filter: Semua, 30 Juz, Makkiyah, Madaniyah
        Row(
            Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            listOf("Semua", "30 Juz", "Makkiyah", "Madaniyah").forEach { namaFilter ->
                val aktif = filterAktif == namaFilter
                FilterChip(
                    selected = aktif,
                    onClick = { filterAktif = namaFilter },
                    label = { Text(namaFilter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        if (sedangMemuat) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (filterAktif == "30 Juz") {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(daftarJuz, key = { it.nomorJuz }) { info ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPilihJuz(info.nomorSurahAwal, info.nomorAyatAwal) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(R.drawable.ic_bingkai_ayat),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                                Text(
                                    "${info.nomorJuz}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                                Text("Juz ${info.nomorJuz}", style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "Dimulai pada ${info.namaSurahAwal} ayat ${info.nomorAyatAwal}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        } else {
            val tampil = remember(daftar, kata, filterAktif) { model.cariSurah(kata, filterAktif) }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(tampil, key = { it.nomor }) { surah ->
                    BarisSurah(surah, onKlik = { model.bukaSurah(surah.nomor) })
                }
            }
        }
    }
}

/** Satu baris surah seragam dengan bingkai Rub el Hizb dan kaligrafi Arab. */
@Composable
private fun BarisSurah(surah: ModelSurah, onKlik: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onKlik),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.ic_bingkai_ayat),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                )
                Text(
                    "${surah.nomor}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(surah.namaLatin, style = MaterialTheme.typography.titleMedium)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.padding(start = 8.dp),
                    ) {
                        Text(
                            surah.tipe,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        )
                    }
                }
                Text(
                    "${surah.arti} · ${surah.jumlahAyat} ayat",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
            Text(
                surah.namaArab,
                fontFamily = KeluargaHurufArab,
                fontSize = 23.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

/** Pembaca ayat satu surah dengan banner kaligrafi, kendali fokus, dan aksi per ayat. */
@Composable
private fun PembacaAyat(
    modifier: Modifier,
    surah: ModelSurah,
    ayatTujuan: Int,
    model: ModelTampilanMushaf,
    onGulirSelesai: () -> Unit = {},
) {
    val konteks = LocalContext.current
    val ayat by model.ayat.collectAsState()
    val sedangMemuat by model.sedangMemuat.collectAsState()
    val bookmark by model.bookmark.collectAsState()
    val ukuranArab by model.ukuranArab.collectAsState()
    val ukuranTerjemahan by model.ukuranTerjemahan.collectAsState()
    val tampilTerjemahan by model.tampilTerjemahan.collectAsState()
    val idBookmark = remember(bookmark) { bookmark.map { it.idAyatAwal }.toSet() }

    var tampilLompat by remember { mutableStateOf(false) }
    var tampilUkuran by remember { mutableStateOf(false) }
    var ayatCatatanKaki by remember { mutableStateOf<ModelAyat?>(null) }
    val keadaanDaftar = rememberLazyListState()

    // Gulir ke ayat tujuan dan catat posisi baca.
    LaunchedEffect(ayat, ayatTujuan) {
        if (ayat.isNotEmpty() && ayatTujuan > 0) {
            val indeks = ayat.indexOfFirst { it.nomor == ayatTujuan }
            if (indeks >= 0) keadaanDaftar.scrollToItem(indeks)
            onGulirSelesai()
        }
    }
    LaunchedEffect(keadaanDaftar.firstVisibleItemIndex, ayat) {
        ayat.getOrNull(keadaanDaftar.firstVisibleItemIndex)?.let {
            model.catatPosisiBaca(it.surah, it.nomor)
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .pointerInput(ukuranArab) {
                detectTransformGestures { _, _, zoom, _ ->
                    if (zoom != 1f) {
                        val baru = (ukuranArab * zoom).toInt().coerceIn(22, 44)
                        if (baru != ukuranArab) {
                            model.aturUkuranTeks(baru, ukuranTerjemahan)
                        }
                    }
                }
            },
    ) {
        // Bilah atas pembaca surah
        Surface(color = MaterialTheme.colorScheme.surface) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { model.tutupSurah() }) {
                    Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali ke daftar surah")
                }
                Column(Modifier.weight(1f)) {
                    Text("${surah.nomor}. ${surah.namaLatin}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${surah.arti} · ${surah.jumlahAyat} ayat",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { model.alihkanTampilTerjemahan() }) {
                    Icon(
                        Icons.Rounded.Translate,
                        contentDescription = "Alihkan tampilan terjemahan",
                        tint = if (tampilTerjemahan) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = { tampilLompat = true }) {
                    Icon(Icons.Rounded.Navigation, contentDescription = "Lompat ke ayat atau juz")
                }
                IconButton(onClick = { tampilUkuran = true }) {
                    Icon(Icons.Rounded.FormatSize, contentDescription = "Atur ukuran teks")
                }
            }
        }
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        if (sedangMemuat) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                state = keadaanDaftar,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Header Ornamen Surah & Basmalah
                item {
                    Card(
                        shape = RoundedCornerShape(22.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(Brush.linearGradient(listOf(HijauZamrudTua, HijauZamrud)))
                                .padding(20.dp),
                        ) {
                            Column(
                                Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(
                                    surah.namaArab,
                                    fontFamily = KeluargaHurufArab,
                                    fontSize = 32.sp,
                                    color = EmasTerang,
                                )
                                Text(
                                    "${surah.namaLatin} — ${surah.arti}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                                Text(
                                    "${surah.tipe} · ${surah.jumlahAyat} Ayat",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                                if (surah.nomor != 9 && surah.nomor != 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = Color.White.copy(alpha = 0.2f),
                                    )
                                    Text(
                                        "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                        fontFamily = KeluargaHurufArab,
                                        fontSize = 26.sp,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }
                }

                items(ayat, key = { it.id }) { butir ->
                    KartuAyat(
                        surah = surah,
                        ayat = butir,
                        ukuranArab = ukuranArab,
                        ukuranTerjemahan = ukuranTerjemahan,
                        tampilTerjemahan = tampilTerjemahan,
                        tersimpan = butir.id in idBookmark,
                        onBookmark = { model.alihkanBookmark(butir) },
                        onPutarAudio = {
                            model.putarAyat(surah, butir)
                            Toast.makeText(
                                konteks,
                                "Memutar QS. ${surah.namaLatin}: ${butir.nomor}",
                                Toast.LENGTH_SHORT,
                            ).show()
                        },
                        onLihatCatatanKaki = { ayatCatatanKaki = butir },
                        onSalin = {
                            val teks = "${butir.arab}\n\n\"${butir.terjemahan}\"\n(QS. ${surah.namaLatin}: ${butir.nomor})"
                            val papanKlip = konteks.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            papanKlip.setPrimaryClip(ClipData.newPlainText("Ayat Al-Quran", teks))
                            Toast.makeText(konteks, "Ayat berhasil disalin", Toast.LENGTH_SHORT).show()
                        },
                        onBagikan = {
                            val teks = "${butir.arab}\n\n\"${butir.terjemahan}\"\n— QS. ${surah.namaLatin} [${surah.nomor}]: ${butir.nomor}"
                            val niat = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, teks)
                            }
                            konteks.startActivity(Intent.createChooser(niat, "Bagikan Ayat"))
                        },
                    )
                }

                // Navigasi surah sebelumnya & berikutnya di bagian bawah
                item {
                    Row(
                        Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        if (surah.nomor > 1) {
                            OutlinedButton(
                                onClick = { model.bukaSurah(surah.nomor - 1) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                            ) {
                                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text("  Surah Sebelumnya")
                            }
                        }
                        if (surah.nomor < 114) {
                            OutlinedButton(
                                onClick = { model.bukaSurah(surah.nomor + 1) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(14.dp),
                            ) {
                                Text("Surah Berikutnya  ")
                                Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    if (tampilLompat) {
        DialogLompat(
            surah = surah,
            ayat = ayat,
            keadaanDaftar = keadaanDaftar,
            onTutup = { tampilLompat = false },
        )
    }
    if (tampilUkuran) {
        DialogUkuranTeks(
            arab = ukuranArab,
            terjemahan = ukuranTerjemahan,
            onTutup = { tampilUkuran = false },
            onSimpan = { a, t -> model.aturUkuranTeks(a, t); tampilUkuran = false },
        )
    }
    ayatCatatanKaki?.let { butir ->
        AlertDialog(
            onDismissRequest = { ayatCatatanKaki = null },
            title = { Text("Catatan Kaki · QS. ${surah.namaLatin}: ${butir.nomor}") },
            text = {
                Text(
                    butir.catatanKaki.ifBlank { "Tidak ada catatan kaki tambahan pada ayat ini." },
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            confirmButton = {
                TextButton(onClick = { ayatCatatanKaki = null }) { Text("Tutup") }
            },
        )
    }
}

/** Kartu satu ayat dengan ornamen bintang segi delapan dan bilah aksi lengkap. */
@Composable
private fun KartuAyat(
    surah: ModelSurah,
    ayat: ModelAyat,
    ukuranArab: Int,
    ukuranTerjemahan: Int,
    tampilTerjemahan: Boolean,
    tersimpan: Boolean,
    onBookmark: () -> Unit,
    onPutarAudio: () -> Unit,
    onLihatCatatanKaki: () -> Unit,
    onSalin: () -> Unit,
    onBagikan: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.dp,
            if (tersimpan) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outline,
        ),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.ic_bingkai_ayat),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                    Text(
                        "${ayat.nomor}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Column(Modifier.padding(start = 10.dp).weight(1f)) {
                    Text(
                        "${surah.namaLatin} : ${ayat.nomor}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        "Juz ${ayat.juz}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onPutarAudio, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.AutoMirrored.Rounded.VolumeUp,
                        contentDescription = "Putar audio ayat",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
                if (ayat.catatanKaki.isNotBlank()) {
                    IconButton(onClick = onLihatCatatanKaki, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Rounded.Info,
                            contentDescription = "Lihat catatan kaki",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }
                IconButton(onClick = onSalin, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Rounded.ContentCopy,
                        contentDescription = "Salin ayat",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                }
                IconButton(onClick = onBagikan, modifier = Modifier.size(36.dp)) {
                    Icon(
                        Icons.Rounded.Share,
                        contentDescription = "Bagikan ayat",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp),
                    )
                }
                IconButton(onClick = onBookmark, modifier = Modifier.size(36.dp)) {
                    Icon(
                        if (tersimpan) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = if (tersimpan) "Hapus bookmark" else "Simpan bookmark",
                        tint = if (tersimpan) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }

            Text(
                ayat.arab,
                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                fontFamily = KeluargaHurufArab,
                fontSize = ukuranArab.sp,
                lineHeight = (ukuranArab * 1.75).sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
            )

            if (tampilTerjemahan) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 12.dp, bottom = 10.dp),
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                )
                Text(
                    ayat.terjemahan,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = ukuranTerjemahan.sp,
                        lineHeight = (ukuranTerjemahan * 1.5).sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Dialog lompat ke nomor ayat atau awal juz tertentu. */
@Composable
private fun DialogLompat(
    surah: ModelSurah,
    ayat: List<ModelAyat>,
    keadaanDaftar: androidx.compose.foundation.lazy.LazyListState,
    onTutup: () -> Unit,
) {
    var masukanAyat by remember { mutableStateOf("") }
    var masukanJuz by remember { mutableStateOf("") }
    val cakupan = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onTutup,
        title = { Text("Lompat Cepat") },
        text = {
            Column {
                OutlinedTextField(
                    value = masukanAyat,
                    onValueChange = { masukanAyat = it.filter(Char::isDigit).take(3) },
                    label = { Text("Nomor ayat (1-${surah.jumlahAyat})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = masukanJuz,
                    onValueChange = { masukanJuz = it.filter(Char::isDigit).take(2) },
                    label = { Text("Nomor juz (1-30)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val sasaran: ModelAyat? = when {
                    masukanAyat.isNotBlank() -> ayat.firstOrNull { it.nomor == masukanAyat.toInt() }
                    masukanJuz.isNotBlank() -> ayat.firstOrNull { it.juz == masukanJuz.toInt() }
                    else -> null
                }
                if (sasaran != null) {
                    val indeks = ayat.indexOf(sasaran)
                    // +1 karena item pertama pada LazyColumn adalah banner header surah
                    cakupan.launch { keadaanDaftar.scrollToItem(indeks + 1) }
                }
                onTutup()
            }) { Text("Lompat") }
        },
        dismissButton = { TextButton(onClick = onTutup) { Text("Batal") } },
    )
}

/** Dialog pengatur ukuran teks Arab dan terjemahan. */
@Composable
private fun DialogUkuranTeks(
    arab: Int,
    terjemahan: Int,
    onTutup: () -> Unit,
    onSimpan: (Int, Int) -> Unit,
) {
    var ukuranArab by remember { mutableIntStateOf(arab) }
    var ukuranTerjemahan by remember { mutableIntStateOf(terjemahan) }

    AlertDialog(
        onDismissRequest = onTutup,
        title = { Text("Ukuran Teks Mushaf") },
        text = {
            Column {
                Text("Teks Arab: $ukuranArab sp", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = ukuranArab.toFloat(),
                    onValueChange = { ukuranArab = it.toInt() },
                    valueRange = 22f..44f,
                )
                Spacer(Modifier.height(8.dp))
                Text("Terjemahan: $ukuranTerjemahan sp", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = ukuranTerjemahan.toFloat(),
                    onValueChange = { ukuranTerjemahan = it.toInt() },
                    valueRange = 12f..24f,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSimpan(ukuranArab, ukuranTerjemahan) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onTutup) { Text("Batal") } },
    )
}
