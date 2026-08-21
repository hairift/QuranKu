package id.nusantara.quran.fitur.nusantara

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.inti.util.KompasKiblat
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Ruang Nusantara: kompas kiblat dengan sensor rotasi asli dan
 * jadwal sholat lima waktu untuk 518 kota/kabupaten Indonesia.
 */
@Composable
fun TampilanNusantara(
    modifier: Modifier = Modifier,
    onKembali: () -> Unit = {},
    model: ModelTampilanNusantara = hiltViewModel(),
) {
    val konteks = LocalContext.current
    val jadwal by model.jadwal.collectAsState()
    val kotaTerpilih by model.kotaTerpilih.collectAsState()
    val koordinat by model.koordinat.collectAsState()
    val pengingatAzan by model.pengingatAzan.collectAsState()
    var tampilPilihKota by remember { mutableStateOf(false) }

    // Dengarkan sensor vektor rotasi untuk arah hadap perangkat.
    var arahMentah by remember { mutableFloatStateOf(0f) }
    val sensorManager = remember { konteks.getSystemService(SensorManager::class.java) }
    DisposableEffect(sensorManager) {
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            ?: sensorManager?.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)
        val pendengar = object : SensorEventListener {
            override fun onSensorChanged(peristiwa: SensorEvent) {
                val matriks = FloatArray(9)
                val orientasi = FloatArray(3)
                SensorManager.getRotationMatrixFromVector(matriks, peristiwa.values)
                SensorManager.getOrientation(matriks, orientasi)
                arahMentah = ((Math.toDegrees(orientasi[0].toDouble()) + 360) % 360).toFloat()
            }

            override fun onAccuracyChanged(sensor: Sensor?, akurasi: Int) = Unit
        }
        sensor?.let { sensorManager.registerListener(pendengar, it, SensorManager.SENSOR_DELAY_GAME) }
        onDispose { sensorManager?.unregisterListener(pendengar) }
    }

    // Animasi pegas agar jarum kompas bergerak mulus, tidak kaku.
    val arahPonsel by animateFloatAsState(
        targetValue = arahMentah,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 120f),
        label = "jarum",
    )

    val arahKiblat = KompasKiblat.arah(koordinat.first, koordinat.second)
    val peluncurLokasi = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { hasil ->
        if (hasil.values.any { it }) model.pakaiLokasiPerangkat()
    }

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
            Text("Sholat & Kiblat", style = MaterialTheme.typography.headlineSmall)
        }

        // Kartu kompas kiblat.
        Card(
            Modifier.fillMaxWidth().padding(top = 12.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Arah Kiblat", style = MaterialTheme.typography.titleMedium)
                Text(
                    "${arahKiblat.roundToInt()}° dari Utara",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
                KompasVisual(
                    arahPonsel = arahPonsel,
                    arahKiblat = arahKiblat.toFloat(),
                    modifier = Modifier.padding(vertical = 16.dp),
                )
                val selisih = selisihSudut(arahPonsel, arahKiblat.toFloat())
                Text(
                    if (selisih < 6f) "Perangkat sudah menghadap kiblat"
                    else "Putar perangkat hingga penanda emas berada tepat di atas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selisih < 6f) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
                TextButton(onClick = {
                    val izinAda = ContextCompat.checkSelfPermission(
                        konteks, Manifest.permission.ACCESS_FINE_LOCATION,
                    ) == PackageManager.PERMISSION_GRANTED
                    if (izinAda) {
                        model.pakaiLokasiPerangkat()
                    } else {
                        peluncurLokasi.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            ),
                        )
                    }
                }) {
                    Icon(Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("  Pakai lokasi perangkat", modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        // Kartu jadwal sholat.
        Card(
            Modifier.fillMaxWidth().padding(top = 14.dp),
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Jadwal Sholat", style = MaterialTheme.typography.titleMedium)
                        Text(
                            kotaTerpilih.second,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    TextButton(onClick = { tampilPilihKota = true }) {
                        Icon(Icons.Rounded.Place, contentDescription = null, modifier = Modifier.size(18.dp))
                        Text("  Ganti kota", modifier = Modifier.padding(start = 4.dp))
                    }
                }
                Spacer(Modifier.height(8.dp))
                jadwal.sebagaiDaftar().forEach { (nama, jam) ->
                    Row(
                        Modifier.fillMaxWidth().padding(vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(nama, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            jam,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Row(
                    Modifier.fillMaxWidth().padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Rounded.NotificationsActive,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        "Pengingat azan",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 10.dp).weight(1f),
                    )
                    Switch(
                        checked = pengingatAzan,
                        onCheckedChange = { model.alihkanPengingatAzan(it) },
                    )
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (tampilPilihKota) {
        DialogPilihKota(model = model, onTutup = { tampilPilihKota = false })
    }
}

/** Visual kompas: piringan mata angin berputar, penanda kiblat emas tetap di atas. */
@Composable
private fun KompasVisual(arahPonsel: Float, arahKiblat: Float, modifier: Modifier = Modifier) {
    val warnaUtama = MaterialTheme.colorScheme.primary
    val warnaGaris = MaterialTheme.colorScheme.outline
    val warnaEmas = Color(0xFFC8A951)
    val warnaTinta = MaterialTheme.colorScheme.onSurface
    val density = androidx.compose.ui.platform.LocalDensity.current.density

    Box(modifier.size(260.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val pusat = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f - 8f

            // Piringan luar dan cincin penunjuk.
            drawCircle(warnaUtama.copy(alpha = 0.08f), radius = radius, center = pusat)
            drawCircle(warnaGaris, radius = radius, center = pusat, style = Stroke(width = 3f))

            // Seluruh elemen arah berputar berlawanan arah hadap ponsel.
            rotate(-arahPonsel, pusat) {
                // Garis derajat setiap 10°, lebih panjang tiap 30°.
                for (indeks in 0 until 36) {
                    val sudut = Math.toRadians((indeks * 10 - 90).toDouble())
                    val panjang = if (indeks % 3 == 0) 20f else 10f
                    val awal = Offset(
                        pusat.x + (radius - panjang) * cos(sudut).toFloat(),
                        pusat.y + (radius - panjang) * sin(sudut).toFloat(),
                    )
                    val akhir = Offset(
                        pusat.x + radius * cos(sudut).toFloat(),
                        pusat.y + radius * sin(sudut).toFloat(),
                    )
                    drawLine(
                        warnaGaris, awal, akhir,
                        strokeWidth = if (indeks % 3 == 0) 4f else 2f,
                    )
                }

                // Jarum penunjuk kiblat berwarna emas mengarah ke sudut kiblat.
                rotate(arahKiblat, pusat) {
                    val ujung = Offset(pusat.x, pusat.y - radius * 0.72f)
                    drawLine(warnaEmas, pusat, ujung, strokeWidth = 12f)
                    drawCircle(warnaEmas, radius = 16f, center = ujung)
                    drawCircle(warnaEmas, radius = 12f, center = pusat)
                }
            }
        }
        // Label mata angin yang ikut berputar mengikuti piringan.
        val daftarArah = listOf("U" to 0f, "T" to 90f, "S" to 180f, "B" to 270f)
        daftarArah.forEach { (huruf, sudutDasar) ->
            val sudut = Math.toRadians((sudutDasar - arahPonsel - 90f).toDouble())
            val jarakPiksel = 92.dp.value * density
            Text(
                huruf,
                color = warnaTinta,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Center).offset {
                    androidx.compose.ui.unit.IntOffset(
                        (cos(sudut) * jarakPiksel).toInt(),
                        (sin(sudut) * jarakPiksel).toInt(),
                    )
                },
            )
        }
    }
}

/** Selisih terkecil antara dua sudut kompas dalam derajat. */
private fun selisihSudut(a: Float, b: Float): Float {
    val selisih = ((a - b + 540f) % 360f) - 180f
    return kotlin.math.abs(selisih)
}

/** Dialog pencarian dan pemilihan kota/kabupaten. */
@Composable
private fun DialogPilihKota(model: ModelTampilanNusantara, onTutup: () -> Unit) {
    var kata by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onTutup,
        title = { Text("Pilih kota") },
        text = {
            Column {
                OutlinedTextField(
                    value = kata,
                    onValueChange = { kata = it },
                    placeholder = { Text("Ketik nama kota…") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                LazyColumn(Modifier.height(320.dp).padding(top = 8.dp)) {
                    items(model.cariKota(kata).take(60), key = { it.id }) { kota ->
                        Text(
                            kota.nama,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    model.pilihKota(kota)
                                    onTutup()
                                }
                                .padding(vertical = 12.dp, horizontal = 4.dp),
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onTutup) { Text("Tutup") } },
    )
}
