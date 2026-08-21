package id.nusantara.quran.fitur.pengaturan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Ruang pengaturan, sumber data, dan pilihan tampilan. */
@Composable
fun TampilanPengaturan(modifier: Modifier = Modifier, gelap: Boolean, onGantiTema: () -> Unit) {
    Column(modifier.fillMaxSize().padding(22.dp)) {
        Text("Saya", style = MaterialTheme.typography.headlineMedium)
        Text("QuranKu · offline-first", modifier = Modifier.padding(top = 6.dp, bottom = 22.dp))
        ListItem(headlineContent = { Text("Mode gelap") }, leadingContent = { Icon(Icons.Rounded.DarkMode, "Mode gelap") }, trailingContent = {
            Switch(checked = gelap, onCheckedChange = { onGantiTema() })
        })
        ListItem(headlineContent = { Text("Tentang dan sumber data") }, supportingContent = { Text("Tanzil · QuranEnc · Kemenag · EveryAyah") }, leadingContent = { Icon(Icons.Rounded.Info, "Tentang") })
    }
}
