# 05 - Arsitektur Teknis Kotlin (Untuk Codex)

## Stack Final
- Bahasa: Kotlin 1.9.22
- UI: Jetpack Compose BOM 2024.02, Material 3
- DI: Hilt 2.48
- DB: Room 2.6.1
- Preferences: DataStore
- Audio: Media3 ExoPlayer 1.2.1
- Network: Retrofit 2.9 + OkHttp + Moshi
- Async: Coroutines + Flow
- Navigasi: Navigation Compose + Hilt Navigation
- Background: WorkManager
- Build: Gradle KTS

## Struktur Folder WAJIB (Beda dari Referensi)

```
id.nusantara.quran/
├── inti/
│   ├── ui/tema/ (TemaNusantara.kt, Tipografi.kt, Warna.kt)
│   ├── navigasi/ (GrafNavigasi.kt)
│   └── util/ (FormatTanggalHijriah.kt, KompasKiblat.kt)
├── data/
│   ├── lokal/
│   │   ├── basisdata/QuranDatabase.kt
│   │   ├── entitas/EntitasAyat.kt, EntitasSurah.kt
│   │   └── dao/DaoAyat.kt
│   ├── remote/
│   │   ├── api/ApiQuranEnc.kt, ApiJadwalSholat.kt
│   │   └── model/ModelRemote.kt
│   └── repositori/RepositoriQuran.kt (implementasi)
├── domain/
│   ├── model/ModelSurah.kt, ModelAyat.kt
│   └── usecase/AmbilDaftarSurah.kt, CariAyat.kt, PutarAudio.kt
└── fitur/
    ├── beranda/TampilanBeranda.kt
    ├── mushaf/TampilanMushaf.kt + ModelTampilanMushaf.kt
    ├── audio/TampilanAudio.kt + ServisPemutarAudio.kt
    ├── pencarian/TampilanPencarian.kt
    ├── bookmark/TampilanBookmark.kt
    ├── tematik/TampilanTematik.kt
    └── pengaturan/TampilanPengaturan.kt
```

## Contoh Penamaan Clean Room (Bahasa Indonesia)

JANGAN:
```kotlin
class QuranViewModel @Inject constructor(private val quranRepo: QuranRepository)
fun getSurahList()
```

DO (Wajib):
```kotlin
@HiltViewModel
class ModelTampilanMushaf @Inject constructor(
    private val repositoriQuran: RepositoriQuran
) : ViewModel() {
    // Mengambil daftar surah dari basis data lokal
    fun ambilDaftarSurah(): Flow<List<ModelSurah>> { ... }
}
```

## Skema Database Room (Sederhana tapi Lengkap)

EntitasAyat:
- idAyat: Int (PK) -> format: surah*1000 + ayat
- nomorSurah: Int
- nomorAyat: Int
- teksArabUthmani: String
- teksArabIndopak: String
- nomorJuz: Int
- nomorHalaman: Int

EntitasTerjemahan:
- id: String (misal: kemenag_id)
- idAyat: Int (FK)
- bahasa: String (in, en, ar)
- teksTerjemahan: String
- catatanKaki: String?

EntitasBookmark:
- id: Long auto
- idAyatAwal: Int
- idAyatAkhir: Int?
- judulCatatan: String
- isiCatatan: String
- warnaTag: String (hex)
- tanggalDibuat: Long

## Manajemen Audio Offline

- Simpan file di `context.filesDir/audio/qari_id/surah_xxx.mp3`
- Jangan di Download folder (butuh izin)
- Gunakan `CacheDataSource` dari ExoPlayer

## Build APK

./gradlew assembleDebug -> hasil di app/build/outputs/apk/debug/
./gradlew assembleRelease -> butuh keystore.properties