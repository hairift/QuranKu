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
import id.nusantara.quran.data.remote.api.ApiJadwalSholat
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/** Penyedia dependensi data lokal dan layanan jaringan. */
@Module
@InstallIn(SingletonComponent::class)
object ModulData {

    @Provides
    @Singleton
    fun basisData(@ApplicationContext konteks: Context): BasisDataQuran =
        Room.databaseBuilder(konteks, BasisDataQuran::class.java, "quranku.db").build()

    @Provides
    fun dao(basis: BasisDataQuran): DaoQuran = basis.daoQuran()

    @Provides
    @Singleton
    fun klienHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    /** Layanan jadwal sholat resmi dari data Kemenag melalui MyQuran. */
    @Provides
    @Singleton
    fun apiJadwalSholat(klien: OkHttpClient): ApiJadwalSholat = Retrofit.Builder()
        .baseUrl("https://api.myquran.com/")
        .client(klien)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(ApiJadwalSholat::class.java)
}
