# Sumber Data dan Lisensi

## Kode aplikasi

Kode QuranKu ditulis secara mandiri menggunakan paket `id.nusantara.quran` dengan sistem desain **Mushaf Nusantara**, aset vektor kustom, dan penamaan berbahasa Indonesia. Tidak ada kode sumber atau aset privat proyek pihak ketiga yang disalin.

## Sumber konten dan aset

- **Tanzil**: teks Al-Qur'an bahasa Arab mengikuti syarat atribusi dan ketentuan distribusi Tanzil.
- **QuranEnc & Kemenag RI**: terjemahan bahasa Indonesia serta catatan kaki merujuk pada terjemahan Kementerian Agama RI yang didistribusikan melalui bundel lokal untuk penggunaan luring.
- **EveryAyah / QuranicAudio**: pemutaran audio murottal surah dan ayat mengikuti ketentuan penggunaan layanan terkait.
- **MyQuran API**: jadwal sholat dan imsakiyah per kota diambil dari endpoint publik MyQuran dengan daftar kota luring (`kota.json`).
- **Aset Visual Luring**: seluruh ikon aplikasi menggunakan VectorDrawable Android asli di `app/src/main/res/drawable/` dan foto ilustrasi masjid serta Ka'bah disimpan secara lokal di `app/src/main/res/drawable-nodpi/` agar tetap tampil saat perangkat tidak terhubung ke internet.

Jangan memasukkan kunci API rahasia, kredensial pribadi, atau data berhak cipta tertutup ke dalam repositori.
