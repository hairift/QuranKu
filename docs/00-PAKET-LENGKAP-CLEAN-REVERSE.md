# PAKET LENGKAP CLEAN REVERSE ENGINEERING - QURANKU
# QURANKU - BACA, PAHAMI, AMALKAN
# Total 8 dokumen + assets logo

Dokumen ini adalah gabungan semua file .md yang dibutuhkan Codex untuk build APK sekali jadi, aman lisensi GPL, UI original, anti AI slop, dan sudah include branding resmi.

---



# ===== FILE: 00_PANDUAN_CLEAN_ROOM.md =====

# 00 - Panduan Clean Room Reverse Engineering (Anti Lisensi)

## Tujuan
Kita TIDAK BOLEH menyalin kode, nama variabel, struktur file, komentar, atau aset dari https://github.com/alfaazplus/QuranApp. Yang kita lakukan adalah **Clean Room Reverse Engineering**.

## Definisi Clean Room
1.  **Tim Spek (Fase 1):** Melihat aplikasi referensi HANYA untuk mencatat FUNGSI-nya. Outputnya adalah dokumen PRD berbahasa Indonesia yang mendeskripsikan *apa* yang dilakukan aplikasi, BUKAN *bagaimana* kodenya.
2.  **Tim Implementasi (Fase 2 - Codex):** TIDAK PERNAH melihat source code asli. Hanya membaca PRD kita. Menulis ulang semua dari nol dengan arsitektur, nama variabel, dan UI milik kita sendiri 100% Bahasa Indonesia.

## Kenapa Ini Aman?
- Lisensi QuranApp adalah GPL v3 (umum untuk app Quran open source). Jika copy-paste, aplikasi kamu wajib open source GPL juga.
- Dengan clean room, kamu hanya meniru **ide dan fungsionalitas** yang tidak dilindungi hak cipta. Ekspresi (kode, UI, icon, teks) adalah buatan baru → kamu pemilik penuh, bisa pakai lisensi MIT / Proprietary.

## Bukti Kepatuhan
Simpan folder ini sebagai bukti:
- `/docs/prd/` -> spesifikasi fungsional
- `/docs/legal/` -> file ini
- Log prompt Codex

---


# ===== FILE: 01_PRD_QURAN_APP_ID.md =====

# 01 - PRD - Aplikasi Al-Qur'an Nusantara (Kotlin) - Versi Clean Room

> Dokumen ini adalah hasil observasi fungsional dari QuranApp, ditulis ulang 100% dengan bahasa Indonesia untuk Tim Implementasi. TIDAK mengandung potongan kode asli.

## 1. Visi Produk
Aplikasi Al-Qur'an offline-first, tanpa iklan, tanpa izin berbahaya (storage, GPS, kamera tidak wajib), fokus privasi, super ringan, dengan dukungan penuh Bahasa Indonesia dan 22+ bahasa lain. Tampilan modern, bukan AI slop.

## 2. Target Pengguna
- Muslim Indonesia yang butuh mushaf offline
- Penghafal, pelajar tafsir
- User yang butuh terjemahan Kemenag RI

## 3. Daftar Fitur Wajib (Paritas 100% + Ekstra Indonesia)

### A. Inti Mushaf
- [ ] Mode Ayat-per-Ayat (Verse-by-Verse)
- [ ] Mode Mushaf (halaman seperti cetakan Madinah) dengan variable lines (15 baris customizable)
- [ ] Navigasi: Surah (114), Juz (30), Hizb (60), Halaman (604), Ruku, Manzil, Sajda
- [ ] Lompat cepat: dialog "Lompat ke Ayat" -> input Surah:Ayah, Juz, Halaman
- [ ] Font Arab: minimal 3 keluarga -> Utsmani Hafs, Indopak (Kemenag), KFGQPC v1 & v2 + Noorehuda. Ukuran pinch-to-zoom terpisah untuk Arab dan Terjemahan

