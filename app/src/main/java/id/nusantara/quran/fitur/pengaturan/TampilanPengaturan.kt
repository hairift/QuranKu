package id.nusantara.quran.fitur.pengaturan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Ruang "Saya": pengaturan tampilan, pengingat, kota,
 * serta informasi sumber data dan atribusi lisensi.
 */
@Composable
fun TampilanPengaturan(
    modifier: Modifier = Modifier,
    gelap: Boolean = false,
    onGantiTema: () -> Unit = {},
    model: ModelTampilanPengaturan = hiltViewModel(),
) {
    val temaGelap by model.temaGelap.collectAsState()
    val pengingatAyat by model.pengingatAyat.collectAsState()
    val jamPengingat by model.jamPengingat.collectAsState()
    val namaKota by model.namaKota.collectAsState()

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        Text("Saya", style = MaterialTheme.typography.headlineMedium)
        Text(
            "QuranKu · gratis · tanpa iklan · offline-first",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
        )

        ListItem(
            headlineContent = { Text("Mode gelap") },
            supportingContent = { Text("Tema malam bernuansa hijau hutan") },
            leadingContent = { Icon(Icons.Rounded.DarkMode, contentDescription = null) },
            trailingContent = {
                Switch(checked = temaGelap, onCheckedChange = { model.aturTema(it) })
            },
        )
        HorizontalDivider()

        ListItem(
            headlineContent = { Text("Pengingat ayat harian") },
            supportingContent = { Text("Notifikasi ayat pilihan pukul ${"%02d".format(jamPengingat)}:00") },
            leadingContent = { Icon(Icons.Rounded.Notifications, contentDescription = null) },
            trailingContent = {
                Switch(checked = pengingatAyat, onCheckedChange = { model.alihkanPengingatAyat(it) })
            },
        )
        if (pengingatAyat) {
            Text(
                "Jam pengingat: ${"%02d".format(jamPengingat)}:00",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Slider(
                value = jamPengingat.toFloat(),
                onValueChange = { model.aturJamPengingat(it.toInt()) },
                valueRange = 4f..21f,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
        HorizontalDivider()

        ListItem(
            headlineContent = { Text("Kota jadwal sholat") },
            supportingContent = { Text(namaKota) },
            leadingContent = { Icon(Icons.Rounded.Place, contentDescription = null) },
        )
        HorizontalDivider()

        ListItem(
            headlineContent = { Text("Tentang QuranKu") },
            supportingContent = {
                Text(
                    "Dikembangkan oleh Muhammad Arif Triyana. Berlisensi GPL v3, " +
                        "dibangun di atas kode sumber terbuka QuranApp (AlfaazPlus). " +
                        "Sumber data: QuranEnc, Tanzil, Kemenag via MyQuran, dan EveryAyah.",
                )
            },
            leadingContent = { Icon(Icons.Rounded.Info, contentDescription = null) },
        )
        Spacer(Modifier.height(24.dp))
    }
}
