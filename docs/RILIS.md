# Prosedur Rilis

## Paket Instalasi Siap Pakai (GitHub Releases)

Perintah `gradlew.bat assembleRelease` menghasilkan paket instalasi produksi `app-release.apk` yang disalin menjadi `QuranKu-v1.3.0.apk` untuk diunggah ke halaman GitHub Releases agar siap dipasang langsung oleh pengguna Android.

## Rilis Toko Aplikasi (Google Play Store)

1. Buat berkas keystore produksi pribadi di luar repositori.
2. Simpan kata sandi melalui `gradle.properties` lokal atau variabel rahasia CI/CD.
3. Arahkan `signingConfig` pada `app/build.gradle.kts` ke keystore produksi tersebut.
4. Jalankan `gradlew.bat clean assembleRelease`.
5. Verifikasi tanda tangan digital menggunakan `apksigner verify --verbose`.
6. Lakukan uji instalasi dan pemeriksaan fitur utama sebelum publikasi.