### B. Terjemahan & Tafsir
- [ ] 45+ terjemahan dalam 20+ bahasa (data dari QuranEnc, Tanzil). WAJIB: Bahasa Indonesia - Kemenag RI, Tafsir Jalalain Indonesia, Bahasa Indonesia Sabiq (ringkas)
- [ ] Tampilkan multi-terjemahan sekaligus (max 3 bersusun)
- [ ] Preview footnote dan referensi ayat saat klik nomor footnote
- [ ] 10+ tafsir multibahasa offline setelah diunduh

### C. Audio Murottal
- [ ] 18+ Qari (dari everyayah.com / quranicaudio.com)
- [ ] Audio terjemahan (10+ bahasa)
- [ ] Mode putar: Hanya Quran, Hanya Terjemahan, Quran + Terjemahan
- [ ] Repeat custom: per ayat, range ayat (misal 2:255-2:257 x10)
- [ ] Kontrol kecepatan 0.5x - 2.0x
- [ ] Background player dengan notifikasi MediaStyle
- [ ] Spotlight mode: ayat yang diputar di-highlight + auto-scroll
- [ ] Download per surah / per juz / keseluruhan, dengan manajemen storage cleanup UI

### D. Pencarian
- [ ] Advanced search: cari di Arab, transliterasi, terjemahan hingga 5 kata kunci AND/OR
- [ ] Voice search (SpeechToText)
- [ ] Riwayat pencarian
- [ ] Hasil klik -> langsung buka ayat dengan terjemahan yang relevan ter-highlight
- [ ] Pencarian full-word atau partial

### E. Bookmark & Catatan
- [ ] Bookmark 1 ayat atau rentang ayat
- [ ] Tambah catatan pribadi per bookmark
- [ ] Tag warna kustom
- [ ] Export / Import bookmark (JSON + CSV)
- [ ] Folder bookmark (misal: Doa Harian, Kajian)

### F. Konten Tematik (Dari Quran)
- [ ] Doa-doa dari Al-Qur'an
- [ ] Solusi dari Al-Qur'an
- [ ] Nabi yang disebut dalam Al-Qur'an
- [ ] Adab / Etika
- [ ] Dosa-dosa besar
- [ ] Quran dan Sains
- [ ] Semua konten ini multi-bahasa

### G. Lain-lain
- [ ] Pengingat Ayat Harian (Verse of the Day) dengan jam kustom, WorkManager
- [ ] Berbagi Ayat Advanced: share sebagai teks, gambar dengan background kustom (canvas), dengan info surah
- [ ] Tema: Light, Dark, AMOLED + 5+ Material You warna + Dynamic Color Android 12+
- [ ] 22 bahasa UI, WAJIB Bahasa Indonesia sebagai default, plus Inggris, Arab
- [ ] Tanpa iklan, tanpa analytics, tanpa pelacakan

### H. Fitur Ekstra Nusantara (Nilai Tambah, pakai Daftar API Lokal Indonesia)
Ambil dari https://github.com/farizdotid/DAFTAR-API-LOKAL-INDONESIA
- [ ] Jadwal Sholat (API MyQuran / Kemenag) + notifikasi adzan offline
- [ ] Arah Kiblat (kompas sensor + kalkulasi)
- [ ] Kalender Hijriah Indonesia
- [ ] Teks Kemenag resmi dari quran.kemenag.go.id (sebagai sumber terjemahan utama)

## 4. Kebutuhan Non-Fungsional
- Offline-first: semua mushaf, terjemahan, tafsir, audio harus bisa offline setelah download
- Ukuran APK awal < 25 MB tanpa data
- Min SDK 24, Target SDK 34
- 100% Kotlin, Jetpack Compose
- Tidak meminta izin READ_EXTERNAL_STORAGE, LOCATION, CAMERA kecuali fitur kiblat butuh sensor kompas saja

