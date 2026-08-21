# Matriks Paritas Fitur QuranKu

Dokumen ini membedakan fitur yang sudah berfungsi dengan fitur yang masih memerlukan implementasi data/layanan tambahan. Status tidak boleh dianggap selesai hanya karena folder atau layar sudah ada.

| Kelompok | Status QuranKu | Catatan |
|---|---|---|
| Mushaf ayat-per-ayat | Aktif | Membaca 6.236 ayat lokal, Arab, terjemahan, zoom, bookmark sesi |
| Terjemahan Indonesia | Aktif | Bundel QuranEnc 114 surah |
| Pencarian Arab/terjemahan | Aktif | Pencarian lokal, maksimal 30 hasil tampilan |
| Jadwal azan | Aktif | API MyQuran per kota (bundel kota.json), alarm notifikasi azan via AlarmManager, dipasang ulang otomatis |
| Kompas kiblat | Aktif | Sensor rotasi, derajat, jarum visual, arah mata angin |
| Navigasi Juz/Hizb/Halaman | Aktif dasar | Dialog lompat cepat ke ayat/juz di mushaf |
| Mode mushaf 15 baris | UI dasar | Layout halaman tetap dan font mushaf khusus belum lengkap |
| Audio qari | Aktif | Servis latar Media3 MediaSession dengan notifikasi media, putar/jeda/lanjut; cache offline dan repeat rentang belum lengkap |
| Bookmark | Aktif dasar | Penyimpanan Room tersedia; folder, catatan, ekspor-impor belum lengkap |
| Tafsir | Fondasi | Entitas/API tersedia; katalog dan pembaca belum lengkap |
| Tematik | Aktif dasar | Kategori dan konten tematik lokal dibundel (tematik.json) |
| Multi-bahasa | Dasar | Indonesia, Inggris, Arab; 22 bahasa belum lengkap |
| Pengingat ayat | Aktif | Worker WorkManager harian dengan notifikasi ayat pada jam pilihan pengguna |
| Lisensi dan atribusi | Aktif | Dokumentasi sumber QuranEnc/Tanzil tersedia |

## Syarat rilis produksi

Sebelum rilis publik, selesaikan seluruh baris berstatus `Fondasi` atau `UI dasar`, tambahkan pengujian perangkat, verifikasi lisensi setiap konten, dan ganti keystore QA dengan keystore produksi.
