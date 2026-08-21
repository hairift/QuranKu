package id.nusantara.quran.fitur.pengaturan

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/** Tautan donasi resmi pengembang QuranKu. */
const val TAUTAN_DONASI = "https://trakteer.id/fira73"

/** Tautan aplikasi di Google Play untuk dibagikan. */
const val TAUTAN_PLAY_STORE = "https://play.google.com/store/apps/details?id=id.nusantara.quran"

/**
 * Ruang "Saya": pengaturan tampilan, pengingat, kota, penyimpanan,
 * cadangan data, serta aksi komunitas (rating, berbagi, donasi).
 */
@Composable
fun TampilanPengaturan(
    modifier: Modifier = Modifier,
    bukaTentang: () -> Unit = {},
    model: ModelTampilanPengaturan = hiltViewModel(),
) {
    val konteks = LocalContext.current
    val temaGelap by model.temaGelap.collectAsState()
    val pengingatAyat by model.pengingatAyat.collectAsState()
    val jamPengingat by model.jamPengingat.collectAsState()
    val namaKota by model.namaKota.collectAsState()
    val ukuranCache by model.ukuranCache.collectAsState()
    val pesanStatus by model.pesanStatus.collectAsState()
    val sedangProses by model.sedangProses.collectAsState()
    var tanyaBersihkan by remember { mutableStateOf(false) }

    // Pemilih berkas tujuan ekspor dan sumber impor via Storage Access Framework.
    val peluncurEkspor = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri -> uri?.let { model.eksporData(it) } }
    val peluncurImpor = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri -> uri?.let { model.imporData(it) } }

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

        // --- Tampilan & pengingat ---
        Text("Tampilan & Pengingat", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
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

        // --- Penyimpanan ---
        Spacer(Modifier.height(20.dp))
        Text("Penyimpanan & Data", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        ListItem(
            headlineContent = { Text("Pembersihan penyimpanan") },
            supportingContent = { Text("Cache sementara: $ukuranCache") },
            leadingContent = { Icon(Icons.Rounded.Delete, contentDescription = null) },
            modifier = Modifier.clickable(enabled = !sedangProses) { tanyaBersihkan = true },
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Ekspor data") },
            supportingContent = { Text("Simpan bookmark, riwayat, dan pengaturan ke berkas JSON") },
            leadingContent = { Icon(Icons.Rounded.Upload, contentDescription = null) },
            modifier = Modifier.clickable(enabled = !sedangProses) {
                peluncurEkspor.launch("quranku_cadangan.json")
            },
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Impor data") },
            supportingContent = { Text("Pulihkan cadangan yang pernah diekspor") },
            leadingContent = { Icon(Icons.Rounded.Download, contentDescription = null) },
            modifier = Modifier.clickable(enabled = !sedangProses) {
                peluncurImpor.launch(arrayOf("application/json", "text/plain", "*/*"))
            },
        )

        // --- Komunitas ---
        Spacer(Modifier.height(20.dp))
        Text("Dukung QuranKu", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        ListItem(
            headlineContent = { Text("Beri rating") },
            supportingContent = { Text("Nilai QuranKu di Google Play") },
            leadingContent = { Icon(Icons.Rounded.Star, contentDescription = null) },
            modifier = Modifier.clickable {
                val niatToko = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=id.nusantara.quran"))
                try {
                    konteks.startActivity(niatToko)
                } catch (tidakAda: ActivityNotFoundException) {
                    konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_PLAY_STORE)))
                }
            },
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Berbagi aplikasi") },
            supportingContent = { Text("Ajak keluarga dan teman membaca Al-Quran") },
            leadingContent = { Icon(Icons.Rounded.Share, contentDescription = null) },
            modifier = Modifier.clickable {
                val niatBagi = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "QuranKu — Al-Quran digital gratis, tanpa iklan, dan bisa offline. " +
                            "Unduh di: $TAUTAN_PLAY_STORE",
                    )
                }
                konteks.startActivity(Intent.createChooser(niatBagi, "Bagikan QuranKu"))
            },
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Donasi") },
            supportingContent = { Text("Dukung pengembangan lewat Trakteer") },
            leadingContent = {
                Icon(Icons.Rounded.Favorite, contentDescription = null, tint = MaterialTheme.colorScheme.error)
            },
            modifier = Modifier.clickable {
                konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_DONASI)))
            },
        )
        HorizontalDivider()
        ListItem(
            headlineContent = { Text("Tentang Kami") },
            supportingContent = { Text("Profil pengembang, sumber data, dan lisensi") },
            leadingContent = { Icon(Icons.Rounded.Info, contentDescription = null) },
            modifier = Modifier.clickable(onClick = bukaTentang),
        )

        // Kartu donasi menonjol di bagian bawah.
        Spacer(Modifier.height(20.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_DONASI))) },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Column(Modifier.fillMaxWidth().padding(20.dp)) {
                Text(
                    "Bantu QuranKu tetap gratis & tanpa iklan",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    "Donasi seikhlasnya melalui Trakteer membantu biaya server, konten, dan pengembangan fitur baru. Jazakumullah khairan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(top = 8.dp),
                )
                Text(
                    TAUTAN_DONASI,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (tanyaBersihkan) {
        AlertDialog(
            onDismissRequest = { tanyaBersihkan = false },
            title = { Text("Bersihkan penyimpanan?") },
            text = {
                Text("Cache sementara ($ukuranCache) akan dihapus. Bookmark, riwayat, dan pengaturan Anda tetap aman.")
            },
            confirmButton = {
                TextButton(onClick = {
                    model.bersihkanPenyimpanan()
                    tanyaBersihkan = false
                }) { Text("Bersihkan") }
            },
            dismissButton = { TextButton(onClick = { tanyaBersihkan = false }) { Text("Batal") } },
        )
    }

    pesanStatus?.let { pesan ->
        AlertDialog(
            onDismissRequest = { model.tutupPesan() },
            title = { Text("QuranKu") },
            text = { Text(pesan) },
            confirmButton = { TextButton(onClick = { model.tutupPesan() }) { Text("OK") } },
        )
    }
}