## 5. Sumber Data Legal (Bukan dari Repo Asli)
- Teks Arab: Tanzil (CC BY-ND 3.0) atau Kemenag RI
- Terjemahan: QuranEnc API
- Audio: everyayah.com, quranicaudio.com (public)
- Font: KFGQPC, Uthmani dari King Fahad Complex (lisensi bebas)

---


# ===== FILE: 02_ATURAN_WAJIB_DAN_LARANGAN.md =====

# 02 - Aturan WAJIB dan LARANGAN - Clean Room

## WAJIB DILAKUKAN (DO)

### 1. Penamaan & Bahasa
- WAJIB: Semua variabel, fungsi, kelas, file, komentar, dokumentasi, commit message, issue GitHub, dan PR menggunakan **Bahasa Indonesia** yang baku (bukan campur Inggris). Contoh: `DaftarSurahScreen`, `PemutarAudioService`, `RepositoriAyat`, `fungsi cariAyat()`
- WAJIB: Komentar kode pakai Bahasa Indonesia
- WAJIB: Dokumentasi `.github/`, `README.md`, `CONTRIBUTING.md` full Bahasa Indonesia

### 2. Arsitektur Baru
- WAJIB: Pakai arsitektur Modern Android: MVVM + Clean Architecture (Domain, Data, Presentation)
- WAJIB: Stack: Kotlin, Jetpack Compose, Material 3, Room, DataStore, ExoPlayer / Media3, WorkManager, Hilt
- WAJIB: Struktur paket berbeda total dari referensi. Usulan: `id.nusantara.quran` -> `fitur.mushaf`, `fitur.audio`, `fitur.pencarian`, `fitur.bookmark`, `fitur.tematik`, `data.lokal`, `data.remote`, `inti.ui`

### 3. UI/UX Original Anti AI Slop
- WAJIB: Desain UI berbeda 100% dari QuranApp. Jangan pakai layout list yang sama persis.
- Konsep UI Baru (Nusantara Modern):
  - BottomNav dengan 4 tab: Beranda, Mushaf, Audio, Saya
  - Gaya: Minimalis, whitespace besar, kartu dengan rounded 24dp, typography Outfit / Plus Jakarta Sans
  - Warna: Hijau Zamrud #0E5E4D sebagai primary, krem #FFFBF0 background
  - Jangan pakai emoji sebagai icon. WAJIB pakai Material Symbols atau Phosphor Icons (real vector icon)
  - Animasi: Shared element transition antar ayat
- WAJIB: Buat Design System sendiri di `inti/ui/tema/`

### 4. Bahasa
- WAJIB: Support multi-bahasa dengan `strings.xml`. Default `in` (Indonesia), fallback `en`, `ar`. Pastikan semua string di-extract, jangan hardcode.
- Indonesia BELUM ada di referensi, jadi kita wajib bikin terjemahan UI lengkap Indonesia

### 5. Lisensi & Legal
- WAJIB: Cantumkan kredit sumber data: Tanzil, QuranEnc, Kemenag, EveryAyah di menu Tentang
- WAJIB: Lisensi project kamu sendiri (disarankan MIT atau Proprietary)

## DILARANG KERAS (DON'T)

- DILARANG: Membuka folder source code QuranApp saat coding (hanya boleh baca PRD ini)
- DILARANG: Copy-paste file `.kt`, `.xml`, `.gradle`, `.json` dari repo referensi
- DILARANG: Meniru nama variabel / fungsi seperti `QuranViewModel`, `AyahAdapter` -> harus ganti jadi `ModelTampilanAyat` tidak boleh mirip 1:1
- DILARANG: Meniru struktur folder `com.quranapp` atau `alfaazplus`
- DILARANG: Menggunakan screenshot, logo, atau icon dari referensi
- DILARANG: Pakai emoji 💚 📖 🎙️ sebagai icon utama aplikasi (ini ciri AI slop)
- DILARANG: Menulis komentar bahasa Inggris kalau ada padanan Indonesianya
- DILARANG: Hardcode API key di repo
- DILARANG: Minta izin `READ_EXTERNAL_STORAGE`, `ACCESS_FINE_LOCATION` di manifest kecuali fitur kiblat
- DILARANG: Menambahkan iklan, Firebase Analytics, atau tracker apapun

