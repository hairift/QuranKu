package id.nusantara.quran.data.lokal.entitas

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Entitas surah yang disimpan di basis data lokal. */
@Entity(tableName = "surah")
data class EntitasSurah(@PrimaryKey val nomor: Int, val namaArab: String, val namaLatin: String, val jumlahAyat: Int)

/** Entitas ayat dengan metadata mushaf. */
@Entity(tableName = "ayat")
data class EntitasAyat(@PrimaryKey val idAyat: Int, val nomorSurah: Int, val nomorAyat: Int, val teksArab: String, val nomorJuz: Int, val nomorHalaman: Int)

/** Terjemahan ayat dari sumber yang dipilih pengguna. */
@Entity(tableName = "terjemahan")
data class EntitasTerjemahan(@PrimaryKey val id: String, val idAyat: Int, val bahasa: String, val teks: String, val catatanKaki: String? = null)

/** Tafsir ringkas yang dapat dipasang secara offline. */
@Entity(tableName = "tafsir")
data class EntitasTafsir(@PrimaryKey val id: String, val idAyat: Int, val namaSumber: String, val teks: String)

/** Penanda baca ayat milik pengguna. */
@Entity(tableName = "bookmark")
data class EntitasBookmark(@PrimaryKey(autoGenerate = true) val id: Long = 0, val idAyatAwal: Int, val idAyatAkhir: Int? = null, val judulCatatan: String = "", val isiCatatan: String = "", val warnaTag: String = "#C8A951", val tanggalDibuat: Long = System.currentTimeMillis())

/** Metadata berkas audio yang telah dicache. */
@Entity(tableName = "audio_cache")
data class EntitasAudioCache(@PrimaryKey val id: String, val qari: String, val lokasiBerkas: String, val ukuranBerkas: Long)
