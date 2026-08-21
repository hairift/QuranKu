package id.nusantara.quran.fitur.mushaf

import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Mushaf ayat-per-ayat dengan zoom gestur dan pilihan mode tampilan. */
@Composable
fun TampilanMushaf(modifier: Modifier = Modifier) {
    var skala by remember { mutableFloatStateOf(1f) }
    var tersimpan by remember { mutableStateOf(setOf<String>()) }
    var ayat by remember { mutableStateOf<List<AyatLokal>>(emptyList()) }
    val konteks = LocalContext.current
    LaunchedEffect(Unit) {
        ayat = withContext(Dispatchers.IO) {
            val isi = konteks.assets.open("data-lokal/quran_lokal.json").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val daftar = JSONObject(isi).getJSONArray("ayat")
            buildList {
                for (indeks in 0 until daftar.length()) {
                    val item = daftar.getJSONObject(indeks)
                    if (item.getInt("sura") == 2) add(AyatLokal(item.getInt("aya"), item.getString("arabic_text"), item.getString("translation")))
                }
            }
        }
    }
    Column(modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 18.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text("Al-Baqarah", fontSize = 24.sp); Text("Madaniyah · 286 ayat") }
            Row {
                IconButton(onClick = {}) { Icon(Icons.Rounded.Search, "Cari ayat") }
                IconButton(onClick = {}) { Icon(Icons.Rounded.MoreVert, "Menu mushaf") }
            }
        }
        Row(Modifier.padding(vertical = 14.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(true, {}, { Text("Ayat") })
            FilterChip(false, {}, { Text("Mushaf 15 baris") })
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(ayat.ifEmpty { listOf(AyatLokal(1, "الٓمٓ", "Alif Lam Mim")) }) { item ->
                KolomAyat(item, skala, item.arab in tersimpan, { tersimpan = if (item.arab in tersimpan) tersimpan - item.arab else tersimpan + item.arab }, Modifier.pointerInput(Unit) {
                    detectTransformGestures { _, _, perubahan, _ -> skala = (skala * perubahan).coerceIn(0.8f, 1.8f) }
                })
            }
        }
    }
}

@Composable
private fun KolomAyat(item: AyatLokal, skala: Float, tersimpan: Boolean, ubahBookmark: () -> Unit, modifier: Modifier) {
    androidx.compose.material3.Card(Modifier.fillMaxWidth()) {
        Column(modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("۝", color = androidx.compose.material3.MaterialTheme.colorScheme.secondary)
                androidx.compose.material3.IconButton(onClick = ubahBookmark) { Icon(Icons.Rounded.BookmarkBorder, if (tersimpan) "Hapus bookmark" else "Simpan bookmark") }
            }
            Text(item.arab, Modifier.graphicsLayer(scaleX = skala, scaleY = skala).fillMaxWidth(), fontSize = 27.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Right)
            Text(item.terjemahan, Modifier.padding(top = 12.dp))
        }
    }
}

private data class AyatLokal(val nomor: Int, val arab: String, val terjemahan: String)
