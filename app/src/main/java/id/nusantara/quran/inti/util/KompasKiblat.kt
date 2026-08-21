package id.nusantara.quran.inti.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/** Kalkulasi arah kiblat dari koordinat pengguna tanpa meminta lokasi otomatis. */
object KompasKiblat {
    fun arah(latitud: Double, longitud: Double): Double {
        val kaabahLat = Math.toRadians(21.4225)
        val selisihBujur = Math.toRadians(39.8262 - longitud)
        return (Math.toDegrees(atan2(sin(selisihBujur), cos(Math.toRadians(latitud)) * tan(kaabahLat) - sin(Math.toRadians(latitud)) * cos(selisihBujur))) + 360) % 360
    }
    private fun tan(nilai: Double) = sin(nilai) / cos(nilai)
}
