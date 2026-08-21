package id.nusantara.quran.fitur.tentang

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import id.nusantara.quran.fitur.pengaturan.TAUTAN_DONASI

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
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text("Tentang Kami", style = MaterialTheme.typography.headlineSmall)
        }

        Text(
            "QuranKu",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            "Versi 1.2.0 · gratis · tanpa iklan · offline-first",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(20.dp))
        Text("Pengembang", style = MaterialTheme.typography.titleMedium)
        Text(
            "QuranKu dikembangkan oleh Muhammad Arif Triyana sebagai aplikasi Al-Quran " +
                "digital untuk umat Islam Indonesia. Aplikasi ini dibangun dengan prinsip " +
                "sederhana: konten utama tersedia offline, tanpa iklan, dan tanpa pelacakan.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )

        Spacer(Modifier.height(20.dp))
        Text("Sumber Data & Lisensi", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        val sumber = listOf(
            "Teks Arab dan terjemahan Indonesia" to "QuranEnc dan Tanzil (domain publik / CC)",
            "Jadwal sholat 518 kota" to "Data Kemenag RI melalui API MyQuran",
            "Audio murottal" to "EveryAyah / Islamic Network (CDN publik)",
            "Kode sumber aplikasi" to "GPL v3, dikembangkan di atas QuranApp (AlfaazPlus)",
        )
        sumber.forEach { (apa, dari) ->
            Column(Modifier.padding(vertical = 8.dp)) {
                Text(apa, style = MaterialTheme.typography.bodyLarge)
                Text(dari, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            HorizontalDivider()
        }

        Spacer(Modifier.height(20.dp))
        Text("Dukung Kami", style = MaterialTheme.typography.titleMedium)
        Text(
            "QuranKu akan selalu gratis dan tanpa iklan. Jika aplikasi ini bermanfaat, " +
                "Anda bisa mendukung biaya pengembangan dan konten melalui donasi seikhlasnya.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
        Card(
            Modifier
                .fillMaxWidth()
                .padding(top = 14.dp)
                .clickable { konteks.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TAUTAN_DONASI))) },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Row(
                Modifier.fillMaxWidth().padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Rounded.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Column(Modifier.weight(1f).padding(start = 12.dp)) {
                    Text(
                        "Donasi via Trakteer",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Text(
                        TAUTAN_DONASI,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }

        Text(
            "Jazakumullah khairan katsiran atas dukungan Anda.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            textAlign = TextAlign.Center,
        )
    }
}
