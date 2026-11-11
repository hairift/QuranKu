# Matriks Paritas Fitur QuranKu

Dokumen ini mencatat status fitur yang sudah aktif dan teruji pada aplikasi QuranKu versi **1.3.0**.

| Kelompok Fitur | Status QuranKu | Catatan Implementasi |
|---|---|---|
| Mushaf ayat-per-ayat | Aktif | Membaca 6.236 ayat lokal, Arab, terjemahan, ukuran font, mode fokus Arab, salin & bagikan |
| Indeks 30 Juz & Filter Wahyu | Aktif | Tab Daftar Surah & 30 Juz, filter Semua/Makkiyah/Madaniyah, navigasi surah sebelum/berikutnya |
| Catatan Kaki Terjemahan | Aktif | Dialog rincian catatan kaki terjemahan Kemenag per ayat di layar Mushaf |
| Audio per Ayat & Murottal Surah | Aktif | Servis latar Media3 `MediaSessionService`, pemutaran per surah dan per ayat dari Mushaf |
| Terjemahan Indonesia | Aktif | Bundel lokal QuranEnc/Kemenag 114 surah (6.236 ayat) |
| Pencarian Arab/Terjemahan | Aktif | Pencarian lokal instan + chip kata kunci populer |
| Pencarian Suara | Aktif | `SpeechRecognizer` Bahasa Indonesia (`id-ID`) dengan izin `RECORD_AUDIO` runtime |
| Ayat Hari Ini | Aktif | Kartu berlatar foto masjid luring, berganti harian secara deterministik, buka langsung di Mushaf |
| Target Tilawah Harian | Aktif | Pelacak progres bacaan harian di Beranda dengan pilihan target 5, 10, 20, 30, atau 50 ayat |
| 99 Asmaul Husna | Aktif | Bundel `asmaul_husna.json` lengkap 99 nama Allah, pencarian, dialog tadabbur, dan salin |
| Dzikir & Tasbih Digital | Aktif | Bundel `dzikir.json` (Dzikir Pagi, Petang, Setelah Sholat) + Tasbih Digital dengan getar haptik |
| Fitur Disarankan | Aktif | Baris pintasan geser dengan ukuran kartu seragam ke 10 fitur utama |
| Riwayat Bacaan | Aktif | Tercatat otomatis dari Mushaf (Room), pratinjau Beranda, hapus per butir atau bersihkan semua |
| Bacaan Unggulan | Aktif | 12 bacaan anjuran waktu terbaik dari bundel `unggulan.json` |
| Quran & Sains | Aktif | 18 fenomena ilmiah beserta ayat rujukan dari bundel `sains.json` |
| Penjelajah Topik | Aktif | 8 kategori + 8 suasana hati dari bundel `topik.json`, tiga tingkat navigasi |
| Kajian Tematik | Aktif | Kategori dan konten kajian tematik lokal (`tematik.json`) |
| Jadwal Sholat & Imsakiyah | Aktif | 7 waktu (Imsak, Subuh, Terbit, Dzuhur, Ashar, Maghrib, Isya) via API MyQuran & `kota.json`, alarm azan |
| Kompas Kiblat Nusantara | Aktif | Sensor rotasi, piringan 8 arah mata angin Nusantara, jarum kiblat emas, lencana Ka'bah, jarak km ke Makkah |
| Bookmark | Aktif | Penyimpanan Room Database, pratinjau ayat, lompat ke Mushaf, ekspor-impor JSON |
| Pembersihan Penyimpanan | Aktif | Hitung dan bersihkan cache dari layar Saya |
| Ekspor & Impor Data | Aktif | Cadangan JSON (pengaturan + bookmark + riwayat) via Storage Access Framework |
| Tentang Kami, Rating & Donasi | Aktif | Profil pengembang, sumber data, lisensi, berbagi aplikasi, dan tautan dukungan |
| Pengingat Ayat Harian | Aktif | Worker `WorkManager` harian dengan notifikasi ayat pada jam pilihan pengguna |
| Multi-bahasa Sumber Daya | Aktif | Dukungan string dasar Indonesia (`values`), Inggris (`values-en`), dan Arab (`values-ar`) |
