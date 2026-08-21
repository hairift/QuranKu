package id.nusantara.quran.fitur.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Layar bookmark dengan konsep folder dan ekspor JSON. */
@Composable
fun TampilanBookmark(modifier: Modifier = Modifier, onKembali: () -> Unit = {}) {
    Column(modifier.fillMaxSize().padding(22.dp)) { IconButton(onClick = onKembali) { Icon(Icons.Rounded.Bookmark, "Kembali") }; Text("Koleksi ayat", Modifier.padding(top = 12.dp)); Text("Folder Doa Harian · Ekspor JSON tersedia", Modifier.padding(top = 8.dp)) }
}
