package id.nusantara.quran.fitur.tematik

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
 * Katalog tematik Al-Quran: doa, solusi kehidupan, adab keseharian,
 * peringatan dosa besar, dan kisah para nabi dengan rujukan ayat langsung ke Mushaf.
 */
@Composable
fun TampilanTematik(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    bukaAyat: (Int, Int) -> Unit = { _, _ -> },
    model: ModelTampilanTematik = hiltViewModel(),
) {
    val kategori by model.kategori.collectAsState()
    val kategoriAktif by model.kategoriAktif.collectAsState()
    val ayatRujukan by model.ayatRujukan.collectAsState()
    val judulButir by model.judulButir.collectAsState()

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = {
                when {
                    ayatRujukan.isNotEmpty() -> model.tutupButir()
                    kategoriAktif != null -> model.tutupKategori()
                    else -> onKembali()
                }
            }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column {
                Text(
                    when {
                        ayatRujukan.isNotEmpty() -> judulButir
                        kategoriAktif != null -> kategoriAktif!!.judul
                        else -> "Kajian Tematik Al-Quran"
                    },
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    when {
                        ayatRujukan.isNotEmpty() -> "Ketuk kartu ayat untuk membuka di Mushaf"
                        kategoriAktif != null -> kategoriAktif!!.deskripsi
                        else -> "Indeks doa, solusi batin, adab, dan kisah para nabi"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        when {
            // Tingkat 3: ayat-ayat rujukan satu butir.
            ayatRujukan.isNotEmpty() -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(ayatRujukan, key = { it.id }) { ayat ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { bukaAyat(ayat.surah, ayat.nomor) },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        ) {
                            Column(Modifier.padding(18.dp)) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        "Surah ${ayat.surah} : Ayat ${ayat.nomor}",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                    ) {
                                        Text(
                                            "Juz ${ayat.juz}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        )
                                    }
                                }
                                Text(
                                    ayat.arab,
                                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                                    fontFamily = KeluargaHurufArab,
                                    fontSize = 24.sp,
                                    lineHeight = 40.sp,
                                    textAlign = TextAlign.Right,
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f),
                                )
                                Text(
                                    ayat.terjemahan,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            // Tingkat 2: butir-butir dalam satu kategori.
            kategoriAktif != null -> {
                val aktif = kategoriAktif!!
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(aktif.item, key = { it.judul }) { butir ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { model.bukaButir(butir.judul, butir.rujukan) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    butir.judul,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.weight(1f),
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                ) {
                                    Text(
                                        "${butir.rujukan.size} ayat",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    )
                                }
                                Icon(
                                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            // Tingkat 1: daftar kategori.
            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(kategori, key = { it.id }) { butir ->
                        Card(
                            Modifier.fillMaxWidth().clickable { model.bukaKategori(butir) },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.size(44.dp),
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Rounded.AutoStories,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                        )
                                    }
                                }
                                Column(Modifier.weight(1f).padding(horizontal = 14.dp)) {
                                    Text(butir.judul, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        butir.deskripsi,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 4.dp),
                                    )
                                }
                                Icon(
                                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}
