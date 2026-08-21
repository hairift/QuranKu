# Prosedur Rilis

## Rilis pengembangan

`assembleRelease` menghasilkan `app-release.apk` bertanda tangan debug lokal agar bisa dipasang untuk QA. Jangan unggah berkas ini ke Play Store.

## Rilis produksi

1. Buat keystore pribadi di luar repositori.
2. Simpan password melalui `gradle.properties` lokal atau secret CI.
3. Ganti `signingConfig` `rilisLokal` pada `app/build.gradle.kts` dengan konfigurasi keystore produksi.
4. Jalankan `gradlew.bat clean assembleRelease`.
5. Verifikasi tanda tangan dengan `apksigner verify --verbose`.
6. Jalankan uji instalasi, smoke test, dan backup sebelum unggah.
