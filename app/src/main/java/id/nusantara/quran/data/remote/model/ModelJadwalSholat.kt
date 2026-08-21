package id.nusantara.quran.data.remote.model

import com.squareup.moshi.Json

/** Bentuk respons jadwal sholat dari layanan MyQuran. */
data class ResponsJadwalSholat(val data: DataJadwalSholat?)
data class DataJadwalSholat(val jadwal: WaktuSholat?)
data class WaktuSholat(
    @Json(name = "subuh") val subuh: String = "-",
    @Json(name = "dzuhur") val dzuhur: String = "-",
    @Json(name = "ashar") val ashar: String = "-",
    @Json(name = "maghrib") val maghrib: String = "-",
    @Json(name = "isya") val isya: String = "-"
)
