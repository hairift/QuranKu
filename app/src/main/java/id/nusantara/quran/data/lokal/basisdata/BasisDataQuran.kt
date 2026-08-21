package id.nusantara.quran.data.lokal.basisdata

import androidx.room.Database
import androidx.room.RoomDatabase
import id.nusantara.quran.data.lokal.dao.DaoQuran
import id.nusantara.quran.data.lokal.entitas.*

/** Basis data lokal offline-first aplikasi. */
@Database(entities = [EntitasSurah::class, EntitasAyat::class, EntitasTerjemahan::class, EntitasTafsir::class, EntitasBookmark::class, EntitasAudioCache::class], version = 1, exportSchema = false)
abstract class BasisDataQuran : RoomDatabase() { abstract fun daoQuran(): DaoQuran }
