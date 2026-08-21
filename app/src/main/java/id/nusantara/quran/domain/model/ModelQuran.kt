package id.nusantara.quran.domain.model

/** Model domain surah yang tidak bergantung pada Room. */
data class ModelSurah(val nomor: Int, val namaArab: String, val namaLatin: String, val jumlahAyat: Int)

/** Model domain ayat untuk lapisan tampilan. */
data class ModelAyat(val id: Int, val surah: Int, val nomor: Int, val arab: String, val terjemahan: String = "")
