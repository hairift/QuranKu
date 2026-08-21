package id.nusantara.quran.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import id.nusantara.quran.data.remote.model.ResponsJadwalSholat

/** Kontrak layanan jadwal sholat MyQuran dan data terjemahan QuranEnc. */
interface ApiJadwalSholat {
    @GET("v2/sholat/jadwal/{kota}/{tanggal}") suspend fun jadwal(@Path("kota") kota: String, @Path("tanggal") tanggal: String): ResponsJadwalSholat
}

interface ApiQuranEnc {
    @GET("api/translation/surah/{surah}") suspend fun terjemahan(@Path("surah") surah: Int): Map<String, Any>
}
