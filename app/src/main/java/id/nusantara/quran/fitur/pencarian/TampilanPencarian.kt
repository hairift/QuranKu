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
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.MicOff
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.inti.ui.tema.KeluargaHurufArab

/**
 * Pencarian ayat berdasarkan teks Arab atau terjemahan Indonesia,
 * dilengkapi pencarian suara (voice note) berbahasa Indonesia.
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

    val konteks = LocalContext.current
    var mendengarkan by remember { mutableStateOf(false) }
    var pesanSuara by remember { mutableStateOf<String?>(null) }

    // Pengenal suara perangkat; null bila layanan tidak tersedia.
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
            override fun onResults(hasil: Bundle) {
                mendengarkan = false
                val teks = hasil
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (sedangMencari) {
                        CircularProgressIndicator(modifier = Modifier.padding(12.dp), strokeWidth = 2.dp)
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
            placeholder = { Text("Teks Arab atau terjemahan…") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
        )
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
