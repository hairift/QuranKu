# Status Fitur QuranKu

## Sudah aktif di aplikasi

- Build Kotlin/Compose dengan min SDK 24 dan target SDK 34 (debug dan release lolos lint vital).
- Navigasi Beranda, Mushaf, Audio, dan Saya.
- Kontrol tema terang/gelap yang tersambung ke preferensi (tersimpan dan langsung diterapkan).
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
- **Ayat Hari Ini** di beranda, berganti setiap hari dan bisa disentuh untuk membuka ayatnya di mushaf.
- **Fitur Disarankan**: baris pintasan geser di beranda menuju seluruh fitur utama.
- **Riwayat Bacaan**: tercatat otomatis saat membaca mushaf (Room `riwayat`), pratinjau di beranda, layar penuh dengan hapus per-butir dan bersihkan semua.
- **Bacaan Unggulan**: 12 bacaan anjuran waktu terbaik (Al-Kahfi, Yasin, Al-Mulk, Ayat Kursi, dst) dari `unggulan.json`, pratinjau di beranda dan layar penuh.
- **Quran & Sains**: 18 fenomena ilmiah beserta ayat rujukan dari `sains.json`, dengan teks Arab dan terjemahan per ayat.
- **Penjelajah Topik**: telusur ayat berdasarkan kategori (akhlak, keluarga, rezeki, iman, sabar, taubat, akhirat, ilmu) dan berdasarkan suasana hati (sedih, cemas, syukur, marah, kesepian, dst) dari `topik.json`.
- **Pencarian suara (voice note)**: mikrofon di layar pencarian memakai SpeechRecognizer berbahasa Indonesia dengan izin RECORD_AUDIO runtime.
- **Pembersihan penyimpanan**: hitung ukuran cache dan bersihkan dari layar Saya.
- **Ekspor & Impor**: cadangan JSON (pengaturan + bookmark + riwayat) via Storage Access Framework.
- **Tentang Kami**: profil pengembang, sumber data, dan lisensi.
- **Beri Rating**: membuka halaman aplikasi di Google Play.
- **Berbagi Aplikasi**: lembar berbagi Android dengan tautan Play Store.
- **Donasi**: tautan Trakteer pengembang (https://trakteer.id/fira73) di layar Saya dan Tentang Kami.

## Tahap berikutnya sebelum produksi publik

- Folder bookmark dan catatan per-bookmark (entitas sudah mendukung, UI belum).
- Cache audio offline dan repeat rentang ayat.
- Katalog dan pembaca tafsir.
- Pengujian UI di emulator Android 7 sampai Android 14.
- Keystore produksi, Play App Signing, privacy policy, dan screenshot store.

Rincian status yang dapat diverifikasi ada di [matriks paritas fitur](PARITAS-FITUR.md).
