package id.nusantara.quran.fitur.bookmark

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
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
 * Koleksi bookmark pengguna: daftar ayat tersimpan dalam kartu rapi,
 * catatan pribadi yang bisa disunting, serta ekspor dan impor JSON.
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
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column(Modifier.weight(1f)) {
                Text("Koleksi Bookmark", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "${daftar.size} ayat tersimpan beserta catatan pribadi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = {
                val data = JSONArray().apply {
                    daftar.forEach {
                        put(
                            JSONObject().apply {
                                put("idAyatAwal", it.idAyatAwal)
                                put("idAyatAkhir", it.idAyatAkhir)
                                put("judulCatatan", it.judulCatatan)
                                put("isiCatatan", it.isiCatatan)
                                put("warnaTag", it.warnaTag)
                                put("tanggalDibuat", it.tanggalDibuat)
                            },
                        )
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

        if (pesan.isNotBlank()) {
            Text(
                pesan,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp),
            )
        }

        if (daftar.isEmpty()) {
            Column(
                Modifier.fillMaxWidth().padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    Icons.Rounded.BookmarkBorder,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(48.dp),
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    "Belum ada ayat yang ditandai.\nKetuk ikon penanda pada ayat di Mushaf untuk menyimpannya di sini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(daftar, key = { it.id }) { bookmark ->
                    val surah = bookmark.idAyatAwal / 1000
                    val ayat = bookmark.idAyatAwal % 1000
                    val tanggal = SimpleDateFormat("d MMM yyyy", Locale("id", "ID")).format(Date(bookmark.tanggalDibuat))
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { bukaAyat(surah, ayat) },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(Modifier.size(42.dp), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(R.drawable.ic_bingkai_ayat),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                                Text(
                                    "$ayat",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                ) {
                                    Text(
                                        "Surah $surah · Ayat $ayat",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    )
                                }
                                Text(
                                    bookmark.judulCatatan.ifBlank { "QS $surah:$ayat" },
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 4.dp),
                                )
                                Text(
                                    bookmark.isiCatatan.ifBlank { "Disimpan pada $tanggal" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                            IconButton(onClick = { disunting = bookmark }) {
                                Icon(
                                    Icons.Rounded.Edit,
                                    contentDescription = "Sunting catatan",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                            IconButton(onClick = { model.hapus(bookmark) }) {
                                Icon(
                                    Icons.Rounded.Delete,
                                    contentDescription = "Hapus bookmark",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    disunting?.let { bookmark ->
        var judul by remember(bookmark.id) { mutableStateOf(bookmark.judulCatatan) }
        var isi by remember(bookmark.id) { mutableStateOf(bookmark.isiCatatan) }
        AlertDialog(
            onDismissRequest = { disunting = null },
            title = { Text("Sunting Catatan Bookmark") },
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
