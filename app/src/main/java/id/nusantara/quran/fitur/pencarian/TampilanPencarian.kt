package id.nusantara.quran.fitur.pencarian

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Pencarian ayat berdasarkan teks Arab atau terjemahan Indonesia.
 * Menyentuh hasil membuka mushaf tepat di ayat tersebut.
 */
@Composable
fun TampilanPencarian(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    bukaAyat: (Int, Int) -> Unit = { _, _ -> },
    model: ModelTampilanPencarian = hiltViewModel(),
) {
    val kata by model.kataKunci.collectAsState()
    val hasil by model.hasil.collectAsState()
    val sedangMencari by model.sedangMencari.collectAsState()

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text("Cari Ayat", style = MaterialTheme.typography.headlineSmall)
        }
        OutlinedTextField(
            value = kata,
            onValueChange = { model.ketik(it) },
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = {
                if (sedangMencari) CircularProgressIndicator(modifier = Modifier.padding(12.dp), strokeWidth = 2.dp)
            },
            placeholder = { Text("Teks Arab atau terjemahan…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )
        if (kata.trim().length in 1..2) {
            Text(
                "Ketik minimal 3 huruf untuk mencari",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        LazyColumn {
            items(hasil, key = { it.id }) { ayat ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable { bukaAyat(ayat.surah, ayat.nomor) }
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                ) {
                    Text(
                        "QS ${ayat.surah}:${ayat.nomor} · Juz ${ayat.juz}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        ayat.arab,
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        fontFamily = KeluargaHurufArab,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Right,
                    )
                    Text(
                        ayat.terjemahan,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                HorizontalDivider()
            }
        }
    }
}
