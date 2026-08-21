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