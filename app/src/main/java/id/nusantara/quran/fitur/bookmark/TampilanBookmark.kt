package id.nusantara.quran.fitur.bookmark

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Koleksi bookmark pengguna: catatan yang bisa disunting,
 * ekspor ke JSON, dan impor kembali dari berkas JSON.
 */
@Composable
fun TampilanBookmark(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    bukaAyat: (Int, Int) -> Unit = { _, _ -> },
    model: ModelTampilanBookmark = hiltViewModel(),
) {
    val daftar by model.daftar.collectAsState()
    val konteks = LocalContext.current
    val cakupan = rememberCoroutineScope()
    var disunting by remember { mutableStateOf<EntitasBookmark?>(null) }
    var pesan by remember { mutableStateOf("") }

    // Pemilih berkas untuk impor bookmark JSON.
    val peluncurImpor = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent(),
    ) { alamat ->
        alamat ?: return@rememberLauncherForActivityResult
        cakupan.launch {
            val teks = withContext(Dispatchers.IO) {
                konteks.contentResolver.openInputStream(alamat)?.bufferedReader()?.use { it.readText() }
            } ?: return@launch
            val jumlah = model.imporJson(teks)
            pesan = if (jumlah >= 0) "$jumlah bookmark berhasil diimpor" else "Berkas tidak dikenali sebagai bookmark QuranKu"
        }
    }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text("Bookmark", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.weight(1f))
            IconButton(onClick = {
                // Ekspor seluruh bookmark sebagai teks JSON melalui lembar berbagi.
                val data = JSONArray().apply {
                    daftar.forEach {
                        put(JSONObject().apply {
                            put("idAyatAwal", it.idAyatAwal)
                            put("idAyatAkhir", it.idAyatAkhir)
                            put("judulCatatan", it.judulCatatan)
                            put("isiCatatan", it.isiCatatan)
                            put("warnaTag", it.warnaTag)
                            put("tanggalDibuat", it.tanggalDibuat)
                        })
                    }
                }
                konteks.startActivity(
                    Intent.createChooser(
                        Intent(Intent.ACTION_SEND).apply {
                            type = "application/json"
                            putExtra(Intent.EXTRA_TEXT, data.toString(2))
                            putExtra(Intent.EXTRA_SUBJECT, "Bookmark QuranKu")
                        },
                        "Ekspor bookmark QuranKu",
                    ),
                )
            }) {
                Icon(Icons.Rounded.Share, contentDescription = "Ekspor JSON")
            }
            IconButton(onClick = { peluncurImpor.launch("application/json") }) {
                Icon(Icons.Rounded.Download, contentDescription = "Impor JSON")
            }
        }

        Text(
            "${daftar.size} ayat ditandai",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        if (pesan.isNotBlank()) {
            Text(
                pesan,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        LazyColumn {
            items(daftar, key = { it.id }) { bookmark ->
                val surah = bookmark.idAyatAwal / 1000
                val ayat = bookmark.idAyatAwal % 1000
                val tanggal = SimpleDateFormat("d MMM yyyy", Locale("id")).format(Date(bookmark.tanggalDibuat))
                ListItem(
                    headlineContent = { Text(bookmark.judulCatatan.ifBlank { "QS $surah:$ayat" }) },
                    supportingContent = {
                        Text(bookmark.isiCatatan.ifBlank { "Disimpan $tanggal" })
                    },
                    overlineContent = { Text("Surah $surah · Ayat $ayat") },
                    trailingContent = {
                        Row {
                            IconButton(onClick = { disunting = bookmark }) {
                                Icon(Icons.Rounded.Edit, contentDescription = "Sunting catatan")
                            }
                            IconButton(onClick = { model.hapus(bookmark) }) {
                                Icon(Icons.Rounded.Delete, contentDescription = "Hapus bookmark")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().clickable { bukaAyat(surah, ayat) },
                )
                HorizontalDivider()
            }
        }
    }

    // Dialog penyuntingan catatan bookmark.
    disunting?.let { bookmark ->
        var judul by remember(bookmark.id) { mutableStateOf(bookmark.judulCatatan) }
        var isi by remember(bookmark.id) { mutableStateOf(bookmark.isiCatatan) }
        AlertDialog(
            onDismissRequest = { disunting = null },
            title = { Text("Sunting catatan") },
            text = {
                Column {
                    OutlinedTextField(
                        value = judul,
                        onValueChange = { judul = it },
                        label = { Text("Judul") },
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = isi,
                        onValueChange = { isi = it },
                        label = { Text("Catatan") },
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    model.ubahCatatan(bookmark, judul, isi)
                    disunting = null
                }) { Text("Simpan") }
            },
            dismissButton = { TextButton(onClick = { disunting = null }) { Text("Batal") } },
        )
    }
}
