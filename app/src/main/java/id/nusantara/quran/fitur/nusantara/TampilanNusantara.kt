package id.nusantara.quran.fitur.nusantara

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import id.nusantara.quran.inti.util.KompasKiblat
import id.nusantara.quran.data.remote.api.ApiJadwalSholat
import id.nusantara.quran.data.remote.model.WaktuSholat
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

/** Layar jadwal azan dan arah kiblat yang bekerja tanpa izin lokasi. */
@Composable
fun TampilanNusantara(modifier: Modifier = Modifier, onKembali: () -> Unit = {}) {
    val konteks = LocalContext.current
    var arahPonsel by remember { mutableFloatStateOf(0f) }
    var waktu by remember { mutableStateOf(WaktuSholat()) }
    val sensorManager = remember { konteks.getSystemService(SensorManager::class.java) }
    DisposableEffect(sensorManager) {
        val sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val pendengar = object : SensorEventListener {
            override fun onSensorChanged(peristiwa: SensorEvent) {
                val matriks = FloatArray(9)
                val orientasi = FloatArray(3)
                SensorManager.getRotationMatrixFromVector(matriks, peristiwa.values)
                SensorManager.getOrientation(matriks, orientasi)
                arahPonsel = ((Math.toDegrees(orientasi[0].toDouble()) + 360) % 360).toFloat()
            }
            override fun onAccuracyChanged(sensor: Sensor?, akurasi: Int) = Unit
        }
        sensor?.let { sensorManager.registerListener(pendengar, it, SensorManager.SENSOR_DELAY_UI) }
        onDispose { sensorManager?.unregisterListener(pendengar) }
    }
    val arahKiblat = KompasKiblat.arah(-6.2, 106.8)
    LaunchedEffect(Unit) {
        waktu = withContext(Dispatchers.IO) {
            runCatching {
                val api = Retrofit.Builder().baseUrl("https://api.myquran.com/").addConverterFactory(MoshiConverterFactory.create()).build().create(ApiJadwalSholat::class.java)
                api.jadwal("1301", SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())).data?.jadwal ?: WaktuSholat()
            }.getOrDefault(WaktuSholat("04:42", "12:02", "15:23", "17:56", "19:08"))
        }
    }
    Column(modifier.fillMaxSize().padding(22.dp)) {
        IconButton(onClick = onKembali) { Icon(Icons.Rounded.ArrowBack, "Kembali") }
        Text("Ruang Nusantara", style = MaterialTheme.typography.headlineMedium)
        Card(Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Column(Modifier.padding(18.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Icon(Icons.Rounded.Explore, "Kiblat")
                    Text("Kompas Kiblat", style = MaterialTheme.typography.titleMedium)
                }
                Text("Arah kiblat Jakarta: ${arahKiblat.roundToInt()}° ${namaArah(arahKiblat)}", modifier = Modifier.padding(top = 12.dp))
                Text("Arah ponsel: ${arahPonsel.roundToInt()}° ${namaArah(arahPonsel.toDouble())}", modifier = Modifier.padding(top = 6.dp))
                Text("Putar perangkat sampai penunjuk mendekati arah kiblat.", modifier = Modifier.padding(top = 8.dp))
                KompasVisual(arahPonsel, arahKiblat)
            }
        }
        Text("Jadwal azan hari ini · Jakarta", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 22.dp, bottom = 8.dp))
        LazyColumn { items(listOf("Subuh" to waktu.subuh, "Dzuhur" to waktu.dzuhur, "Ashar" to waktu.ashar, "Maghrib" to waktu.maghrib, "Isya" to waktu.isya)) { (nama, jam) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(nama); Text(jam, color = MaterialTheme.colorScheme.primary) }
        } }
    }
}

@Composable
private fun KompasVisual(arahPonsel: Float, arahKiblat: Double) {
    val warna = MaterialTheme.colorScheme.primary
    Canvas(Modifier.fillMaxWidth().height(240.dp).padding(top = 18.dp).background(MaterialTheme.colorScheme.surfaceVariant)) {
        val pusat = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * .34f
        drawCircle(warna.copy(alpha = .12f), radius = radius * 1.18f, center = pusat)
        drawCircle(warna, radius = radius, center = pusat, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f))
        for (indeks in 0 until 36) {
            val sudut = Math.toRadians((indeks * 10 - 90).toDouble())
            val panjang = if (indeks % 9 == 0) 18f else 9f
            val awal = Offset(pusat.x + (radius - panjang) * kotlin.math.cos(sudut).toFloat(), pusat.y + (radius - panjang) * kotlin.math.sin(sudut).toFloat())
            val akhir = Offset(pusat.x + radius * kotlin.math.cos(sudut).toFloat(), pusat.y + radius * kotlin.math.sin(sudut).toFloat())
            drawLine(warna, awal, akhir, strokeWidth = if (indeks % 9 == 0) 4f else 2f)
        }
        val rotasi = arahKiblat.toFloat() - arahPonsel
        rotate(rotasi, pusat) {
            drawLine(Color(0xFFC8A951), Offset(pusat.x, pusat.y + radius * .55f), Offset(pusat.x, pusat.y - radius * .78f), strokeWidth = 10f)
            drawCircle(Color(0xFFC8A951), radius = 13f, center = pusat)
        }
    }
    Row(Modifier.fillMaxWidth().padding(top = 6.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        Text("U", color = warna); Text("T", color = warna); Text("S", color = warna); Text("B", color = warna)
    }
}

private fun namaArah(derajat: Double): String = listOf("Utara", "Timur Laut", "Timur", "Tenggara", "Selatan", "Barat Daya", "Barat", "Barat Laut")[(derajat / 45.0).roundToInt() % 8]
