package id.nusantara.quran.fitur.topik

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import id.nusantara.quran.domain.model.ModelKategoriTopik
import id.nusantara.quran.domain.model.ModelTopik
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Penjelajah topik: telusuri ayat berdasarkan kategori (akhlak,
 * keluarga, rezeki, dst) atau berdasarkan suasana hati pembaca.
 */
@Composable
fun TampilanTopik(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    bukaAyat: (Int, Int) -> Unit = { _, _ -> },
    model: ModelTampilanTopik = hiltViewModel(),
) {
    val kategori by model.kategori.collectAsState()
    val suasana by model.suasana.collectAsState()

    // 0 = kategori umum, 1 = berdasarkan suasana hati.
    var mode by remember { mutableIntStateOf(0) }
    var kelompokTerpilih by remember { mutableStateOf<ModelKategoriTopik?>(null) }
    var topikTerpilih by remember { mutableStateOf<ModelTopik?>(null) }
    val cacheAyat = remember { mutableStateMapOf<Pair<Int, Int>, ModelAyat?>() }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                when {
                    topikTerpilih != null -> topikTerpilih = null
                    kelompokTerpilih != null -> kelompokTerpilih = null
                    else -> onKembali()
                }
            }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                when {
                    topikTerpilih != null -> topikTerpilih!!.judul
                    kelompokTerpilih != null -> kelompokTerpilih!!.judul
                    else -> "Penjelajah Topik"
                },
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        when {
            // Tingkat 3: daftar ayat dalam satu topik.
            topikTerpilih != null -> {
                val topik = topikTerpilih!!
                LazyColumn(Modifier.padding(top = 8.dp)) {
                    items(topik.rujukan, key = { "${it.first}:${it.second}" }) { (surah, ayat) ->
                        val teksAyat = cacheAyat[surah to ayat]
                        androidx.compose.runtime.LaunchedEffect(surah, ayat) {
                            if (!cacheAyat.containsKey(surah to ayat)) {
                                cacheAyat[surah to ayat] = model.ayatTunggal(surah, ayat)
                            }
                        }
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .clickable { bukaAyat(surah, ayat) }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                        ) {
                            Text(
                                "${model.namaSurah(surah)}: $ayat",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            teksAyat?.let {
                                Text(
                                    it.arab,
                                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                                    fontFamily = KeluargaHurufArab,
                                    fontSize = 20.sp,
                                    lineHeight = 36.sp,
                                    textAlign = TextAlign.Right,
                                )
                                Text(
                                    it.terjemahan,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 6.dp),
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }

            // Tingkat 2: daftar topik dalam satu kelompok.
            kelompokTerpilih != null -> {
                val kelompok = kelompokTerpilih!!
                LazyColumn(Modifier.padding(top = 8.dp)) {
                    item {
                        Text(
                            kelompok.deskripsi,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                    }
                    items(kelompok.topik, key = { it.judul }) { topik ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { topikTerpilih = topik }
                                .padding(vertical = 14.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(Modifier.weight(1f)) {
                                Text(topik.judul, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    "${topik.rujukan.size} ayat",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 2.dp),
                                )
                            }
                            Icon(
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }

            // Tingkat 1: daftar kelompok topik dengan pemilih mode.
            else -> {
                Row(
                    Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    FilterChip(
                        selected = mode == 0,
                        onClick = { mode = 0 },
                        label = { Text("Berdasarkan Kategori") },
                    )
                    FilterChip(
                        selected = mode == 1,
                        onClick = { mode = 1 },
                        label = { Text("Berdasarkan Suasana Hati") },
                    )
                }
                val kelompokAktif = if (mode == 0) kategori else suasana
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(kelompokAktif, key = { it.id }) { kelompok ->
                        Card(
                            Modifier
                                .clickable { kelompokTerpilih = kelompok },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            ),
                        ) {
                            Text(
                                kelompok.judul,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            )
                        }
                    }
                }
                LazyColumn(Modifier.padding(top = 12.dp)) {
                    items(kelompokAktif, key = { "kartu-${it.id}" }) { kelompok ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable { kelompokTerpilih = kelompok },
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Column(Modifier.fillMaxWidth().padding(18.dp)) {
                                Text(kelompok.judul, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    kelompok.deskripsi,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 6.dp),
                                )
                                Text(
                                    "${kelompok.topik.size} topik",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(top = 8.dp),
                                )
                            }
                        }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}
