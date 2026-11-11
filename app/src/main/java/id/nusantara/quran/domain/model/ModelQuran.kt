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

/** Satu butir Quran & Sains: fenomena ilmiah beserta rujukan ayatnya. */
data class ModelButirSains(
    val judul: String,
    val ringkasan: String,
    val rujukan: List<Pair<Int, Int>>,
)

/** Satu topik pada penjelajah topik: judul dan daftar rujukan ayat. */
data class ModelTopik(val judul: String, val rujukan: List<Pair<Int, Int>>)

/** Kelompok topik, baik kategori umum maupun berdasarkan suasana hati. */
data class ModelKategoriTopik(
    val id: String,
    val judul: String,
    val deskripsi: String,
    val topik: List<ModelTopik>,
)

/** Bacaan unggulan: surah atau ayat tertentu yang dianjurkan pada waktu tertentu. */
data class ModelBacaanUnggulan(
    val id: String,
    val judul: String,
    val deskripsi: String,
    val rekomendasi: String,
    val surah: Int,
    val ayat: Int = 0,
)

/** Satu nama indah Allah dalam 99 Asmaul Husna beserta arti dan hikmahnya. */
data class ModelAsmaulHusna(
    val nomor: Int,
    val arab: String,
    val latin: String,
    val arti: String,
    val keterangan: String,
)

/** Satu bacaan dzikir harian (pagi, petang, atau setelah sholat) beserta target hitungannya. */
data class ModelDzikir(
    val id: String,
    val kategori: String,
    val judul: String,
    val arab: String,
    val latin: String,
    val arti: String,
    val target: Int,
    val keutamaan: String,
)

/** Waktu lima sholat wajib beserta waktu imsak dan terbit dalam sehari. */
data class ModelWaktuSholat(
    val subuh: String = "-",
    val dzuhur: String = "-",
    val ashar: String = "-",
    val maghrib: String = "-",
    val isya: String = "-",
    val imsak: String = "04:25",
    val terbit: String = "05:49",
) {
    /** Daftar lima waktu sholat wajib berurutan untuk tampilan dan penjadwal azan. */
    fun sebagaiDaftar(): List<Pair<String, String>> =
        listOf("Subuh" to subuh, "Dzuhur" to dzuhur, "Ashar" to ashar, "Maghrib" to maghrib, "Isya" to isya)

    /** Daftar lengkap termasuk Imsak dan Terbit untuk tabel jadwal harian. */
    fun sebagaiDaftarLengkap(): List<Pair<String, String>> =
        listOf(
            "Imsak" to imsak,
            "Subuh" to subuh,
            "Terbit" to terbit,
            "Dzuhur" to dzuhur,
            "Ashar" to ashar,
            "Maghrib" to maghrib,
            "Isya" to isya,
        )
}
