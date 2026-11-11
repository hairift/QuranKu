package id.nusantara.quran.fitur.sains

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Quran & Sains: 18 fenomena ilmiah yang disebutkan Al-Quran beserta
 * ayat rujukannya. Menyentuh rujukan membuka ayat di Mushaf.
 */
@Composable
fun TampilanSains(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    bukaAyat: (Int, Int) -> Unit = { _, _ -> },
    model: ModelTampilanSains = hiltViewModel(),
) {
    val daftar by model.daftar.collectAsState()
    var terbuka by remember { mutableStateOf<String?>(null) }
    val cacheAyat = remember { mutableStateMapOf<Pair<Int, Int>, ModelAyat?>() }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column {
                Text("Quran & Sains", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Isyarat ilmiah tentang alam semesta di dalam Al-Quran",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 12.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(daftar, key = { it.judul }) { butir ->
                val sedangTerbuka = terbuka == butir.judul
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { terbuka = if (sedangTerbuka) null else butir.judul },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(
                        1.dp,
                        if (sedangTerbuka) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    Column(Modifier.fillMaxWidth().padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(38.dp),
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.Science,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp),
                                    )
                                }
                            }
                            Text(
                                butir.judul,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f).padding(start = 12.dp),
                            )
                            Icon(
                                if (sedangTerbuka) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                contentDescription = if (sedangTerbuka) "Tutup" else "Buka",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(
                            butir.ringkasan,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 10.dp),
                        )

                        AnimatedVisibility(visible = sedangTerbuka) {
                            Column(Modifier.padding(top = 12.dp)) {
                                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                                Text(
                                    "Ayat Terkait (Ketuk untuk membuka di Mushaf)",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                )
                                butir.rujukan.forEach { (surah, ayat) ->
                                    val teksAyat = cacheAyat[surah to ayat]
                                    LaunchedEffect(surah, ayat) {
                                        if (!cacheAyat.containsKey(surah to ayat)) {
                                            cacheAyat[surah to ayat] = model.ayatTunggal(surah, ayat)
                                        }
                                    }
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { bukaAyat(surah, ayat) },
                                        shape = RoundedCornerShape(14.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                                    ) {
                                        Column(Modifier.padding(12.dp)) {
                                            Text(
                                                "QS. ${model.namaSurah(surah)}: $ayat",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                            teksAyat?.let {
                                                Text(
                                                    it.arab,
                                                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                                    fontFamily = KeluargaHurufArab,
                                                    fontSize = 20.sp,
                                                    lineHeight = 34.sp,
                                                    textAlign = TextAlign.Right,
                                                )
                                                Text(
                                                    it.terjemahan,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.padding(top = 6.dp),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text(
                    "Ringkasan disusun sebagai bahan tadabbur; penjelasan ilmiah merujuk pada literatur sains populer yang umum dikutip.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp),
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
