package id.nusantara.quran.fitur.tentang

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.nusantara.quran.R
import id.nusantara.quran.fitur.pengaturan.TAUTAN_DONASI
import id.nusantara.quran.inti.ui.tema.EmasTerang
import id.nusantara.quran.inti.ui.tema.HijauZamrud
import id.nusantara.quran.inti.ui.tema.HijauZamrudTua

/**
 * Tentang Kami: profil aplikasi dan pengembang, sumber data beserta
 * lisensinya, serta tautan donasi untuk mendukung pengembangan.
 */
@Composable
fun TampilanTentang(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
) {
    val konteks = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text("Tentang Kami", style = MaterialTheme.typography.headlineSmall)
        }

        Column(
            Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.quranku_icon_transparan),
                contentDescription = "Logo QuranKu",
                modifier = Modifier.size(84.dp),
            )
            Text(
                "QuranKu",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(
                "Versi 1.3.0 · Gratis · Tanpa Iklan · Offline-First",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Pengembang", style = MaterialTheme.typography.titleMedium)
                Text(
                    "QuranKu dikembangkan oleh Muhammad Arif Triyana sebagai aplikasi Al-Quran " +
                        "digital untuk umat Islam Indonesia. Aplikasi ini dibangun dengan prinsip " +
                        "kemudahan ibadah: konten utama tersedia luring (offline), bebas iklan, dan tanpa pelacakan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(Modifier.padding(18.dp)) {
                Text("Sumber Data & Lisensi", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                val sumber = listOf(
                    "Teks Arab dan terjemahan Indonesia" to "QuranEnc dan Tanzil (domain publik / CC)",
                    "Jadwal sholat 518 kota/kabupaten" to "Data Kemenag RI melalui layanan API MyQuran",
                    "Audio murottal 5 qari" to "EveryAyah / Islamic Network (CDN publik)",
                    "Arsitektur dan kode sumber" to "Kotlin, Jetpack Compose, MVVM, dan Clean Architecture mandiri",
                )
                sumber.forEachIndexed { indeks, (apa, dari) ->
                    Column(Modifier.padding(vertical = 8.dp)) {
                        Text(apa, style = MaterialTheme.typography.titleSmall)
                        Text(
                            dari,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (indeks < sumber.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Card(
            Modifier
                .fillMaxWidth()
                .clickable { konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_DONASI))) },
            shape = RoundedCornerShape(20.dp),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(Brush.linearGradient(listOf(HijauZamrudTua, HijauZamrud)))
                    .padding(18.dp),
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.Favorite,
                        contentDescription = null,
                        tint = EmasTerang,
                    )
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text(
                            "Donasi via Trakteer",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                        )
                        Text(
                            TAUTAN_DONASI,
                            style = MaterialTheme.typography.bodySmall,
                            color = EmasTerang,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }

        Text(
            "Jazakumullah khairan katsiran atas dukungan dan doa Anda.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            textAlign = TextAlign.Center,
        )
    }
}