## Checklist Anti Plagiarisme Sebelum Build APK
- [ ] `grep -r "alfaazplus" .` -> harus 0 hasil
- [ ] `grep -r "quranapp" . --ignore-case` -> harus 0 hasil
- [ ] Semua file `.kt` punya header komentar Bahasa Indonesia
- [ ] Icon di `res/drawable` adalah vector real (cek isinya `<vector>`, bukan emoji)
- [ ] `strings.xml` punya minimal 3 bahasa: in, en, ar

---


# ===== FILE: 03_PROMPT_MASTER_CODEX.md =====

# 03 - Prompt Master untuk Codex - Build Sekali Jadi

> Copy-paste prompt di bawah ini ke Codex / Codex CLI. Ini dirancang untuk mengerjakan langsung sampai APK jadi, tidak setengah-setengah.

---

## PROMPT 1: Inisialisasi Project (Sekali Jalan Sampai APK)

```
Kamu adalah Senior Android Engineer Indonesia. Tugasmu adalah membangun aplikasi Al-Qur'an Nusantara dari NOL menggunakan Kotlin + Jetpack Compose dengan metode CLEAN ROOM.

ATURAN MUTLAK:
1. JANGAN PERNAH lihat atau buka repo https://github.com/alfaazplus/QuranApp. Kamu hanya boleh kerja berdasarkan PRD di /docs/01_PRD_QURAN_APP_ID.md dan Aturan di /docs/02_ATURAN_WAJIB_DAN_LARANGAN.md
2. Semua kode, variabel, fungsi, komentar, dokumentasi WAJIB Bahasa Indonesia 100%.
3. UI/UX harus ORIGINAL, modern, minimalis Nusantara, BUKAN tiruan. Dilarang pakai emoji sebagai icon, WAJIB pakai Material Symbols Rounded.
4. Arsitektur: MVVM + Clean Architecture. Paket root: id.nusantara.quran
5. Fitur harus paritas 100% dengan PRD + fitur ekstra Indonesia (jadwal sholat, kiblat).
6. Hasil akhir harus bisa di-build menjadi APK debug & release.

LANGKAH KERJA YANG HARUS KAMU LAKUKAN LANGSUNG SEKARANG (JANGAN BERHENTI DI TENGAH):

Fase A - Setup:
- Buat project Android Kotlin baru (minSdk 24, target 34, Compose BOM terbaru)
- Setup Hilt, Room, DataStore, Media3 ExoPlayer, WorkManager, Retrofit, Navigation Compose
- Buat struktur folder: fitur/mushaf, fitur/audio, fitur/pencarian, fitur/bookmark, fitur/tematik, fitur/pengaturan, data/lokal, data/remote, inti/ui/tema
- Buat tema Material 3 kustom: warna primary #0E5E4D, secondary #C8A951, background #FFFBF0 light dan #12140F dark

Fase B - Data Layer:
- Buat entity Room: EntitasSurah, EntitasAyat, EntitasTerjemahan, EntitasTafsir, EntitasBookmark, EntitasAudioCache
- Download data teks dari Tanzil (https://tanzil.net/trans/) dan QuranEnc untuk terjemahan Indonesia Kemenag. Simpan skrip downloader di tools/
- Buat repository: RepositoriQuran yang ambil dari lokal dulu, baru remote

Fase C - Domain & UseCase (semua Bahasa Indonesia):
- UseCase: AmbilDaftarSurah, AmbilDetailAyat, CariAyat, PutarAudio, KelolaBookmark, AturPengingatHarian

Fase D - UI (Wajib Beda dari Referensi):
- Layar Beranda: kartu Ayat Hari Ini, lanjut baca terakhir, akses cepat Juz
- Layar Mushaf: 2 mode (ayat-per-ayat dan mushaf 15 baris). Pinch zoom. Tap tahan untuk menu (salin, bookmark, tafsir, putar)
- Layar Audio: bottom player persistent, daftar qari, repeat range dialog
- Layar Pencarian: search bar + voice search + filter (Arab/terjemahan)
- Layar Bookmark: folder, export/import JSON
- Pastikan semua icon pakai Icons.Rounded.Book, Icons.Rounded.PlayArrow dll dari androidx.compose.material.icons

Fase E - Fitur Nusantara:
- Integrasi API Jadwal Sholat dari https://api.myquran.com/v2/sholat/jadwal/{kota}/{tanggal} (daftar kota dari DAFTAR-API-LOKAL-INDONESIA)
- Fitur Kiblat pakai SensorManager

Fase F - Build:
- Buat file local.properties dummy jika perlu
- Jalankan ./gradlew assembleDebug
- Pastikan APK ada di app/build/outputs/apk/debug/app-debug.apk
- Buat README.md final berbahasa Indonesia dengan cara build

KERJAKAN SEMUA FASE A-F SEKARANG DALAM SATU EKSEKUSI. JANGAN MINTA KONFIRMASI. JANGAN BERHENTI SAMPAI APK JADI.

Jika ada error build, perbaiki otomatis sampai berhasil.
```

