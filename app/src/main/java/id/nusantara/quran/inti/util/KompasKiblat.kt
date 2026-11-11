package id.nusantara.quran.inti.util

import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Kalkulasi arah kiblat, jarak permukaan bumi menuju Ka'bah di Makkah,
 * serta penamaan arah mata angin dalam bahasa Indonesia.
 */
object KompasKiblat {

    private const val LATITUD_KAABAH = 21.4225
    private const val LONGITUD_KAABAH = 39.8262
    private const val RADIUS_BUMI_KM = 6371.0

    /** Menghitung sudut arah kiblat (0-360 derajat dari Utara sejati). */
    fun arah(latitud: Double, longitud: Double): Double {
        val kaabahLat = Math.toRadians(LATITUD_KAABAH)
        val selisihBujur = Math.toRadians(LONGITUD_KAABAH - longitud)
        val latRad = Math.toRadians(latitud)
        return (
            Math.toDegrees(
                atan2(
                    sin(selisihBujur),
                    cos(latRad) * tan(kaabahLat) - sin(latRad) * cos(selisihBujur),
                ),
            ) + 360
            ) % 360
    }

    /** Menghitung jarak garis lingkar besar (Haversine) ke Ka'bah dalam kilometer. */
    fun jarakKilometer(latitud: Double, longitud: Double): Int {
        val dLat = Math.toRadians(LATITUD_KAABAH - latitud)
        val dLon = Math.toRadians(LONGITUD_KAABAH - longitud)
        val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(latitud)) * cos(Math.toRadians(LATITUD_KAABAH)) * sin(dLon / 2).pow(2)
        val c = 2 * asin(sqrt(a.coerceIn(0.0, 1.0)))
        return (RADIUS_BUMI_KM * c).roundToInt()
    }

    /** Mengubah derajat kompas menjadi sebutan delapan arah mata angin Indonesia. */
    fun namaMataAngin(derajat: Float): String {
        val normal = ((derajat % 360f) + 360f) % 360f
        return when {
            normal < 22.5f || normal >= 337.5f -> "Utara"
            normal < 67.5f -> "Timur Laut"
            normal < 112.5f -> "Timur"
            normal < 157.5f -> "Tenggara"
            normal < 202.5f -> "Selatan"
            normal < 247.5f -> "Barat Daya"
            normal < 292.5f -> "Barat"
            else -> "Barat Laut"
        }
    }

    private fun tan(nilai: Double) = sin(nilai) / cos(nilai)
}
