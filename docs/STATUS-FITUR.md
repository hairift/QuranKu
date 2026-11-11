# Status Fitur QuranKu

## Sudah aktif di aplikasi (Versi 1.3.0)

- Build Kotlin/Compose dengan min SDK 24 dan target SDK 34 (debug dan release lolos lint vital tanpa peringatan kompilasi).
- Navigasi bawah (Bottom Navigation Bar) dengan pembeda visual kontras tinggi antara tab aktif (kapsul zamrud + ikon isi putih + teks tebal) dan tab tidak aktif (ikon garis redup).
- Sistem desain **Mushaf Nusantara** berpalet kertas krem gading hangat, zamrud tua, emas antik, serta permukaan kartu bersih tanpa efek kabur/abu-abu (`surfaceTint` nol) baik pada mode terang maupun mode gelap.
- Seluruh ikon menggunakan aset vektor asli dan ilustrasi khusus (piringan kompas 8 mata angin Nusantara, jarum kiblat emas, Ka'bah Kiswah, bingkai ayat Rub el Hizb, kubah masjid, tasbih, dan kaligrafi Asmaul Husna) serta foto latar luring (`foto_masjid_banner.jpg` dan `foto_kaabah_banner.jpg`).
- **Beranda**:
  - Header identitas aplikasi dengan penanggalan Masehi Indonesia dan tombol lonceng pengingat harian.
  - Kartu **Ayat Hari Ini** berlatar foto masjid luring yang berganti otomatis setiap hari dan dapat dibuka langsung ke ayatnya di Mushaf.
  - Dua kartu status berukuran seragam (**Jadwal Berikutnya** dan **Arah Kiblat** lengkap dengan jarak kilometer ke Makkah dan mata angin).
  - Kartu **Target Tilawah Harian** dengan bilah progres dan tombol ubah target harian (5, 10, 20, 30, atau 50 ayat/hari).
  - Baris **Fitur Disarankan** dengan kartu berukuran seragam (108x110 dp) menuju Jadwal & Kiblat, Dzikir & Tasbih, 99 Asmaul Husna, Cari Ayat, Bacaan Unggulan, Quran & Sains, Penjelajah Topik, Kajian Tematik, Tersimpan, dan Riwayat Baca.
  - Baris **Bacaan Unggulan** dengan kartu seragam (224x124 dp) untuk surah dan ayat pilihan.
- **Mushaf Al-Qur'an**:
  - Tab **Daftar Surah (114)** dan **Indeks 30 Juz** untuk melompat langsung ke awal juz mana pun.
  - Chip penyaring cepat: **Semua (114)**, **Makkiyah**, dan **Madaniyah**, disertai kolom pencarian surah instan.
  - Pembaca ayat-per-ayat dan mode mushaf 15 baris dengan *pinch-to-zoom* serta pengatur ukuran huruf Arab (22 sp - 42 sp).
  - Tombol **Mode Fokus / Terjemahan** untuk menyembunyikan atau menampilkan terjemahan saat ingin fokus tilawah teks Arab.
  - Tombol **Putar Audio per Ayat** (streaming EveryAyah sesuai qari pilihan), **Catatan Kaki Terjemahan Kemenag** (dialog penjelasan), **Salin Ayat**, **Bagikan Ayat**, dan **Simpan Bookmark**.
  - Navigasi **Surah Sebelumnya** dan **Surah Berikutnya** di akhir surah.
- **Kompas Kiblat & Jadwal Sholat**:
  - Piringan kompas 8 arah mata angin Nusantara (`U`, `TL`, `T`, `TG`, `S`, `BD`, `B`, `BL`) dengan skala derajat per 5 derajat, jarum kiblat emas beranimasi halus, lencana Ka'bah pada sudut kiblat kota, indikator kesejajaran kiblat, serta informasi jarak kilometer ke Ka'bah (Makkah).
  - Jadwal waktu harian lengkap 7 waktu (**Imsak**, **Subuh**, **Terbit**, **Dzuhur**, **Ashar**, **Maghrib**, dan **Isya**) per kota dari bundel `kota.json` dan API MyQuran, dilengkapi pengingat notifikasi azan via `AlarmManager`.
- **99 Asmaul Husna**:
  - Direktori lengkap 99 Asmaul Husna luring (`asmaul_husna.json`) dengan kaligrafi Arab, transliterasi Latin, arti bahasa Indonesia, pencarian instan, dialog tadabbur makna, serta tombol salin.
- **Dzikir Pagi, Petang & Tasbih Digital**:
  - Kumpulan bacaan **Dzikir Pagi**, **Dzikir Petang**, dan **Dzikir Setelah Sholat** (`dzikir.json`) dengan penghitung ketukan per bacaan, bilah progres, dan penjelasan keutamaan/dalil.
  - **Tasbih Digital** mandiri dengan umpan balik getar (*haptic feedback*), pilihan target hitungan (33, 99, 100, 1000), dan pencatat putaran.
- **Murottal Audio**:
  - Pemutar latar belakang berbasis Media3 `MediaSessionService` dengan pilihan 4 qari internasional, pemilihan surah yang sedang diputar, dan daftar lompat cepat surah populer.
- **Kajian & Penjelajahan Ayat**:
  - **Pencarian Ayat** teks Arab/terjemahan dan pencarian suara (*SpeechRecognizer* bahasa Indonesia) dengan kata kunci populer.
  - **Bookmark** tersimpan di Room Database dengan pratinjau teks ayat dan lompat langsung ke Mushaf.
  - **Riwayat Bacaan** otomatis tercatat saat membaca mushaf.
  - **Bacaan Unggulan**, **Quran & Sains** (18 fenomena ilmiah), **Penjelajah Topik** (berdasarkan kategori & suasana hati), dan **Kajian Tematik**.
- **Pengaturan & Profil Saya**:
  - Pengaturan tema terang/gelap, ukuran huruf Arab, pengingat ayat harian (`WorkManager`), cadangan Ekspor & Impor JSON via *Storage Access Framework*, pembersihan cache, Tentang Kami, Beri Rating, Bagikan Aplikasi, dan Donasi.

Rincian status per modul dapat dilihat pada [matriks paritas fitur](PARITAS-FITUR.md).