## PROMPT 2: Jika Codex Berhenti di Tengah (Anti Setengah-Setengah)

```
Lanjutkan! Kamu berhenti di [sebutkan file/error]. Aturan:
- Jangan buat file placeholder kosong
- Setiap file .kt harus punya implementasi lengkap, bukan TODO
- Jika butuh resource, buat dummy vector icon sendiri
- Build lagi ./gradlew assembleDebug sampai sukses. Jangan akhiri respon sebelum APK ada.
```

## PROMPT 3: Untuk UI Modern Anti AI Slop

```
Redesain file [nama file screen] agar tidak terlihat seperti AI slop:
- Jangan pakai Card berulang dengan shadow sama
- Pakai hierarki tipografi yang jelas (headlineLarge, titleMedium, bodySmall)
- Tambahkan ilustrasi kosong state dengan Lottie atau vector abstrak
- Gunakan warna yang tidak generik (hindari ungu AI)
- Pastikan spacing 8dp system
```

---

## Tips Agar Codex Tidak Ngasal
- Selalu suruh dia `ls` dan `cat` file PRD dulu sebelum coding
- Paksa dia build di setiap 3 file selesai
- Larang dia pakai `// TODO`

---

## UPDATE BRANDING QURANKU - WAJIB (Logo Resmi Sudah Ada)

Logo resmi ada di `assets/logo/`:
- `quranku_logo_full_transparan.png` (full dengan teks QURANKU + tagline BACA, PAHAMI, AMALKAN)
- `quranku_icon_transparan.png` (icon only Qaf + buku)
- `ic_launcher_foreground.png` dan `ic_launcher_background.png` untuk adaptive icon
- `ic_launcher_playstore-512.png` untuk Play Store

TAMBAHKAN INSTRUKSI INI KE CODEX DI FASE A:

```
[BRANDING]
- JANGAN PERNAH generate logo baru dengan AI. DILARANG.
- Warna resmi dari logo: primary #0E5E4D, light #7ED0BF, background #FFFBF0, text #0E2F28. Jangan pakai ungu.
- Splash Screen: Scaffold background #FFFBF0, center Image(painter = quranku_logo_full_transparan, size 200dp)
- Launcher Icon: Buat res/mipmap-anydpi-v26/ic_launcher.xml sebagai adaptive-icon dengan background #FFFBF0 dan foreground ic_launcher_foreground
- TopAppBar: navigationIcon pakai quranku_icon_transparan 32dp
- Empty state, about screen: pakai full logo
```

