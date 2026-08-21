
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
