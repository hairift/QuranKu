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