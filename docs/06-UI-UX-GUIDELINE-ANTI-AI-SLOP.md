# 06 - UI/UX Guideline - Modern Nusantara Anti AI Slop

## Prinsip: Jangan Terlihat Seperti Template AI

### DILARANG (Ciri AI Slop):
- Background gradient ungu-biru
- Semua sudut 16dp Card yang sama
- Icon emoji
- Ilustrasi 3D generic
- Teks Lorem Ipsum
- Tidak ada empty state

### WAJIB (Modern 2026):

#### 1. Warna & Tema
- Light: background #FFFBF0 (krem kertas), surface #FFFFFF, primary #0E5E4D (hijau zamrud dalam), secondary #C8A951 (emas)
- Dark: background #12140F, surface #1E201A, primary #7ED0BF
- Dynamic Color: ambil dari wallpaper user jika Android 12+ tapi tetap tint hijau
- Jangan pakai Material default purple!

#### 2. Tipografi
- Judul Arab: font Amiri / KFGQPC Uthmanic Script (ukuran 28sp)
- Judul Latin: Plus Jakarta Sans Bold
- Body: Inter / Outfit
- Semua teks Arab rata kanan, terjemahan rata kiri

#### 3. Icon Real
- Pakai: `androidx.compose.material.icons:material-icons-extended`
- Contoh: 
  - Mushaf -> Icons.Rounded.MenuBook (bukan 📖)
  - Audio -> Icons.Rounded.GraphicEq
  - Pencarian -> Icons.Rounded.Search
  - Bookmark -> Icons.Rounded.Bookmark
  - Kiblat -> Icons.Rounded.Explore
  - Jadwal Sholat -> Icons.Rounded.Schedule
- Ukuran icon 24dp, stroke 2px

#### 4. Layout Signature (Beda dari QuranApp)
- Beranda: 
  - Header melengkung bawah (curved bottom) dengan jam + tanggal Hijriah
  - Kartu Lanjutkan Membaca dengan progress bar
  - Grid 2 kolom untuk menu Tematik (Doa, Sains, dll) dengan icon besar
- Mushaf:
  - Ayat-per-ayat: tiap ayat dalam kartu dengan nomor ayat di dalam lingkaran emas, bukan kotak
  - Mode Mushaf: halaman dengan garis bismillah tengah, nomor halaman di bawah dengan ornamen islami (bukan angka biasa)
- Player Audio: Bottom sheet yang bisa di-expand jadi full screen dengan visualizer

#### 5. Animasi Halus
- Transisi antar layar pakai `AnimatedContent` + slide
- Saat ganti ayat, pakai crossfade 150ms
- Bookmark animasi scale

#### 6. Aksesibilitas
- Support TalkBack untuk ayat
- Ukuran teks Arab bisa sampai 40sp untuk lansia

## Referensi Inspirasi (Bukan untuk Ditiru Persis):
- Dribbble: "Quran App Minimal"
- App: Al-Quran Indonesia Kemenag (untuk struktur data, bukan UI)

## Deliverable UI:
- Figma wireframe tidak wajib, tapi screenshot 5 layar utama wajib ada di README
- Semua drawable vector buatan sendiri di `res/drawable/`