Jika Codex tidak menemukan assets/logo, suruh dia buat folder dan copy dari /mnt/data/ yang sudah disediakan.


---


# ===== FILE: 04_KOREKSI_DAN_QA_AI.md =====

# 04 - Koreksi Jawaban AI & QA Supaya Tidak Asal Buat

## Masalah Umum Codex / AI Saat Build App Quran

### 1. AI SLOP UI (Paling Sering)
- **Ciri:** Semua layar pakai `Card(elevation=4dp)`, warna ungu, icon emoji 📖, layout membosankan, semua teks center
- **Koreksi Prompt:** 
  > "UI kamu terlalu AI slop. Ulangi dengan: pakai Scaffold dengan TopAppBar large, gunakan LazyColumn dengan sticky header untuk surah, warna primary #0E5E4D bukan ungu, icon wajib dari material-icons-extended, tambahkan empty state illustration, dan jangan pernah pakai emoji sebagai icon."

### 2. Plagiarisme Tidak Sadar
- **Ciri:** AI membuat paket `com.alfaazplus.quranapp` atau variabel `quranViewModel`
- **Koreksi:** Jalankan script `python tools/cek_plagiarisme.py` (buat script yang scan kata terlarang). Jika ketemu, suruh rename total.

### 3. Hardcode Bahasa Inggris
- **Ciri:** `Text("Search")` langsung di Compose
- **Koreksi Prompt:**
  > "Semua string hardcode adalah BUG. Pindahkan ke strings.xml (values-in, values-en, values-ar). Di Compose pakai stringResource(R.string.cari). Tidak ada toleransi."

### 4. Fitur Setengah Jadi (TODO)
- **Ciri:** `fun putarAudio() { // TODO }`
- **Koreksi:** Setting di Codex: `Ganti semua TODO dengan implementasi Media3 ExoPlayer real. Jika belum bisa, pakai mock data tapi UI tetap jalan. Jangan biarkan fungsi kosong.`

### 5. Izin Berbahaya
- **Ciri:** Menambahkan `<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>`
- **Koreksi:** "Hapus semua permission berbahaya. App ini offline-first pakai internal storage (getFilesDir), bukan external. Hanya izinkan INTERNET, FOREGROUND_SERVICE, POST_NOTIFICATIONS, dan SENSOR untuk kiblat."

### 6. Icon Emoji
- **Ciri:** `Text("📖")` atau `Icon(\uD83D\uDCC6)`
- **Koreksi:** "DILARANG pakai emoji sebagai icon. Ganti dengan `Icon(Icons.Rounded.MenuBook, ...)` atau import Phosphor Icons. Emoji hanya boleh di dalam teks ayat/terjemahan, bukan UI."

### 7. Build Gagal Terus
- **Solusi Otomatis:**
  ```
  ./gradlew assembleDebug --stacktrace
  ```
  Jika error Hilt, cek @HiltAndroidApp di Application.
  Jika error Compose, cek kotlinCompilerExtensionVersion.
  Jika error Room, cek @Entity primaryKey.

## Checklist QA Sebelum Dianggap Selesai

### Fungsional
- [ ] Buka app tanpa internet -> mushaf tetap muncul (dari Room)
- [ ] Ganti font Arab -> ukuran berubah real-time
- [ ] Putar audio -> background player muncul + notifikasi
- [ ] Cari "Allah Maha Pengasih" -> hasil < 1 detik
- [ ] Bookmark range 2:255-2:257 + catatan -> export JSON -> import lagi -> data kembali
- [ ] Ganti bahasa UI ke Indonesia -> semua menu jadi Indonesia

### Non-Fungsional Anti AI Slop
- [ ] Tidak ada layar yang semua isinya Card abu-abu
- [ ] Tidak ada emoji di AppBar / BottomNav
- [ ] Ada animasi transisi antar layar
- [ ] Ada dark mode dan light mode yang konsisten
- [ ] Icon launcher buatan sendiri (bukan default Android)

