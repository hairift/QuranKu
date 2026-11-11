package id.nusantara.quran.fitur.asmaulhusna

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.domain.model.ModelAsmaulHusna
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Layar 99 Asmaul Husna: menampilkan seluruh nama indah Allah
 * dengan pencarian cepat, detail tadabbur, serta fitur salin dan bagikan.
 */
@Composable
fun TampilanAsmaulHusna(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    model: ModelTampilanAsmaulHusna = hiltViewModel(),
) {
    val konteks = LocalContext.current
    val daftar by model.daftar.collectAsState()
    var kataKunci by remember { mutableStateOf("") }
    var terpilih by remember { mutableStateOf<ModelAsmaulHusna?>(null) }

    val daftarTampil = remember(daftar, kataKunci) { model.saring(kataKunci) }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column(Modifier.weight(1f)) {
                Text("99 Asmaul Husna", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Nama-nama indah Allah beserta makna dan hikmahnya",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        OutlinedTextField(
            value = kataKunci,
            onValueChange = { kataKunci = it },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = "Cari Asmaul Husna") },
            placeholder = { Text("Cari nama, nomor (1-99), atau arti…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(daftarTampil, key = { it.nomor }) { butir ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { terpilih = butir },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(Modifier.size(44.dp), contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(R.drawable.ic_bingkai_ayat),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                            )
                            Text(
                                "${butir.nomor}",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                            Text(
                                butir.latin,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                butir.arti,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                butir.keterangan,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp),
                            )
                        }
                        Text(
                            butir.arab,
                            fontFamily = KeluargaHurufArab,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }
    }

    terpilih?.let { butir ->
        AlertDialog(
            onDismissRequest = { terpilih = null },
            title = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        butir.arab,
                        fontFamily = KeluargaHurufArab,
                        fontSize = 36.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "${butir.nomor}. ${butir.latin}",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        butir.arti,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            },
            text = {
                Column {
                    Text(
                        butir.keterangan,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(14.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(onClick = {
                            val teksSalin = "${butir.arab}\n${butir.latin} (${butir.arti})\n${butir.keterangan}"
                            val papanKlip = konteks.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            papanKlip.setPrimaryClip(ClipData.newPlainText("Asmaul Husna", teksSalin))
                            Toast.makeText(konteks, "Disalin ke papan klip", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Rounded.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("  Salin")
                        }
                        TextButton(onClick = {
                            val teksBagi = "${butir.arab}\n${butir.latin} — ${butir.arti}\n\n${butir.keterangan}\n(QuranKu Nusantara)"
                            val niat = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, teksBagi)
                            }
                            konteks.startActivity(Intent.createChooser(niat, "Bagikan Asmaul Husna"))
                        }) {
                            Icon(Icons.Rounded.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text("  Bagikan")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { terpilih = null }) { Text("Tutup") }
            },
        )
    }
}

