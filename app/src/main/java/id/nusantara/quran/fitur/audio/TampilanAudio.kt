package id.nusantara.quran.fitur.audio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale

/**
 * Layar murottal: pilih qari dan surah, putar berurutan per ayat,
 * dengan kendali lengkap dan notifikasi media latar belakang.
 */
@Composable
fun TampilanAudio(modifier: Modifier = Modifier, model: ModelTampilanAudio = hiltViewModel()) {
    val daftarSurah by model.daftarSurah.collectAsState()
    val keadaan by model.keadaan.collectAsState()
    val idQari by model.idQari.collectAsState()

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(20.dp))
        Text("Murottal", style = MaterialTheme.typography.headlineMedium)
        Text(
            "Dengarkan bacaan per ayat, tetap berjalan di latar belakang",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // Pilihan qari.
        LazyRow(
            Modifier.padding(vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(DAFTAR_QARI) { qari ->
                FilterChip(
                    selected = idQari == qari.id,
                    onClick = { model.pilihQari(qari.id) },
                    label = { Text(qari.nama) },
                )
            }
        }

        // Panel kendali pemutar.
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.GraphicEq, contentDescription = null)
                    Text(
                        keadaan.judulAktif.ifBlank { "Belum ada bacaan diputar" },
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 10.dp).weight(1f),
                    )
                }
                Slider(
                    value = if (keadaan.durasiMilis > 0) {
                        keadaan.posisiMilis.toFloat() / keadaan.durasiMilis.toFloat()
                    } else 0f,
                    onValueChange = { pecahan ->
                        if (keadaan.durasiMilis > 0) model.geserKe((pecahan * keadaan.durasiMilis).toLong())
                    },
                    modifier = Modifier.padding(top = 8.dp),
                )
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(formatMilis(keadaan.posisiMilis), style = MaterialTheme.typography.labelSmall)
                    Text(formatMilis(keadaan.durasiMilis), style = MaterialTheme.typography.labelSmall)
                }
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { model.sebelumnya() }) {
                        Icon(Icons.Rounded.SkipPrevious, contentDescription = "Ayat sebelumnya")
                    }
                    FilledIconButton(onClick = { model.putarAtauJeda() }) {
                        Icon(
                            if (keadaan.sedangMemutar) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (keadaan.sedangMemutar) "Jeda" else "Putar",
                        )
                    }
                    IconButton(onClick = { model.berikutnya() }) {
                        Icon(Icons.Rounded.SkipNext, contentDescription = "Ayat berikutnya")
                    }
                }
            }
        }

        // Daftar surah untuk diputar.
        Text(
            "Putar satu surah penuh",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 12.dp),
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            items(daftarSurah, key = { it.nomor }) { surah ->
                ListItem(
                    headlineContent = { Text("${surah.nomor}. ${surah.namaLatin}") },
                    supportingContent = { Text("${surah.jumlahAyat} ayat") },
                    trailingContent = {
                        IconButton(onClick = { model.putarSurah(surah, idQari) }) {
                            Icon(
                                Icons.Rounded.PlayArrow,
                                contentDescription = "Putar ${surah.namaLatin}",
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    },
                )
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