### Legal Clean Room
- [ ] Jalankan `grep -R "alfaaz" .` -> 0
- [ ] `LICENSE` file adalah MIT milik kamu, bukan GPL copy
- [ ] Folder `.github` berisi template issue berbahasa Indonesia buatan kamu

## Template Koreksi Cepat (Copy-Paste ke Codex)

**Jika Codex ngarang API:**
> "API yang kamu pakai tidak ada di DAFTAR-API-LOKAL-INDONESIA. Pakai yang resmi: MyQuran untuk jadwal sholat, quran.kemenag.go.id untuk teks. Jangan ngarang endpoint."

**Jika Codex buat UI jelek:**
> "Desainmu masih terlihat AI. Lihat referensi Dribbble 'Islamic app minimal'. Buat ulang dengan: header melengkung, bottom sheet untuk tafsir, bukan dialog biasa."

**Jika Codex campur bahasa:**
> "Komentar dan nama fungsi masih bahasa Inggris. Refactor semua ke Bahasa Indonesia: `getSurahList` -> `ambilDaftarSurah`, `AudioPlayer` -> `PemutarAudio`."

---


# ===== FILE: 05_ARSITEKTUR_TEKNIS_KOTLIN.md =====

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

---


# ===== FILE: 06_UI_UX_GUIDELINE_ANTI_AI_SLOP.md =====

# 06 - UI/UX Guideline - Modern Nusantara Anti AI Slop

## Prinsip: Jangan Terlihat Seperti Template AI

### DILARANG (Ciri AI Slop):
- Background gradient ungu-biru
- Semua sudut 16dp Card yang sama
- Icon emoji
- Ilustrasi 3D generic
- Teks Lorem Ipsum
- Tidak ada empty state

### WAJIB (Modern 2026):

#### 1. Warna & Tema
- Light: background #FFFBF0 (krem kertas), surface #FFFFFF, primary #0E5E4D (hijau zamrud dalam), secondary #C8A951 (emas)
- Dark: background #12140F, surface #1E201A, primary #7ED0BF
- Dynamic Color: ambil dari wallpaper user jika Android 12+ tapi tetap tint hijau
- Jangan pakai Material default purple!

#### 2. Tipografi
- Judul Arab: font Amiri / KFGQPC Uthmanic Script (ukuran 28sp)
- Judul Latin: Plus Jakarta Sans Bold
- Body: Inter / Outfit
- Semua teks Arab rata kanan, terjemahan rata kiri

#### 3. Icon Real
- Pakai: `androidx.compose.material.icons:material-icons-extended`
- Contoh: 
  - Mushaf -> Icons.Rounded.MenuBook (bukan 📖)
  - Audio -> Icons.Rounded.GraphicEq
  - Pencarian -> Icons.Rounded.Search
  - Bookmark -> Icons.Rounded.Bookmark
  - Kiblat -> Icons.Rounded.Explore
  - Jadwal Sholat -> Icons.Rounded.Schedule
- Ukuran icon 24dp, stroke 2px

#### 4. Layout Signature (Beda dari QuranApp)
- Beranda: 
  - Header melengkung bawah (curved bottom) dengan jam + tanggal Hijriah
  - Kartu Lanjutkan Membaca dengan progress bar
  - Grid 2 kolom untuk menu Tematik (Doa, Sains, dll) dengan icon besar
- Mushaf:
  - Ayat-per-ayat: tiap ayat dalam kartu dengan nomor ayat di dalam lingkaran emas, bukan kotak
  - Mode Mushaf: halaman dengan garis bismillah tengah, nomor halaman di bawah dengan ornamen islami (bukan angka biasa)
- Player Audio: Bottom sheet yang bisa di-expand jadi full screen dengan visualizer

