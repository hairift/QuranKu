package id.nusantara.quran.fitur.audio

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.inti.ui.tema.EmasTerang
import id.nusantara.quran.inti.ui.tema.HijauZamrud
import id.nusantara.quran.inti.ui.tema.HijauZamrudTua
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab
import java.util.Locale

/**
 * Layar murottal: pilih 5 qari dunia, kendalikan pemutaran latar belakang,
 * serta cari dan putar surah pilihan secara berurutan per ayat.
 */
@Composable
fun TampilanAudio(modifier: Modifier = Modifier, model: ModelTampilanAudio = hiltViewModel()) {
    val daftarSurah by model.daftarSurah.collectAsState()
    val keadaan by model.keadaan.collectAsState()
    val idQari by model.idQari.collectAsState()
    var kataCari by remember { mutableStateOf("") }

    val namaQariAktif = remember(idQari) {
        DAFTAR_QARI.firstOrNull { it.id == idQari }?.nama ?: "Misyari Rasyid Al-Afasy"
    }
    val surahTampil = remember(daftarSurah, kataCari) {
        if (kataCari.isBlank()) daftarSurah
        else {
            val k = kataCari.trim().lowercase()
            daftarSurah.filter {
                it.namaLatin.lowercase().contains(k) ||
                    it.arti.lowercase().contains(k) ||
                    it.nomor.toString() == k
            }
        }
    }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(18.dp))
        Text("Murottal Al-Quran", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Dengarkan lantunan ayat pilihan dengan layanan pemutar latar belakang",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // Pilihan qari.
        LazyRow(
            Modifier.padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(DAFTAR_QARI, key = { it.id }) { qari ->
                val aktif = idQari == qari.id
                FilterChip(
                    selected = aktif,
                    onClick = { model.pilihQari(qari.id) },
                    label = { Text(qari.nama) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        // Panel kendali pemutar utama bernuansa zamrud-emas.
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(HijauZamrudTua, HijauZamrud)))
                    .padding(20.dp),
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.14f),
                            modifier = Modifier.size(42.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.GraphicEq,
                                    contentDescription = null,
                                    tint = EmasTerang,
                                )
                            }
                        }
                        Column(Modifier.padding(start = 12.dp).weight(1f)) {
                            Text(
                                keadaan.judulAktif.ifBlank { "Pilih surah di bawah untuk memutar" },
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                            )
                            Text(
                                "Qari: $namaQariAktif",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmasTerang,
                            )
                        }
                    }

                    Slider(
                        value = if (keadaan.durasiMilis > 0) {
                            keadaan.posisiMilis.toFloat() / keadaan.durasiMilis.toFloat()
                        } else 0f,
                        onValueChange = { pecahan ->
                            if (keadaan.durasiMilis > 0) model.geserKe((pecahan * keadaan.durasiMilis).toLong())
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = EmasTerang,
                            activeTrackColor = EmasTerang,
                            inactiveTrackColor = Color.White.copy(alpha = 0.25f),
                        ),
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            formatMilis(keadaan.posisiMilis),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                        )
                        Text(
                            formatMilis(keadaan.durasiMilis),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth().padding(top = 4.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = { model.sebelumnya() }) {
                            Icon(
                                Icons.Rounded.SkipPrevious,
                                contentDescription = "Ayat sebelumnya",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                        FilledIconButton(
                            onClick = { model.putarAtauJeda() },
                            modifier = Modifier.padding(horizontal = 16.dp).size(52.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = EmasTerang,
                                contentColor = HijauZamrudTua,
                            ),
                        ) {
                            Icon(
                                if (keadaan.sedangMemutar) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                                contentDescription = if (keadaan.sedangMemutar) "Jeda" else "Putar",
                                modifier = Modifier.size(28.dp),
                            )
                        }
                        IconButton(onClick = { model.berikutnya() }) {
                            Icon(
                                Icons.Rounded.SkipNext,
                                contentDescription = "Ayat berikutnya",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                }
            }
        }

        // Pencarian dan daftar surah untuk diputar.
        OutlinedTextField(
            value = kataCari,
            onValueChange = { kataCari = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            placeholder = { Text("Cari surah untuk diputar…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(surahTampil, key = { it.nomor }) { surah ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { model.putarSurah(surah, idQari) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) {
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
                            Text(surah.namaLatin, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${surah.arti} · ${surah.jumlahAyat} ayat",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            surah.namaArab,
                            fontFamily = KeluargaHurufArab,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 10.dp),
                        )
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.PlayArrow,
                                    contentDescription = "Putar ${surah.namaLatin}",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Mengubah milidetik menjadi teks "m:dd". */
private fun formatMilis(milis: Long): String {
    if (milis <= 0) return "0:00"
    val detik = milis / 1000
    return String.format(Locale.US, "%d:%02d", detik / 60, detik % 60)
}
