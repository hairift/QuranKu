package id.nusantara.quran.fitur.riwayat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Riwayat bacaan: daftar ayat yang pernah dibuka, tercatat otomatis
 * dari Mushaf. Menyentuh kartu membuka kembali ayat tersebut.
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
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column(Modifier.weight(1f)) {
                Text("Riwayat Bacaan", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Jejak tilawah otomatis dari halaman Mushaf",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
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
                    modifier = Modifier.size(48.dp),
                )
                Text(
                    "Belum ada riwayat bacaan.\nAyat yang Anda buka di Mushaf akan tercatat otomatis di sini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp),
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(riwayat, key = { "${it.surah}:${it.ayat}" }) { butir ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bukaAyat(butir.surah, butir.ayat) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.size(38.dp), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(R.drawable.ic_bingkai_ayat),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                                Text(
                                    "${butir.surah}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                                Text(
                                    "QS. ${butir.namaSurah.ifBlank { butir.surah.toString() }} : Ayat ${butir.ayat}",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    formatWaktu.format(Date(butir.waktu)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                            IconButton(onClick = { model.hapus(butir.surah, butir.ayat) }) {
                                Icon(
                                    Icons.Rounded.Close,
                                    contentDescription = "Hapus butir ini",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (tanyaBersihkan) {
        AlertDialog(
            onDismissRequest = { tanyaBersihkan = false },
            title = { Text("Bersihkan Riwayat?") },
            text = { Text("Seluruh riwayat bacaan akan dihapus permanen dan tidak bisa dikembalikan.") },
            confirmButton = {
                TextButton(onClick = {
                    model.bersihkanSemua()
                    tanyaBersihkan = false
                }) { Text("Hapus Semua") }
            },
            dismissButton = { TextButton(onClick = { tanyaBersihkan = false }) { Text("Batal") } },
        )
    }
}
