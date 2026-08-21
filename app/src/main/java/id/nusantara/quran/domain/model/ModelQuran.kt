package id.nusantara.quran.domain.model

/** Model domain surah yang tidak bergantung pada Room. */
data class ModelSurah(
    val nomor: Int,
    val namaArab: String,
    val namaLatin: String,
    val arti: String = "",
    val jumlahAyat: Int,
    val tipe: String = "",
)

/** Model domain ayat untuk lapisan tampilan. */
data class ModelAyat(
    val id: Int,
    val surah: Int,
    val nomor: Int,
    val arab: String,
    val terjemahan: String = "",
    val catatanKaki: String = "",
    val juz: Int = 1,
)

/** Model kota untuk jadwal sholat dan perhitungan arah kiblat. */
data class ModelKota(val id: String, val nama: String)

/** Satu kategori tematik beserta butir ayatnya. */
data class ModelKategoriTematik(
    val id: String,
    val judul: String,
    val deskripsi: String,
    val item: List<ModelButirTematik>,
)

/** Satu butir tematik berisi judul dan daftar rujukan ayat. */
data class ModelButirTematik(val judul: String, val rujukan: List<Pair<Int, Int>>)

/** Waktu lima sholat wajib dalam sehari. */
data class ModelWaktuSholat(
    val subuh: String = "-",
    val dzuhur: String = "-",
    val ashar: String = "-",
    val maghrib: String = "-",
    val isya: String = "-",
) {
    /** Daftar berurutan agar mudah diiterasi tampilan dan penjadwal. */
    fun sebagaiDaftar(): List<Pair<String, String>> =
        listOf("Subuh" to subuh, "Dzuhur" to dzuhur, "Ashar" to ashar, "Maghrib" to maghrib, "Isya" to isya)
}
