# 02 - Aturan Wajib dan Larangan Pengembangan

## Wajib Dilakukan

### 1. Penamaan dan Bahasa
- Seluruh variabel, fungsi, kelas, berkas, komentar kode, dokumentasi, pesan commit, dan catatan rilis wajib menggunakan **Bahasa Indonesia** yang baku dan bersih.
- Seluruh berkas `.kt` wajib memiliki komentar dokumentasi berbahasa Indonesia.
- Dokumentasi pada `README.md` dan `docs/` wajib ditulis dalam Bahasa Indonesia tanpa emoji.

### 2. Arsitektur Modern
- Menggunakan arsitektur Android modern: MVVM + Clean Architecture (Domain, Data, dan Fitur/Presentasi).
- Teknologi utama: Kotlin, Jetpack Compose, Material 3, Room, DataStore, Media3 ExoPlayer, WorkManager, Retrofit, dan Hilt.
- Struktur paket mandiri di bawah `id.nusantara.quran`.

### 3. Desain Antarmuka Mushaf Nusantara
- Navigasi utama dengan 4 tab bawah: Beranda, Mushaf, Audio, dan Saya, dengan pembeda visual tegas antara tab aktif dan tidak aktif.
- Palet warna Mushaf Nusantara: Kertas krem gading hangat (`#FAF6EE`), Hijau Zamrud (`#0B5D46`), dan Emas Antik (`#B8892E`).
- Seluruh ikon wajib berupa aset vektor (`VectorDrawable`) atau ilustrasi gambar lokal, bukan karakter emoji.
- Seluruh kartu pada baris sejajar maupun daftar wajib memiliki ukuran dan proporsi yang seragam.

### 4. Dukungan Bahasa dan Lisensi
- Mendukung sumber daya string multibahasa (`values`, `values-en`, dan `values-ar`).
- Mencantumkan atribusi sumber data terbuka: Tanzil, QuranEnc, Kementerian Agama RI, EveryAyah, dan API MyQuran.

## Larangan

- Dilarang menyalin kode sumber, struktur paket, atau aset antarmuka privat dari proyek pihak ketiga.
- Dilarang menggunakan emoji sebagai ikon di dalam antarmuka aplikasi maupun dokumentasi.
- Dilarang menyimpan kunci API rahasia di dalam repositori.
- Dilarang menambahkan iklan atau pelacak analitik pihak ketiga.
