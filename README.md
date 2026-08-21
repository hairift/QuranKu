# Quran Nusantara

<p align="center">
  <img src="assets/logo/quranku_icon_transparan.png" alt="Logo Quran Nusantara" width="128" />
</p>

Versi aplikasi: **1.1.0** · kode versi **2**

Aplikasi Al-Qur'an offline-first untuk pengguna Indonesia, dibangun ulang secara mandiri dengan Kotlin, Jetpack Compose, MVVM, dan Clean Architecture.

## Status

Proyek ini sedang menuju produksi. Alur navigasi dan kontrol UI utama sudah aktif, sedangkan pengisian penuh 114 surah, katalog terjemahan/tafsir, sinkronisasi remote, dan audio offline harus disiapkan melalui pipeline data legal sebelum rilis publik.

## Fitur yang tersedia

- Beranda dengan Ayat Hari Ini, progres membaca, serta pintasan jadwal sholat, kiblat, doa, dan juz.
- Mushaf ayat-per-ayat dengan pilihan mode mushaf 15 baris dan pinch-to-zoom.
- Fondasi audio Media3, pencarian Arab/terjemahan, bookmark folder, tema terang/gelap, jadwal sholat, dan kompas kiblat.
- Menu Cari, Simpan, dan Tematik dapat dibuka langsung dari Beranda; tombol mushaf, audio, tema, bookmark, serta pilihan qari memiliki umpan balik interaktif.

## Struktur pengembangan

- `app`: aplikasi Android Kotlin + Jetpack Compose.
- `inventory`: catatan inventaris data dan sumber legal, bukan salinan dari proyek lain.
- `fastlane`: metadata Play Store berbahasa Indonesia.
- `.github`: pemeriksaan build otomatis.
- `.kotlin`: catatan metadata pengembangan lokal; artefak kompilasi dikecualikan.
- `peacedesign`: catatan design system; implementasi tema berada di paket `inti/ui/tema`.
- Data lokal Room dengan jalur pengembangan remote QuranEnc, Tanzil, Kemenag, dan EveryAyah.

## Menjalankan dan memasang

Pastikan Android SDK dengan platform 34 tersedia, lalu jalankan:

```text
gradlew.bat assembleDebug
gradlew.bat assembleRelease
```

APK debug berada di `app/build/outputs/apk/debug/app-debug.apk`. APK release pengujian bertanda tangan lokal berada di `app/build/outputs/apk/release/app-release.apk` dan dapat dipasang dengan `adb install -r`. Tanda tangan lokal bukan untuk publikasi Play Store; gunakan keystore produksi melalui `signingConfigs` sebelum rilis toko.

Untuk pengujian langsung:

```text
adb install -r app/build/outputs/apk/release/app-release.apk
```

## Data, lisensi, dan atribusi

Gunakan `python tools/unduh_data_quran.py` untuk menyiapkan data dari Tanzil dan QuranEnc sesuai ketentuan sumber masing-masing. Aplikasi ini tidak menyalin source code, aset UI, atau data privat dari proyek referensi. Rincian kewajiban lisensi ada di [dokumen sumber data](docs/SUMBER-DATA-DAN-LISENSI.md).

## Sumber

Tanzil, QuranEnc, Kemenag RI, EveryAyah, dan API MyQuran digunakan sebagai sumber data yang dicantumkan untuk pengembangan aplikasi.
