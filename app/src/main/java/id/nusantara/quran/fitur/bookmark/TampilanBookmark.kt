package id.nusantara.quran.fitur.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Layar bookmark dengan konsep folder dan ekspor JSON. */
@Composable
fun TampilanBookmark(modifier: Modifier = Modifier, onKembali: () -> Unit = {}, model: ModelTampilanBookmark = hiltViewModel()) {
    val daftar by model.daftar.collectAsState()
    val konteks = LocalContext.current
    Column(modifier.fillMaxSize().padding(22.dp)) {
        IconButton(onClick = onKembali) { Icon(Icons.Rounded.Bookmark, "Kembali") }
        Text("Koleksi ayat", Modifier.padding(top = 12.dp))
        Text("${daftar.size} bookmark tersimpan di perangkat", Modifier.padding(top = 8.dp))
        Button(onClick = {
            val data = JSONArray().apply { daftar.forEach { put(org.json.JSONObject().apply { put("idAyatAwal", it.idAyatAwal); put("idAyatAkhir", it.idAyatAkhir); put("judulCatatan", it.judulCatatan); put("isiCatatan", it.isiCatatan); put("warnaTag", it.warnaTag); put("tanggalDibuat", it.tanggalDibuat) }) } }
            konteks.startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "application/json"; putExtra(Intent.EXTRA_TEXT, data.toString(2)) }, "Ekspor bookmark QuranKu"))
        }, modifier = Modifier.padding(top = 12.dp)) { Text("Ekspor JSON") }
        androidx.compose.foundation.lazy.LazyColumn(Modifier.padding(top = 12.dp)) {
            items(daftar, key = { it.id }) { data ->
                ListItem(headlineContent = { Text("Ayat ${data.idAyatAwal}") }, supportingContent = { Text(data.isiCatatan.ifBlank { "Belum ada catatan" }) }, trailingContent = { IconButton(onClick = { model.hapus(data) }) { Text("×") } })
            }
        }
    }
}
