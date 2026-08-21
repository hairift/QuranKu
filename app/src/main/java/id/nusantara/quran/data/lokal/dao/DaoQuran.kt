package id.nusantara.quran.data.lokal.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import id.nusantara.quran.data.lokal.entitas.*
import kotlinx.coroutines.flow.Flow

/** Akses data mushaf dari Room. */
@Dao
interface DaoQuran {
    @Query("SELECT * FROM surah ORDER BY nomor") fun alurSurah(): Flow<List<EntitasSurah>>
    @Query("SELECT * FROM ayat WHERE nomorSurah = :nomor ORDER BY nomorAyat") suspend fun ayatSurah(nomor: Int): List<EntitasAyat>
    @Query("SELECT * FROM ayat WHERE teksArab LIKE '%' || :kata || '%' ORDER BY nomorSurah, nomorAyat") suspend fun cariAyat(kata: String): List<EntitasAyat>
    @Query("SELECT * FROM bookmark ORDER BY tanggalDibuat DESC") fun alurBookmark(): Flow<List<EntitasBookmark>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpanSurah(data: List<EntitasSurah>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpanAyat(data: List<EntitasAyat>)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun simpanBookmark(data: EntitasBookmark)
    @Delete suspend fun hapusBookmark(data: EntitasBookmark)
    @Query("UPDATE bookmark SET judulCatatan = :judul, isiCatatan = :isi WHERE id = :id") suspend fun ubahCatatan(id: Long, judul: String, isi: String)
}
