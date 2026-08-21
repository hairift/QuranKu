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
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import id.nusantara.quran.inti.util.KompasKiblat
import kotlin.math.roundToInt

/** Layar jadwal azan dan arah kiblat yang bekerja tanpa izin lokasi. */
@Composable
fun TampilanNusantara(modifier: Modifier = Modifier, onKembali: () -> Unit = {}) {
    val konteks = LocalContext.current
    var arahPonsel by remember { mutableFloatStateOf(0f) }
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
            }
        }
        Text("Jadwal azan hari ini · Jakarta", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 22.dp, bottom = 8.dp))
        LazyColumn { items(listOf("Subuh" to "04:42", "Dzuhur" to "12:02", "Ashar" to "15:23", "Maghrib" to "17:56", "Isya" to "19:08")) { (nama, waktu) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(nama); Text(waktu, color = MaterialTheme.colorScheme.primary) }
        } }
    }
}

private fun namaArah(derajat: Double): String = listOf("Utara", "Timur Laut", "Timur", "Tenggara", "Selatan", "Barat Daya", "Barat", "Barat Laut")[(derajat / 45.0).roundToInt() % 8]
