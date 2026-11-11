package id.nusantara.quran.fitur.pencarian

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Pencarian ayat berdasarkan teks Arab atau terjemahan Indonesia,
 * dilengkapi pencarian suara berbahasa Indonesia dan saran kata kunci cepat.
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

    val konteks = LocalContext.current
    var mendengarkan by remember { mutableStateOf(false) }
    var pesanSuara by remember { mutableStateOf<String?>(null) }

    val pengenal = remember {
        if (SpeechRecognizer.isRecognitionAvailable(konteks)) {
            SpeechRecognizer.createSpeechRecognizer(konteks)
        } else null
    }

    fun mulaiMendengarkan() {
        if (pengenal == null) {
            pesanSuara = "Pengenalan suara tidak tersedia di perangkat ini."
            return
        }
        val niat = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        pengenal.startListening(niat)
        mendengarkan = true
        pesanSuara = "Mendengarkan… ucapkan kata kunci ayat."
    }

    DisposableEffect(pengenal) {
        val pendengar = object : RecognitionListener {
            override fun onResults(hasilSuara: Bundle) {
                mendengarkan = false
                val teks = hasilSuara
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                if (!teks.isNullOrBlank()) {
                    pesanSuara = null
                    model.ketik(teks)
                } else {
                    pesanSuara = "Suara tidak terdengar jelas. Coba lagi."
                }
            }

            override fun onError(kode: Int) {
                mendengarkan = false
                pesanSuara = when (kode) {
                    SpeechRecognizer.ERROR_NO_MATCH -> "Tidak ada ucapan yang cocok. Coba lagi."
                    SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Izin mikrofon belum diberikan."
                    SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                        "Pengenalan suara butuh koneksi jaringan."
                    else -> "Pengenalan suara gagal. Coba lagi."
                }
            }

            override fun onReadyForSpeech(params: Bundle?) = Unit
            override fun onBeginningOfSpeech() = Unit
            override fun onRmsChanged(rmsdB: Float) = Unit
            override fun onBufferReceived(buffer: ByteArray?) = Unit
            override fun onEndOfSpeech() = Unit
            override fun onPartialResults(partialResults: Bundle?) = Unit
            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        }
        pengenal?.setRecognitionListener(pendengar)
        onDispose { pengenal?.destroy() }
    }

    val peluncurIzinMikrofon = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { diberikan ->
        if (diberikan) {
            mulaiMendengarkan()
        } else {
            pesanSuara = "Izin mikrofon ditolak. Aktifkan di pengaturan untuk memakai pencarian suara."
        }
    }

    Column(modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column {
                Text("Cari Ayat Al-Quran", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "Telusuri 6.236 ayat dengan kata kunci atau suara",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        OutlinedTextField(
            value = kata,
            onValueChange = { model.ketik(it) },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 8.dp),
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (sedangMencari) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    }
                    if (kata.isNotBlank()) {
                        IconButton(onClick = { model.ketik("") }) {
                            Icon(Icons.Rounded.Close, contentDescription = "Bersihkan teks")
                        }
                    }
                    IconButton(onClick = {
                        if (mendengarkan) {
                            pengenal?.stopListening()
                            mendengarkan = false
                            pesanSuara = null
                        } else {
                            val izinAda = ContextCompat.checkSelfPermission(
                                konteks, Manifest.permission.RECORD_AUDIO,
                            ) == PackageManager.PERMISSION_GRANTED
                            if (izinAda) mulaiMendengarkan()
                            else peluncurIzinMikrofon.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }) {
                        Icon(
                            if (mendengarkan) Icons.Rounded.MicOff else Icons.Rounded.Mic,
                            contentDescription = if (mendengarkan) "Berhenti mendengarkan" else "Cari dengan suara",
                            tint = if (mendengarkan) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            },
            placeholder = { Text("Ketik kata Arab atau terjemahan…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )

        // Saran kata kunci populer
        val saranPopuler = listOf("Sabar", "Syukur", "Rezeki", "Rahmat", "Taubat", "Keluarga", "Ilmu", "Surga")
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 10.dp),
        ) {
            items(saranPopuler) { saran ->
                FilterChip(
                    selected = kata.equals(saran, ignoreCase = true),
                    onClick = { model.ketik(saran) },
                    label = { Text(saran) },
                )
            }
        }

        pesanSuara?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodySmall,
                color = if (mendengarkan) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }
        if (kata.trim().length in 1..2) {
            Text(
                "Ketik minimal 3 huruf untuk menampilkan hasil pencarian.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else if (hasil.isNotEmpty()) {
            Text(
                "Ditemukan ${hasil.size} ayat yang relevan",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp),
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            items(hasil, key = { it.id }) { ayat ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { bukaAyat(ayat.surah, ayat.nomor) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(34.dp), contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(R.drawable.ic_bingkai_ayat),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                )
                                Text(
                                    "${ayat.nomor}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Text(
                                "Surah ${ayat.surah} : Ayat ${ayat.nomor}",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 10.dp).weight(1f),
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
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                            fontFamily = KeluargaHurufArab,
                            fontSize = 22.sp,
                            lineHeight = 38.sp,
                            textAlign = TextAlign.Right,
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 8.dp),
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
}
