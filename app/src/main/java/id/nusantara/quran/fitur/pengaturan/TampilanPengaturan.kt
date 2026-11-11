package id.nusantara.quran.fitur.pengaturan

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
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
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.inti.ui.tema.EmasTerang
import id.nusantara.quran.inti.ui.tema.HijauZamrud
import id.nusantara.quran.inti.ui.tema.HijauZamrudTua

/** Tautan donasi resmi pengembang QuranKu. */
const val TAUTAN_DONASI = "https://trakteer.id/fira73"

/** Tautan aplikasi di Google Play untuk dibagikan. */
const val TAUTAN_PLAY_STORE = "https://play.google.com/store/apps/details?id=id.nusantara.quran"

/**
 * Ruang "Saya": pengaturan tampilan, pengingat, kota jadwal sholat,
 * manajemen penyimpanan, cadangan data, serta dukungan komunitas.
 */
@Composable
fun TampilanPengaturan(
    modifier: Modifier = Modifier,
    bukaTentang: () -> Unit = {},
    bukaNusantara: () -> Unit = {},
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
            .padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        // Header identitas QuranKu di ruang Saya
        Row(
            Modifier.fillMaxWidth().padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.size(54.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.quranku_icon_transparan),
                        contentDescription = "Logo QuranKu",
                        modifier = Modifier.size(38.dp),
                    )
                }
            }
            Column(Modifier.padding(start = 14.dp)) {
                Text("Pengaturan & Akun Saya", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "QuranKu Nusantara · Gratis · Tanpa Iklan · Offline-First",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // --- Tampilan & Pengingat ---
        Text(
            "Tampilan & Pengingat",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column {
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Mode Gelap", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Tema malam zamrud yang teduh untuk membaca") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.DarkMode,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Switch(checked = temaGelap, onCheckedChange = { model.aturTema(it) })
                    },
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Pengingat Ayat Harian", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Notifikasi ayat pilihan pukul ${"%02d".format(jamPengingat)}:00") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Switch(checked = pengingatAyat, onCheckedChange = { model.alihkanPengingatAyat(it) })
                    },
                )
                if (pengingatAyat) {
                    Text(
                        "Jam pengingat: ${"%02d".format(jamPengingat)}:00",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    Slider(
                        value = jamPengingat.toFloat(),
                        onValueChange = { model.aturJamPengingat(it.toInt()) },
                        valueRange = 4f..21f,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))

                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Kota Jadwal Sholat & Kiblat", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("$namaKota · Ketuk untuk membuka pengaturan lokasi") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Place,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable(onClick = bukaNusantara),
                )
            }
        }

        // --- Penyimpanan & Cadangan Data ---
        Spacer(Modifier.height(20.dp))
        Text(
            "Penyimpanan & Cadangan Data",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column {
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Pembersihan Penyimpanan", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Cache sementara: $ukuranCache") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable(enabled = !sedangProses) { tanyaBersihkan = true },
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Ekspor Cadangan Data", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Simpan bookmark, riwayat, dan pengaturan ke berkas JSON") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Upload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable(enabled = !sedangProses) {
                        peluncurEkspor.launch("quranku_cadangan.json")
                    },
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Impor Cadangan Data", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Pulihkan cadangan yang pernah diekspor") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Download,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable(enabled = !sedangProses) {
                        peluncurImpor.launch(arrayOf("application/json", "text/plain", "*/*"))
                    },
                )
            }
        }

        // --- Komunitas ---
        Spacer(Modifier.height(20.dp))
        Text(
            "Dukung QuranKu",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column {
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Beri Rating", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Nilai QuranKu di Google Play") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        val niatToko = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=id.nusantara.quran"))
                        try {
                            konteks.startActivity(niatToko)
                        } catch (tidakAda: ActivityNotFoundException) {
                            konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_PLAY_STORE)))
                        }
                    },
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Berbagi Aplikasi", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Ajak keluarga dan sahabat membaca Al-Quran") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Share,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        val niatBagi = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "QuranKu — Al-Quran digital Nusantara gratis, tanpa iklan, dan bisa offline. " +
                                    "Unduh di: $TAUTAN_PLAY_STORE",
                            )
                        }
                        konteks.startActivity(Intent.createChooser(niatBagi, "Bagikan QuranKu"))
                    },
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Donasi Pengembang", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Dukung pengembangan lewat Trakteer") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable {
                        konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_DONASI)))
                    },
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ListItem(
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    headlineContent = { Text("Tentang Kami & Lisensi Data", style = MaterialTheme.typography.titleSmall) },
                    supportingContent = { Text("Profil pengembang, sumber data resmi, dan lisensi") },
                    leadingContent = {
                        Icon(
                            Icons.Rounded.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    trailingContent = {
                        Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null)
                    },
                    modifier = Modifier.clickable(onClick = bukaTentang),
                )
            }
        }

        // Kartu donasi menonjol di bagian bawah.
        Spacer(Modifier.height(20.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_DONASI))) },
            shape = RoundedCornerShape(22.dp),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(HijauZamrudTua, HijauZamrud)))
                    .padding(20.dp),
            ) {
                Column(Modifier.fillMaxWidth()) {
                    Text(
                        "Bantu QuranKu tetap gratis & tanpa iklan",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                    Text(
                        "Donasi seikhlasnya melalui Trakteer membantu pemeliharaan konten dan pengembangan fitur baru. Jazakumullah khairan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.padding(top = 8.dp),
                    )
                    Text(
                        TAUTAN_DONASI,
                        style = MaterialTheme.typography.labelLarge,
                        color = EmasTerang,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (tanyaBersihkan) {
        AlertDialog(
            onDismissRequest = { tanyaBersihkan = false },
            title = { Text("Bersihkan Penyimpanan?") },
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
