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