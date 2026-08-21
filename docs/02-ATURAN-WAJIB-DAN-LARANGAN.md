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