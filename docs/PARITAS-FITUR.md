# Matriks Paritas Fitur QuranKu

Dokumen ini membedakan fitur yang sudah berfungsi dengan fitur yang masih memerlukan implementasi data/layanan tambahan. Status tidak boleh dianggap selesai hanya karena folder atau layar sudah ada.

| Kelompok | Status QuranKu | Catatan |
|---|---|---|
| Mushaf ayat-per-ayat | Aktif | Membaca 6.236 ayat lokal, Arab, terjemahan, zoom, bookmark sesi |
| Terjemahan Indonesia | Aktif | Bundel QuranEnc 114 surah |
| Pencarian Arab/terjemahan | Aktif | Pencarian lokal, maksimal 30 hasil tampilan |
| Jadwal azan | Aktif dasar | Jadwal Jakarta statis; API kota dan notifikasi adzan belum selesai |
| Kompas kiblat | Aktif | Sensor rotasi, derajat, jarum visual, arah mata angin |
| Navigasi Juz/Hizb/Halaman | Fondasi | Model dan dialog lompat belum lengkap |
| Mode mushaf 15 baris | UI dasar | Layout halaman tetap dan font mushaf khusus belum lengkap |
| Audio qari | UI dasar | Media3 tersedia; service background, cache, repeat range belum lengkap |
| Bookmark | UI dasar | Penyimpanan Room tersedia; folder, catatan, ekspor-impor belum lengkap |
| Tafsir | Fondasi | Entitas/API tersedia; katalog dan pembaca belum lengkap |
| Tematik | UI dasar | Kategori tersedia; konten offline lengkap belum dibundel |
| Multi-bahasa | Dasar | Indonesia, Inggris, Arab; 22 bahasa belum lengkap |
| Pengingat ayat | Fondasi | Use case tersedia; Worker/notifikasi belum lengkap |
| Lisensi dan atribusi | Aktif | Dokumentasi sumber QuranEnc/Tanzil tersedia |

## Syarat rilis produksi

Sebelum rilis publik, selesaikan seluruh baris berstatus `Fondasi` atau `UI dasar`, tambahkan pengujian perangkat, verifikasi lisensi setiap konten, dan ganti keystore QA dengan keystore produksi.
