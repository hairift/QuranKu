# QuranKu — Penyelesaian Fitur 100%

## Yang dikerjakan

Melanjutkan pekerjaan yang terhenti dan menambahkan seluruh fitur yang diminta hingga **build `assembleDebug` sukses tanpa warning**. APK debug tersedia di `app/build/outputs/apk/debug/app-debug.apk`.

### Fitur baru yang ditambahkan

| Fitur | Implementasi |
|---|---|
| Fitur Disarankan | Baris pintasan geser di Beranda ke semua fitur utama |
| Ayat Hari Ini | Sudah aktif, kini bisa disentuh untuk membuka ayat di mushaf |
| Riwayat Bacaan | Tercatat otomatis dari mushaf (tabel Room `riwayat`), pratinjau di Beranda + layar penuh (hapus per-butir/semua) |
| Bacaan Unggulan | 12 bacaan anjuran (Al-Kahfi, Yasin, Al-Mulk, Ayat Kursi, dst) dari `unggulan.json` |
| Quran & Sains | 18 fenomena ilmiah + ayat rujukan berbuka-tutup dari `sains.json` |
| Penjelajah Topik | 8 kategori (akhlak, keluarga, rezeki, dll) + **topik berdasarkan suasana hati** (sedih, cemas, marah, dll) dari `topik.json` |
| Voice note (cari suara) | Mikrofon SpeechRecognizer bahasa Indonesia di layar Cari Ayat + izin `RECORD_AUDIO` |
| Bookmark | Sudah ada (Room), kini ikut terekspor/impor |
| Pengaturan | Diperluas: tema (diperbaiki agar benar-benar aktif), pengingat, kota, dst |
| Pembersihan penyimpanan | Hitung & bersihkan cache dari layar Saya |
| Ekspor & Impor | Cadangan JSON (pengaturan + bookmark + riwayat) via pemilih berkas Android |
| Tentang Kami | Layar baru: pengembang, sumber data, lisensi |
| Beri Rating | Membuka halaman Google Play |
| Berbagi Aplikasi | Lembar berbagi Android dengan tautan Play Store |
| Donasi | https://trakteer.id/fira73 di layar Saya (menu + kartu) dan Tentang Kami |

### Berkas utama

- Data: `app/src/main/assets/data-lokal/{sains,topik,unggulan}.json`
- Layar baru: `fitur/riwayat`, `fitur/unggulan`, `fitur/sains`, `fitur/topik`, `fitur/tentang`
- Util: `inti/util/PengelolaPenyimpanan.kt`, `inti/util/PengelolaCadangan.kt`
- DB naik ke versi 2 (tabel `riwayat`)

### Catatan

- `gradlew` bawaan rusak; build memakai Gradle 8.7 lokal yang sudah terunduh. Disarankan memperbaiki wrapper jar di lain waktu.
- Sisa sebelum rilis publik: folder bookmark, cache audio offline, tafsir, keystore produksi, dan pengujian perangkat (lihat `docs/STATUS-FITUR.md`).
