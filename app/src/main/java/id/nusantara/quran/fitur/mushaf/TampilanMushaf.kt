package id.nusantara.quran.fitur.mushaf

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.FormatSize
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelSurah
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab
import kotlinx.coroutines.launch

/**
 * Layar mushaf: daftar 114 surah dan pembaca ayat-per-ayat
 * dengan lompat juz, bookmark persisten, dan pengatur ukuran teks.
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

    // Buka langsung surah tertentu bila datang dari pencarian/bookmark/tematik.
    LaunchedEffect(surahTujuan) {
        if (surahTujuan in 1..114) {
            ayatTertunda = ayatTujuan
            model.bukaSurah(surahTujuan)
            onTujuanSelesai()
        }
    }

    val terpilih = surahAktif
    if (terpilih == null) {
        DaftarSurah(modifier, model)
    } else {
        PembacaAyat(modifier, terpilih, ayatTertunda, model, onGulirSelesai = { ayatTertunda = 0 })
    }
}

/** Daftar seluruh surah dengan pencarian cepat. */
@Composable
private fun DaftarSurah(modifier: Modifier, model: ModelTampilanMushaf) {
    val daftar by model.daftarSurah.collectAsState()
    val sedangMemuat by model.sedangMemuat.collectAsState()
    var kata by remember { mutableStateOf("") }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(20.dp))
        Text("Mushaf", style = MaterialTheme.typography.headlineMedium)
        Text(
            "114 surah · 30 juz · terjemahan Indonesia",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        OutlinedTextField(
            value = kata,
            onValueChange = { kata = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Cari surah") },
            placeholder = { Text("Cari nama atau nomor surah") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )
        if (sedangMemuat) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            val tampil = model.cariSurah(kata)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(tampil, key = { it.nomor }) { surah ->
                    BarisSurah(surah, onKlik = { model.bukaSurah(surah.nomor) })
                }
            }
        }
    }
}

/** Satu baris surah dengan nomor berbingkai dan nama Arab. */
@Composable
private fun BarisSurah(surah: ModelSurah, onKlik: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onKlik),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "${surah.nomor}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                Text(surah.namaLatin, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${surah.tipe} · ${surah.jumlahAyat} ayat · ${surah.arti}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(surah.namaArab, fontFamily = KeluargaHurufArab, fontSize = 22.sp)
        }
    }
}

/** Pembaca ayat satu surah dengan header informasi dan kendali. */
@Composable
private fun PembacaAyat(
    modifier: Modifier,
    surah: ModelSurah,
    ayatTujuan: Int,
    model: ModelTampilanMushaf,
    onGulirSelesai: () -> Unit = {},
) {
    val ayat by model.ayat.collectAsState()
    val sedangMemuat by model.sedangMemuat.collectAsState()
    val bookmark by model.bookmark.collectAsState()
    val ukuranArab by model.ukuranArab.collectAsState()
    val ukuranTerjemahan by model.ukuranTerjemahan.collectAsState()
    val idBookmark = remember(bookmark) { bookmark.map { it.idAyatAwal }.toSet() }
    var tampilLompat by remember { mutableStateOf(false) }
    var tampilUkuran by remember { mutableStateOf(false) }
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

    Column(modifier.fillMaxSize()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { model.tutupSurah() }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali ke daftar surah")
            }
            Column(Modifier.weight(1f)) {
                Text(surah.namaLatin, style = MaterialTheme.typography.titleLarge)
                Text(
                    "${surah.tipe} · ${surah.jumlahAyat} ayat",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text(surah.namaArab, fontFamily = KeluargaHurufArab, fontSize = 24.sp)
            IconButton(onClick = { tampilLompat = true }) {
                Icon(Icons.Rounded.Navigation, contentDescription = "Lompat ke ayat atau juz")
            }
            IconButton(onClick = { tampilUkuran = true }) {
                Icon(Icons.Rounded.FormatSize, contentDescription = "Atur ukuran teks")
            }
        }
        HorizontalDivider()

        if (sedangMemuat) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(
                state = keadaanDaftar,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Basmalah pembuka untuk seluruh surah kecuali At-Taubah.
                if (surah.nomor != 9 && surah.nomor != 1) {
                    item {
                        Text(
                            "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            fontFamily = KeluargaHurufArab,
                            fontSize = ukuranArab.sp,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                items(ayat, key = { it.id }) { butir ->
                    KartuAyat(
                        ayat = butir,
                        ukuranArab = ukuranArab,
                        ukuranTerjemahan = ukuranTerjemahan,
                        tersimpan = butir.id in idBookmark,
                        onBookmark = { model.alihkanBookmark(butir) },
                    )
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
}

/** Kartu satu ayat: nomor, teks Arab, terjemahan, dan tombol bookmark. */
@Composable
private fun KartuAyat(
    ayat: ModelAyat,
    ukuranArab: Int,
    ukuranTerjemahan: Int,
    tersimpan: Boolean,
    onBookmark: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(32.dp),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("${ayat.nomor}", style = MaterialTheme.typography.labelLarge)
                    }
                }
                Text(
                    "Juz ${ayat.juz}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 10.dp).weight(1f),
                )
                IconButton(onClick = onBookmark) {
                    Icon(
                        if (tersimpan) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = if (tersimpan) "Hapus bookmark" else "Simpan bookmark",
                        tint = if (tersimpan) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Text(
                ayat.arab,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                fontFamily = KeluargaHurufArab,
                fontSize = ukuranArab.sp,
                lineHeight = (ukuranArab * 1.7).sp,
                textAlign = TextAlign.Right,
                fontWeight = FontWeight.Normal,
            )
            Text(
                ayat.terjemahan,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = ukuranTerjemahan.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
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
    // Cakupan korutina bawaan Compose untuk menggulir daftar ayat.
    val cakupan = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onTutup,
        title = { Text("Lompat cepat") },
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
                    cakupan.launch { keadaanDaftar.scrollToItem(indeks) }
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
        title = { Text("Ukuran teks") },
        text = {
            Column {
                Text("Teks Arab: $ukuranArab", style = MaterialTheme.typography.bodyMedium)
                Slider(
                    value = ukuranArab.toFloat(),
                    onValueChange = { ukuranArab = it.toInt() },
                    valueRange = 22f..44f,
                )
                Spacer(Modifier.height(8.dp))
                Text("Terjemahan: $ukuranTerjemahan", style = MaterialTheme.typography.bodyMedium)
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
