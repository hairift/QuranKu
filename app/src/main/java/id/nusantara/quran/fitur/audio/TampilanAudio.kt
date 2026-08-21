package id.nusantara.quran.fitur.audio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Layar audio dengan pemilih qari dan kendali pemutar persistent. */
@Composable
fun TampilanAudio(modifier: Modifier = Modifier) {
    var diputar by remember { mutableStateOf(false) }
    var qari by remember { mutableStateOf("Mishary Alafasy") }
    Column(modifier.fillMaxSize().padding(22.dp)) {
        Icon(Icons.Rounded.GraphicEq, "Audio")
        Text("Murottal QuranKu", modifier = Modifier.padding(top = 12.dp))
        Text("Pilih qari, lalu dengarkan ayat dengan fokus.", modifier = Modifier.padding(top = 8.dp))
        androidx.compose.material3.Text("Qari aktif: $qari", modifier = Modifier.padding(top = 22.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 10.dp)) {
            androidx.compose.material3.FilterChip(qari == "Mishary Alafasy", { qari = "Mishary Alafasy" }, { Text("Mishary") })
            androidx.compose.material3.FilterChip(qari == "Abdul Basit", { qari = "Abdul Basit" }, { Text("Abdul Basit") })
        }
        Button(onClick = { diputar = !diputar }, modifier = Modifier.padding(top = 22.dp)) { Icon(Icons.Rounded.PlayArrow, "Putar atau jeda"); Text(if (diputar) "  Jeda Al-Baqarah" else "  Putar Al-Baqarah") }
        Text(if (diputar) "Sedang memutar ayat pilihan." else "Pemutar siap digunakan.", modifier = Modifier.padding(top = 18.dp))
    }
}
