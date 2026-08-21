package id.nusantara.quran.fitur.pencarian

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Pencarian Arab dan terjemahan dengan ruang voice search. */
@Composable
fun TampilanPencarian(modifier: Modifier = Modifier, onKembali: () -> Unit = {}) {
    var kata by remember { mutableStateOf("") }
    Column(modifier.fillMaxSize().padding(22.dp)) {
        IconButton(onClick = onKembali) { Icon(Icons.Rounded.Search, "Kembali") }
        Text("Temukan ayat")
        OutlinedTextField(kata, { kata = it }, Modifier.padding(top = 16.dp), leadingIcon = { Icon(Icons.Rounded.Search, "Cari") }, trailingIcon = { Icon(Icons.Rounded.Mic, "Pencarian suara") }, placeholder = { Text("Arab atau terjemahan") })
    }
}
