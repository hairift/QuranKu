package id.nusantara.quran.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import id.nusantara.quran.data.lokal.basisdata.BasisDataQuran
import id.nusantara.quran.data.lokal.dao.DaoQuran
import javax.inject.Singleton

/** Penyedia dependensi data lokal. */
@Module
@InstallIn(SingletonComponent::class)
object ModulData {
    @Provides @Singleton fun basisData(@ApplicationContext konteks: Context): BasisDataQuran = Room.databaseBuilder(konteks, BasisDataQuran::class.java, "quranku.db").build()
    @Provides fun dao(basis: BasisDataQuran): DaoQuran = basis.daoQuran()
}
