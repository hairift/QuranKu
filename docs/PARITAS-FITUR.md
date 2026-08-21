# Matriks Paritas Fitur QuranKu

Dokumen ini membedakan fitur yang sudah berfungsi dengan fitur yang masih memerlukan implementasi data/layanan tambahan. Status tidak boleh dianggap selesai hanya karena folder atau layar sudah ada.

| Kelompok | Status QuranKu | Catatan |
|---|---|---|
| Mushaf ayat-per-ayat | Aktif | Membaca 6.236 ayat lokal, Arab, terjemahan, zoom, bookmark sesi |
| Terjemahan Indonesia | Aktif | Bundel QuranEnc 114 surah |
| Pencarian Arab/terjemahan | Aktif | Pencarian lokal, maksimal 30 hasil tampilan |
| Pencarian suara (voice note) | Aktif | SpeechRecognizer id-ID di layar pencarian, izin RECORD_AUDIO runtime |
| Ayat hari ini | Aktif | Berganti harian secara deterministik, bisa dibuka langsung di mushaf |
| Fitur disarankan | Aktif | Baris pintasan geser di beranda ke seluruh fitur utama |
| Riwayat bacaan | Aktif | Tercatat otomatis dari mushaf (Room), pratinjau beranda, hapus per-butir/semua |
| Bacaan unggulan | Aktif | 12 bacaan anjuran waktu terbaik dari bundel unggulan.json |
| Quran & Sains | Aktif | 18 fenomena ilmiah + ayat rujukan dari bundel sains.json |
| Penjelajah topik | Aktif | 8 kategori + 8 suasana hati dari bundel topik.json, tiga tingkat navigasi |
| Jadwal azan | Aktif | API MyQuran per kota (bundel kota.json), alarm notifikasi azan via AlarmManager, dipasang ulang otomatis |
| Kompas kiblat | Aktif | Sensor rotasi, derajat, jarum visual, arah mata angin |
| Navigasi Juz/Hizb/Halaman | Aktif dasar | Dialog lompat cepat ke ayat/juz di mushaf |
| Mode mushaf 15 baris | UI dasar | Layout halaman tetap dan font mushaf khusus belum lengkap |
| Audio qari | Aktif | Servis latar Media3 MediaSession dengan notifikasi media, putar/jeda/lanjut; cache offline dan repeat rentang belum lengkap |
| Bookmark | Aktif | Penyimpanan Room, catatan, ikut ekspor-impor; folder belum lengkap |
| Pembersihan penyimpanan | Aktif | Hitung dan bersihkan cache dari layar Saya |
| Ekspor & impor | Aktif | Cadangan JSON (pengaturan + bookmark + riwayat) via SAF |
| Tentang Kami | Aktif | Profil pengembang, sumber data, lisensi, tautan donasi |
| Beri rating & berbagi | Aktif | Intent Google Play dan lembar berbagi Android |
| Donasi | Aktif | Tautan Trakteer di layar Saya dan Tentang Kami |
| Tafsir | Fondasi | Entitas/API tersedia; katalog dan pembaca belum lengkap |
| Tematik | Aktif dasar | Kategori dan konten tematik lokal dibundel (tematik.json) |
| Multi-bahasa | Dasar | Indonesia, Inggris, Arab; 22 bahasa belum lengkap |
| Pengingat ayat | Aktif | Worker WorkManager harian dengan notifikasi ayat pada jam pilihan pengguna |
| Lisensi dan atribusi | Aktif | Dokumentasi sumber QuranEnc/Tanzil tersedia |

## Syarat rilis produksi

Sebelum rilis publik, selesaikan seluruh baris berstatus `Fondasi` atau `UI dasar`, tambahkan pengujian perangkat, verifikasi lisensi setiap konten, dan ganti keystore QA dengan keystore produksi.
