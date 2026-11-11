package id.nusantara.quran.fitur.dzikir

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Layar Dzikir Harian & Penghitung Tasbih Digital.
 * Dilengkapi penghitung sentuh per bacaan dengan umpan balik getar halus (haptic).
 */
@Composable
fun TampilanDzikir(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    model: ModelTampilanDzikir = hiltViewModel(),
) {
    val daftar by model.daftar.collectAsState()
    val getar = LocalHapticFeedback.current

    // Pilihan tab: "pagi", "petang", "sholat", atau "tasbih"
    var tabAktif by remember { mutableStateOf("pagi") }
    val hitunganDzikir = remember { mutableStateMapOf<String, Int>() }

    // State khusus mode Tasbih Digital mandiri
    var hitunganTasbih by remember { mutableIntStateOf(0) }
    var targetTasbih by remember { mutableIntStateOf(33) }
    var indeksKalimat by remember { mutableIntStateOf(0) }
    val daftarKalimatTasbih = listOf(
        Triple("سُبْحَانَ اللَّهِ", "Subhanallah", "Mahasuci Allah"),
        Triple("الْحَمْدُ لِلَّهِ", "Alhamdulillah", "Segala puji bagi Allah"),
        Triple("اللَّهُ أَكْبَرُ", "Allahu Akbar", "Allah Mahabesar"),
        Triple("لَا إِلَٰهَ إِلَّا اللَّهُ", "La ilaha illallah", "Tiada Tuhan selain Allah"),
        Triple("أَسْتَغْفِرُ اللَّهَ", "Astaghfirullah", "Aku memohon ampun kepada Allah"),
    )

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column(Modifier.weight(1f)) {
                Text("Dzikir & Tasbih", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Dzikir pagi, petang, seusai sholat, dan tasbih digital",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            if (tabAktif != "tasbih") {
                IconButton(onClick = { hitunganDzikir.clear() }) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Atur ulang hitungan")
                }
            }
        }

        // Baris chip pemilih mode
        Row(
            Modifier.fillMaxWidth().padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val daftarTab = listOf(
                "pagi" to "Pagi",
                "petang" to "Petang",
                "sholat" to "Seusai Sholat",
                "tasbih" to "Tasbih",
            )
            daftarTab.forEach { (kode, nama) ->
                val aktif = tabAktif == kode
                FilterChip(
                    selected = aktif,
                    onClick = { tabAktif = kode },
                    label = { Text(nama) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        if (tabAktif == "tasbih") {
            // Mode Tasbih Digital Mandiri
            val (arab, latin, arti) = daftarKalimatTasbih[indeksKalimat]
            Column(
                Modifier.fillMaxSize().padding(bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(
                        Modifier.fillMaxWidth().padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            arab,
                            fontFamily = KeluargaHurufArab,
                            fontSize = 34.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(latin, style = MaterialTheme.typography.titleMedium)
                        Text(
                            arti,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf(33, 99, 100).forEach { batas ->
                                FilterChip(
                                    selected = targetTasbih == batas,
                                    onClick = { targetTasbih = batas },
                                    label = { Text("Target $batas") },
                                )
                            }
                        }
                    }
                }

                // Lingkaran sentuh Tasbih besar
                Box(
                    modifier = Modifier.size(230.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        progress = { (hitunganTasbih.toFloat() / targetTasbih.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.outline,
                        strokeWidth = 10.dp,
                    )
                    Surface(
                        modifier = Modifier
                            .size(196.dp)
                            .clip(CircleShape)
                            .clickable {
                                getar.performHapticFeedback(HapticFeedbackType.LongPress)
                                hitunganTasbih++
                            },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                    ) {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            Text(
                                "$hitunganTasbih",
                                style = MaterialTheme.typography.displaySmall.copy(fontSize = 46.sp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "dari $targetTasbih",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Rounded.TouchApp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                                    modifier = Modifier.size(16.dp),
                                )
                                Text(
                                    " Sentuh untuk menghitung",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                                )
                            }
                        }
                    }
                }

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedButton(
                        onClick = { hitunganTasbih = 0 },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Icon(Icons.Rounded.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("  Ulangi (0)")
                    }
                    Button(
                        onClick = {
                            indeksKalimat = (indeksKalimat + 1) % daftarKalimatTasbih.size
                            hitunganTasbih = 0
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                    ) {
                        Text("Ganti Kalimat")
                    }
                }
            }
        } else {
            val daftarAktif = remember(daftar, tabAktif) { model.berdasarkanKategori(tabAktif) }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(daftarAktif, key = { it.id }) { butir ->
                    val saatIni = hitunganDzikir[butir.id] ?: 0
                    val selesai = saatIni >= butir.target
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(
                            1.dp,
                            if (selesai) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        ),
                    ) {
                        Column(Modifier.padding(18.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    butir.judul,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f),
                                )
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selesai) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.secondaryContainer,
                                ) {
                                    Text(
                                        "Dibaca ${butir.target}x",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (selesai) MaterialTheme.colorScheme.onPrimaryContainer
                                        else MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    )
                                }
                            }

                            Text(
                                butir.arab,
                                modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
                                fontFamily = KeluargaHurufArab,
                                fontSize = 24.sp,
                                lineHeight = 42.sp,
                                textAlign = TextAlign.Right,
                                color = MaterialTheme.colorScheme.onSurface,
                            )

                            Text(
                                butir.latin,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(top = 10.dp),
                            )

                            Text(
                                butir.arti,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 6.dp),
                            )

                            Text(
                                "Keutamaan: ${butir.keutamaan}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                modifier = Modifier.padding(top = 8.dp),
                            )

                            Spacer(Modifier.height(14.dp))
                            LinearProgressIndicator(
                                progress = { (saatIni.toFloat() / butir.target.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                            )
                            Spacer(Modifier.height(10.dp))

                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    if (selesai) "Target selesai ($saatIni/${butir.target})"
                                    else "Hitungan: $saatIni / ${butir.target}",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (selesai) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                Button(
                                    onClick = {
                                        getar.performHapticFeedback(HapticFeedbackType.LongPress)
                                        hitunganDzikir[butir.id] = saatIni + 1
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (selesai) MaterialTheme.colorScheme.secondary
                                        else MaterialTheme.colorScheme.primary,
                                    ),
                                ) {
                                    if (selesai) {
                                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Text("  Selesai (+1)")
                                    } else {
                                        Icon(Icons.Rounded.TouchApp, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Text("  Hitung (+1)")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

