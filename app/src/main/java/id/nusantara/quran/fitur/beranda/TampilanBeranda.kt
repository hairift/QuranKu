package id.nusantara.quran.fitur.beranda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.nusantara.quran.inti.ui.tema.EmasNusantara

/** Beranda dengan ayat harian, progres baca, dan pintasan Nusantara. */
@Composable
fun TampilanBeranda(modifier: Modifier = Modifier, bukaMushaf: () -> Unit = {}, bukaAudio: () -> Unit = {}, bukaPencarian: () -> Unit = {}, bukaBookmark: () -> Unit = {}, bukaTematik: () -> Unit = {}) {
    Column(modifier.fillMaxSize().padding(horizontal = 22.dp, vertical = 28.dp)) {
        Text("Assalamu'alaikum", style = MaterialTheme.typography.titleMedium)
        Text("Ruang teduh untuk membaca", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(24.dp))
        Card(
            Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(Modifier.padding(22.dp)) {
                Text("AYAT HARI INI", color = EmasNusantara, fontSize = 12.sp)
                Spacer(Modifier.height(12.dp))
                Text("فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", color = MaterialTheme.colorScheme.onPrimary, fontSize = 27.sp)
                Spacer(Modifier.height(8.dp))
                Text("Karena sesungguhnya sesudah kesulitan itu ada kemudahan.", color = MaterialTheme.colorScheme.onPrimary)
                Text("QS. Al-Insyirah: 5", color = EmasNusantara, modifier = Modifier.padding(top = 12.dp))
            }
        }
        Spacer(Modifier.height(18.dp))
        Card(Modifier.fillMaxWidth().clickable(onClick = bukaMushaf), shape = RoundedCornerShape(20.dp)) {
            Column(Modifier.padding(18.dp)) {
                Text("Lanjutkan membaca", style = MaterialTheme.typography.titleMedium)
                Text("Al-Baqarah · Ayat 255", modifier = Modifier.padding(top = 5.dp))
                Text("12% selesai", color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(top = 10.dp))
            }
        }
        Spacer(Modifier.height(22.dp))
        Text("Pintasan Nusantara", style = MaterialTheme.typography.titleMedium)
        Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Pintasan(Icons.Rounded.Schedule, "Jadwal", bukaAudio)
            Pintasan(Icons.Rounded.Explore, "Kiblat", bukaAudio)
            Pintasan(Icons.Rounded.SelfImprovement, "Doa", bukaMushaf)
            Pintasan(Icons.Rounded.MenuBook, "Juz", bukaMushaf)
        }
        Row(Modifier.fillMaxWidth().padding(top = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = bukaPencarian, modifier = Modifier.weight(1f)) { Icon(Icons.Rounded.Search, "Pencarian"); Text("Cari") }
            OutlinedButton(onClick = bukaBookmark, modifier = Modifier.weight(1f)) { Icon(Icons.Rounded.Bookmark, "Bookmark"); Text("Simpan") }
            OutlinedButton(onClick = bukaTematik, modifier = Modifier.weight(1f)) { Icon(Icons.Rounded.AutoStories, "Tematik"); Text("Tema") }
        }
    }
}

@Composable
private fun Pintasan(ikon: androidx.compose.ui.graphics.vector.ImageVector, label: String, aksi: () -> Unit) {
    Card(Modifier.width(76.dp).clickable(onClick = aksi), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(vertical = 14.dp), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
            Icon(ikon, label)
            Text(label, fontSize = 12.sp, modifier = Modifier.padding(top = 7.dp))
        }
    }
}
