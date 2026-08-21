package id.nusantara.quran.fitur.pencarian

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Pencarian Arab dan terjemahan dengan ruang voice search. */
@Composable
fun TampilanPencarian(modifier: Modifier = Modifier, onKembali: () -> Unit = {}) {
    var kata by remember { mutableStateOf("") }
    var hasil by remember { mutableStateOf<List<HasilPencarian>>(emptyList()) }
    val konteks = LocalContext.current
    LaunchedEffect(kata) {
        hasil = if (kata.trim().length < 2) emptyList() else withContext(Dispatchers.IO) {
            val isi = konteks.assets.open("data-lokal/quran_lokal.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val daftar = JSONObject(isi).getJSONArray("ayat")
            buildList {
                for (indeks in 0 until daftar.length()) {
                    val ayat = daftar.getJSONObject(indeks)
                    if (ayat.getString("translation").contains(kata, ignoreCase = true) || ayat.getString("arabic_text").contains(kata)) {
                        add(HasilPencarian(ayat.getInt("sura"), ayat.getInt("aya"), ayat.getString("arabic_text"), ayat.getString("translation")))
                    }
                    if (size == 30) break
                }
            }
        }
    }
    Column(modifier.fillMaxSize().padding(22.dp)) {
        IconButton(onClick = onKembali) { Icon(Icons.Rounded.Search, "Kembali") }
        Text("Temukan ayat")
        OutlinedTextField(kata, { kata = it }, Modifier.padding(top = 16.dp), leadingIcon = { Icon(Icons.Rounded.Search, "Cari") }, trailingIcon = { Icon(Icons.Rounded.Mic, "Pencarian suara") }, placeholder = { Text("Arab atau terjemahan") })
        LazyColumn(Modifier.fillMaxWidth().padding(top = 12.dp)) {
            items(hasil) { item ->
                androidx.compose.material3.ListItem(
                    headlineContent = { Text("${item.surah}:${item.ayat}") },
                    supportingContent = { Column { Text(item.arab, textAlign = androidx.compose.ui.text.style.TextAlign.Right, modifier = Modifier.fillMaxWidth()); Text(item.terjemahan) } }
                )
            }
        }
    }
}

private data class HasilPencarian(val surah: Int, val ayat: Int, val arab: String, val terjemahan: String)
