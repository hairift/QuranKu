package id.nusantara.quran.fitur.tematik

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
 * Katalog tematik Al-Quran: doa, solusi, adab, dosa besar,
 * dan para nabi — seluruhnya dengan rujukan ayat nyata.
 */
@Composable
fun TampilanTematik(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    model: ModelTampilanTematik = hiltViewModel(),
) {
    val kategori by model.kategori.collectAsState()
    val kategoriAktif by model.kategoriAktif.collectAsState()
    val ayatRujukan by model.ayatRujukan.collectAsState()
    val judulButir by model.judulButir.collectAsState()

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                when {
                    ayatRujukan.isNotEmpty() -> model.tutupButir()
                    kategoriAktif != null -> model.tutupKategori()
                    else -> onKembali()
                }
            }) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Text(
                when {
                    ayatRujukan.isNotEmpty() -> judulButir
                    kategoriAktif != null -> kategoriAktif!!.judul
                    else -> "Tematik Al-Quran"
                },
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        when {
            // Tingkat 3: ayat-ayat rujukan satu butir.
            ayatRujukan.isNotEmpty() -> {
                LazyColumn(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(ayatRujukan, key = { it.id }) { ayat ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        ) {
                            Column(Modifier.padding(18.dp)) {
                                Text(
                                    "QS ${ayat.surah}:${ayat.nomor}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                                Text(
                                    ayat.arab,
                                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                                    fontFamily = KeluargaHurufArab,
                                    fontSize = 24.sp,
                                    lineHeight = 40.sp,
                                    textAlign = TextAlign.Right,
                                )
                                Text(
                                    ayat.terjemahan,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 10.dp),
                                )
                            }
                        }
                    }
                }
            }

            // Tingkat 2: butir-butir dalam satu kategori.
            kategoriAktif != null -> {
                val aktif = kategoriAktif!!
                Text(
                    aktif.deskripsi,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
                LazyColumn {
                    items(aktif.item, key = { it.judul }) { butir ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable { model.bukaButir(butir.judul, butir.rujukan) }
                                .padding(vertical = 14.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                butir.judul,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.weight(1f),
                            )
                            Text(
                                "${butir.rujukan.size} ayat",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Icon(
                                Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }

            // Tingkat 1: daftar kategori.
            else -> {
                LazyColumn(
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(kategori, key = { it.id }) { butir ->
                        Card(
                            Modifier.fillMaxWidth().clickable { model.bukaKategori(butir) },
                            shape = RoundedCornerShape(20.dp),
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(butir.judul, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        butir.deskripsi,
                                        style = MaterialTheme.typography.bodyMedium,
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
