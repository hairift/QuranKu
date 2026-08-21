package id.nusantara.quran.fitur.riwayat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Riwayat bacaan: daftar ayat yang pernah dibuka, tercatat otomatis
 * dari mushaf. Menyentuh butir membuka kembali ayat tersebut.
 */
@Composable
fun TampilanRiwayat(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    bukaAyat: (Int, Int) -> Unit = { _, _ -> },
    model: ModelTampilanRiwayat = hiltViewModel(),
) {
    val riwayat by model.riwayat.collectAsState()
    var tanyaBersihkan by remember { mutableStateOf(false) }
    val formatWaktu = remember { SimpleDateFormat("d MMM yyyy, HH:mm", Locale("id", "ID")) }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text("Riwayat Bacaan", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
            if (riwayat.isNotEmpty()) {
                IconButton(onClick = { tanyaBersihkan = true }) {
                    Icon(Icons.Rounded.DeleteSweep, contentDescription = "Bersihkan riwayat")
                }
            }
        }

        if (riwayat.isEmpty()) {
            Column(
                Modifier.fillMaxWidth().padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Rounded.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                )
                Text(
                    "Belum ada riwayat bacaan.\nAyat yang Anda buka di mushaf akan tercatat di sini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        } else {
            LazyColumn {
                items(riwayat, key = { "${it.surah}:${it.ayat}" }) { butir ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { bukaAyat(butir.surah, butir.ayat) }
                            .padding(vertical = 12.dp, horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "QS ${butir.namaSurah.ifBlank { butir.surah.toString() }}: ${butir.ayat}",
                                style = MaterialTheme.typography.titleMedium,
                            )
                            Text(
                                formatWaktu.format(Date(butir.waktu)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                        IconButton(onClick = { model.hapus(butir.surah, butir.ayat) }) {
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = "Hapus butir ini",
                                tint = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
                    HorizontalDivider()
                }
            }
        }
    }

    if (tanyaBersihkan) {
        AlertDialog(
            onDismissRequest = { tanyaBersihkan = false },
            title = { Text("Bersihkan riwayat?") },
            text = { Text("Seluruh riwayat bacaan akan dihapus permanen dan tidak bisa dikembalikan.") },
            confirmButton = {
                TextButton(onClick = {
                    model.bersihkanSemua()
                    tanyaBersihkan = false
                }) { Text("Hapus semua") }
            },
            dismissButton = { TextButton(onClick = { tanyaBersihkan = false }) { Text("Batal") } },
        )
    }
}
