# Status Fitur QuranKu

## Sudah aktif di aplikasi

- Build Kotlin/Compose dengan min SDK 24 dan target SDK 34 (debug dan release lolos lint vital).
- Navigasi Beranda, Mushaf, Audio, dan Saya.
- Kontrol tema terang/gelap.
- Tombol pindah Mushaf, Audio, Cari, Simpan, dan Tematik.
- Mushaf dengan pinch-to-zoom, bookmark lokal, dan dialog lompat cepat ke ayat/juz.
- Pemilihan qari dengan pemutar audio latar belakang Media3 (MediaSessionService) dan notifikasi media.
- Fondasi Room, Hilt, DataStore, Media3, WorkManager, Retrofit, kompas, dan API jadwal sholat.
- Ikon aplikasi vector asli.
- Logo PNG `quranku_icon_transparan.png` dipakai sebagai ikon launcher.
- Bundel lokal QuranEnc berisi 6.236 ayat dari 114 surah, termasuk teks Arab dan terjemahan Indonesia.
- Jadwal sholat per kota dari API MyQuran dengan bundel `kota.json`, pengingat azan via AlarmManager, dan pemasangan ulang alarm otomatis.
- Kompas kiblat dengan derajat serta nama arah mata angin.
- Pengingat ayat harian via WorkManager dengan notifikasi pada jam pilihan pengguna.
- Kajian tematik dengan konten lokal terbundel (`tematik.json`).

## Tahap berikutnya sebelum produksi publik

- Persistensi bookmark/folder/catatan dan ekspor-impor JSON/CSV.
- Cache audio offline dan repeat rentang ayat.
- Katalog dan pembaca tafsir.
- Pengujian UI di emulator Android 7 sampai Android 14.
- Keystore produksi, Play App Signing, privacy policy, dan screenshot store.

Rincian status yang dapat diverifikasi ada di [matriks paritas fitur](PARITAS-FITUR.md).