#### 5. Animasi Halus
- Transisi antar layar pakai `AnimatedContent` + slide
- Saat ganti ayat, pakai crossfade 150ms
- Bookmark animasi scale

#### 6. Aksesibilitas
- Support TalkBack untuk ayat
- Ukuran teks Arab bisa sampai 40sp untuk lansia

## Referensi Inspirasi (Bukan untuk Ditiru Persis):
- Dribbble: "Quran App Minimal"
- App: Al-Quran Indonesia Kemenag (untuk struktur data, bukan UI)

## Deliverable UI:
- Figma wireframe tidak wajib, tapi screenshot 5 layar utama wajib ada di README
- Semua drawable vector buatan sendiri di `res/drawable/`

---


# ===== FILE: 07_BRANDING_QURANKU.md =====


# 07 - Branding QURANKU - Baca, Pahami, Amalkan

## Logo Resmi
Logo yang kamu upload sudah diproses menjadi versi transparan.

### File Asset
- Original: assets/logo/quranku_original.jpg
- Full Logo Transparan (logo + teks QURANKU + tagline): assets/logo/quranku_logo_full_transparan.png
- Icon Only Transparan (simbol Qaf + buku): assets/logo/quranku_icon_transparan.png
- Launcher: assets/android_launcher/
  - ic_launcher_foreground.png (432x432) untuk adaptive icon foreground
  - ic_launcher_background.png untuk background
  - ic_launcher_mipmap-*.png untuk legacy
  - ic_launcher_playstore-512.png untuk Play Store (512x512)

### Makna Logo
- Huruf Qaf Arab stylized dengan titik dua di atas (ق) -> QURAN
- Bentuk buku terbuka menyatu -> mushaf
- Lengkungan bawah seperti ombak -> Nusantara, mengalir
- Gradien Hijau #7ED0BF -> #0E5E4D: kesejukan, kedalaman ilmu
- Tagline BACA, PAHAMI, AMALKAN: positioning beda dari QuranApp yang generik

### Penggunaan di Codex
Wajib instruksikan Codex:

```
Gunakan logo resmi dari assets/logo/:
- Untuk splash screen: pakai quranku_logo_full_transparan.png centered di atas background #FFFBF0
- Untuk launcher icon: buat ic_launcher.xml adaptive icon dengan foreground = ic_launcher_foreground.png dan background = #FFFBF0
- Untuk TopAppBar: pakai icon only 32dp di kiri
- Jangan pernah generate logo baru dengan AI. Pakai file ini.
```

### Warna Resmi (update dari PRD)
- Primary: #0E5E4D (Hijau Zamrud Dalam) - dari logo
- Primary Light: #7ED0BF (dari gradien atas logo)
- Secondary: #C8A951 (emas, kontras)
- Background Light: #FFFBF0 (krem kertas, cocok dengan mockup logo kamu yang background cream)
- Background Dark: #12140F
- Text Primary: #0E2F28

### Tipografi Resmi
- Logo Font: mirip Outfit Bold / Plus Jakarta Sans Bold (untuk QURANKU)
- Tagline: Inter Medium, tracking 1.5, uppercase
- Jangan ganti warna logo di dark mode, tetap hijau di atas krem, tapi untuk adaptive icon dark, background bisa #12140F.

### Larangan Branding
- Dilarang memutar logo
- Dilarang ganti warna logo jadi ungu / biru (harus tetap hijau)
- Dilarang pakai emoji 📖 sebagai pengganti icon logo
- Dilarang menambahkan efek drop shadow berlebihan

### Implementasi di Android
Di AndroidManifest.xml:
```xml
android:icon="@mipmap/ic_launcher"
android:roundIcon="@mipmap/ic_launcher_round"
```

Di res/mipmap-anydpi-v26/ic_launcher.xml:
```xml
<adaptive-icon>
  <background android:drawable="@color/quranku_background_light"/>
  <foreground android:drawable="@mipmap/ic_launcher_foreground"/>
</adaptive-icon>
```


---
