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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import id.nusantara.quran.R
import id.nusantara.quran.inti.ui.tema.EmasNusantara
import id.nusantara.quran.inti.ui.tema.EmasTerang
import id.nusantara.quran.inti.ui.tema.HijauZamrud
import id.nusantara.quran.inti.ui.tema.HijauZamrudTua
import id.nusantara.quran.inti.util.KompasKiblat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Ruang Nusantara: Kompas Kiblat berdesain piringan aset mata angin Nusantara
 * dengan penanda Ka'bah presisi serta jadwal sholat 518 kota/kabupaten Indonesia.
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

    // Animasi pegas agar piringan dan jarum kompas bergerak halus.
    val arahPonsel by animateFloatAsState(
        targetValue = arahMentah,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 110f),
        label = "jarumKompas",
    )

    val arahKiblat = KompasKiblat.arah(koordinat.first, koordinat.second)
    val jarakKm = KompasKiblat.jarakKilometer(koordinat.first, koordinat.second)
    val sebutanKiblat = KompasKiblat.namaMataAngin(arahKiblat.toFloat())
    val sebutanHadap = KompasKiblat.namaMataAngin(arahPonsel)
    val formatAngka = remember { NumberFormat.getNumberInstance(Locale("id", "ID")) }

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
        Row(
            Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onKembali) {
                Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Kembali")
            }
            Column(Modifier.weight(1f)) {
                Text("Sholat & Kiblat", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "${kotaTerpilih.second} · Kompas Kiblat & Jadwal Harian",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        // Banner informasi Ka'bah & koordinat kiblat dengan aset foto Ka'bah luring.
        Spacer(Modifier.height(12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(HijauZamrudTua, HijauZamrud),
                        ),
                    ),
            ) {
                Image(
                    painter = painterResource(R.drawable.foto_kaabah_banner),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.22f,
                    modifier = Modifier.matchParentSize(),
                )
                Row(
                    Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_kaabah),
                        contentDescription = "Ikon Ka'bah",
                        modifier = Modifier.size(54.dp),
                    )
                    Column(Modifier.weight(1f).padding(start = 14.dp)) {
                        Text(
                            "BAITULLAH · MAKKAH AL-MUKARRAMAH",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmasTerang,
                        )
                        Text(
                            "Sudut Kiblat ${arahKiblat.roundToInt()}° ($sebutanKiblat)",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                        Text(
                            "Jarak garis lurus sekitar ${formatAngka.format(jarakKm)} km dari lokasi Anda",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.88f),
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }

        // Kartu utama Kompas Kiblat Profesional.
        Spacer(Modifier.height(14.dp))
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(
                Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Baris metrik sudut hadap ponsel vs sudut kiblat
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Column(
                            Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                "ARAH HADAP",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                            Text(
                                "${arahPonsel.roundToInt()}° $sebutanHadap",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                    ) {
                        Column(
                            Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                "ARAH KIBLAT",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                            Text(
                                "${arahKiblat.roundToInt()}° $sebutanKiblat",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))
                val selisih = selisihSudut(arahPonsel, arahKiblat.toFloat())
                val tepatKiblat = selisih < 6f

                KompasVisual(
                    arahPonsel = arahPonsel,
                    arahKiblat = arahKiblat.toFloat(),
                    tepatKiblat = tepatKiblat,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                Spacer(Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (tepatKiblat) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        1.dp,
                        if (tepatKiblat) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            if (tepatKiblat) Icons.Rounded.CheckCircle else Icons.Rounded.Explore,
                            contentDescription = null,
                            tint = if (tepatKiblat) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (tepatKiblat) "Perangkat sudah menghadap tepat ke arah Kiblat"
                            else "Sejajarkan ikon Ka'bah dan jarum emas ke penanda atas",
                            style = MaterialTheme.typography.labelLarge,
                            color = if (tepatKiblat) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
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
                    },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Icon(
                        Icons.Rounded.MyLocation,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp),
                    )
                    Text(
                        "  Kalibrasi Titik GPS Perangkat (${"%.2f".format(koordinat.first)}, ${"%.2f".format(koordinat.second)})",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }

        // Kartu jadwal sholat harian.
        Spacer(Modifier.height(16.dp))
        Card(
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Column(Modifier.weight(1f).padding(start = 12.dp)) {
                        Text("Jadwal Sholat Harian", style = MaterialTheme.typography.titleMedium)
                        Text(
                            kotaTerpilih.second,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    OutlinedButton(
                        onClick = { tampilPilihKota = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    ) {
                        Icon(Icons.Rounded.Place, contentDescription = null, modifier = Modifier.size(16.dp))
                        Text("  Ganti Kota", style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(Modifier.height(14.dp))
                val daftarLengkap = jadwal.sebagaiDaftarLengkap()
                daftarLengkap.forEachIndexed { indeks, (nama, jam) ->
                    val wajib = nama !in listOf("Imsak", "Terbit")
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = if (wajib) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(8.dp),
                            ) {}
                            Spacer(Modifier.width(12.dp))
                            Text(
                                nama,
                                style = if (wajib) MaterialTheme.typography.titleSmall
                                else MaterialTheme.typography.bodyMedium,
                                color = if (wajib) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (wajib) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Text(
                                jam,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (wajib) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            )
                        }
                    }
                    if (indeks < daftarLengkap.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
                    }
                }

                Spacer(Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Rounded.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(22.dp),
                        )
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text("Pengingat Azan Otomatis", style = MaterialTheme.typography.titleSmall)
                            Text(
                                "Bunyikan notifikasi azan setiap masuk waktu sholat wajib",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Switch(
                            checked = pengingatAzan,
                            onCheckedChange = { model.alihkanPengingatAzan(it) },
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (tampilPilihKota) {
        DialogPilihKota(model = model, onTutup = { tampilPilihKota = false })
    }
}

/**
 * Visual kompas berlapis profesional:
 * - Piringan aset vektor `ic_kompas_piringan` (bintang 8 mata angin & Rub el Hizb)
 * - Skala derajat presisi dan label 8 penjuru mata angin (U, TL, T, TG, S, BD, B, BL)
 * - Jarum tombak emas `ic_kompas_jarum_kiblat` dan lencana `ic_kaabah` di sudut kiblat
 * - Penanda segitiga atas statis sebagai titik bidik kiblat.
 */
@Composable
private fun KompasVisual(
    arahPonsel: Float,
    arahKiblat: Float,
    tepatKiblat: Boolean,
    modifier: Modifier = Modifier,
) {
    val warnaUtama = MaterialTheme.colorScheme.primary
    val warnaGaris = MaterialTheme.colorScheme.outline
    val warnaTinta = MaterialTheme.colorScheme.onSurface
    val warnaRedup = MaterialTheme.colorScheme.onSurfaceVariant
    val density = LocalDensity.current.density

    Box(modifier.size(288.dp), contentAlignment = Alignment.Center) {
        // 1. Piringan vektor mata angin Nusantara yang berputar mengikuti sensor
        Image(
            painter = painterResource(R.drawable.ic_kompas_piringan),
            contentDescription = "Piringan mata angin kompas",
            modifier = Modifier
                .size(252.dp)
                .rotate(-arahPonsel),
        )

        // 2. Canvas untuk cincin luar, skala derajat 5°, dan indikator bidik atas
        Canvas(Modifier.fillMaxSize()) {
            val pusat = Offset(size.width / 2f, size.height / 2f)
            val radiusLuar = size.minDimension / 2f - 10f

            // Cincin pendar zamrud saat tepat menghadap kiblat
            if (tepatKiblat) {
                drawCircle(
                    color = warnaUtama.copy(alpha = 0.18f),
                    radius = radiusLuar + 6f,
                    center = pusat,
                )
            }
            drawCircle(
                color = if (tepatKiblat) warnaUtama else EmasNusantara.copy(alpha = 0.7f),
                radius = radiusLuar,
                center = pusat,
                style = Stroke(width = if (tepatKiblat) 5f else 3f),
            )

            // Garis skala derajat berputar mengikuti piringan
            rotate(-arahPonsel, pusat) {
                for (derajat in 0 until 360 step 5) {
                    val sudutRad = Math.toRadians((derajat - 90).toDouble())
                    val utama = derajat % 90 == 0
                    val sedang = derajat % 30 == 0
                    val panjang = when {
                        utama -> 18f
                        sedang -> 13f
                        else -> 7f
                    }
                    val awal = Offset(
                        pusat.x + (radiusLuar - 18f - panjang) * cos(sudutRad).toFloat(),
                        pusat.y + (radiusLuar - 18f - panjang) * sin(sudutRad).toFloat(),
                    )
                    val akhir = Offset(
                        pusat.x + (radiusLuar - 18f) * cos(sudutRad).toFloat(),
                        pusat.y + (radiusLuar - 18f) * sin(sudutRad).toFloat(),
                    )
                    drawLine(
                        color = when {
                            derajat == 0 -> EmasNusantara
                            utama -> warnaUtama
                            sedang -> warnaTinta.copy(alpha = 0.65f)
                            else -> warnaGaris
                        },
                        start = awal,
                        end = akhir,
                        strokeWidth = when {
                            utama -> 4.5f
                            sedang -> 2.8f
                            else -> 1.4f
                        },
                    )
                }
            }

            // Penanda segitiga statis di puncak (12 o'clock) sebagai acuan hadap perangkat
            val jalurSegitiga = Path().apply {
                moveTo(pusat.x, 2f)
                lineTo(pusat.x - 14f, 20f)
                lineTo(pusat.x + 14f, 20f)
                close()
            }
            drawPath(
                path = jalurSegitiga,
                color = if (tepatKiblat) warnaUtama else EmasNusantara,
            )
        }

        // 3. Jarum emas kiblat (aset vektor) yang mengarah tepat ke sudut kiblat
        Image(
            painter = painterResource(R.drawable.ic_kompas_jarum_kiblat),
            contentDescription = "Jarum penunjuk kiblat",
            modifier = Modifier
                .size(228.dp)
                .rotate(arahKiblat - arahPonsel),
        )

        // 4. Label 8 penjuru mata angin Indonesia yang ikut berputar di sekeliling piringan
        val delapanPenjuru = listOf(
            "U" to 0f,
            "TL" to 45f,
            "T" to 90f,
            "TG" to 135f,
            "S" to 180f,
            "BD" to 225f,
            "B" to 270f,
            "BL" to 315f,
        )
        delapanPenjuru.forEach { (kode, sudutDasar) ->
            val sudut = Math.toRadians((sudutDasar - arahPonsel - 90f).toDouble())
            val utama = kode.length == 1
            val jarakPiksel = (if (utama) 96.dp else 94.dp).value * density
            Text(
                text = kode,
                color = when {
                    kode == "U" -> EmasNusantara
                    utama -> warnaTinta
                    else -> warnaRedup
                },
                style = if (utama) {
                    MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                } else {
                    MaterialTheme.typography.labelSmall
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset {
                        IntOffset(
                            (cos(sudut) * jarakPiksel).toInt(),
                            (sin(sudut) * jarakPiksel).toInt(),
                        )
                    },
            )
        }

        // 5. Lencana ikon Ka'bah pada tepi luar piringan di sudut kiblat
        val sudutKiblatRad = Math.toRadians((arahKiblat - arahPonsel - 90f).toDouble())
        val jarakIkonKaabah = 118.dp.value * density
        Image(
            painter = painterResource(R.drawable.ic_kaabah),
            contentDescription = "Arah Ka'bah",
            modifier = Modifier
                .size(34.dp)
                .align(Alignment.Center)
                .offset {
                    IntOffset(
                        (cos(sudutKiblatRad) * jarakIkonKaabah).toInt(),
                        (sin(sudutKiblatRad) * jarakIkonKaabah).toInt(),
                    )
                },
        )
    }
}

/** Selisih terkecil antara dua sudut kompas dalam derajat. */
private fun selisihSudut(a: Float, b: Float): Float {
    val selisih = ((a - b + 540f) % 360f) - 180f
    return abs(selisih)
}

/** Dialog pencarian dan pemilihan 518 kota/kabupaten Indonesia. */
@Composable
private fun DialogPilihKota(model: ModelTampilanNusantara, onTutup: () -> Unit) {
    var kata by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onTutup,
        title = { Text("Pilih Kota atau Kabupaten") },
        text = {
            Column {
                OutlinedTextField(
                    value = kata,
                    onValueChange = { kata = it },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                    placeholder = { Text("Ketik nama kota…") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(),
                )
                LazyColumn(Modifier.height(320.dp).padding(top = 8.dp)) {
                    items(model.cariKota(kata).take(60), key = { it.id }) { kota ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    model.pilihKota(kota)
                                    onTutup()
                                }
                                .padding(vertical = 12.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                kota.nama,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 10.dp),
                            )
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onTutup) { Text("Tutup") } },
    )
}
