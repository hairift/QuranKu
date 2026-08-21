# 00 - Panduan Clean Room Reverse Engineering (Anti Lisensi)

## Tujuan
Kita TIDAK BOLEH menyalin kode, nama variabel, struktur file, komentar, atau aset dari https://github.com/alfaazplus/QuranApp. Yang kita lakukan adalah **Clean Room Reverse Engineering**.

## Definisi Clean Room
1.  **Tim Spek (Fase 1):** Melihat aplikasi referensi HANYA untuk mencatat FUNGSI-nya. Outputnya adalah dokumen PRD berbahasa Indonesia yang mendeskripsikan *apa* yang dilakukan aplikasi, BUKAN *bagaimana* kodenya.
2.  **Tim Implementasi (Fase 2 - Codex):** TIDAK PERNAH melihat source code asli. Hanya membaca PRD kita. Menulis ulang semua dari nol dengan arsitektur, nama variabel, dan UI milik kita sendiri 100% Bahasa Indonesia.

## Kenapa Ini Aman?
- Lisensi QuranApp adalah GPL v3 (umum untuk app Quran open source). Jika copy-paste, aplikasi kamu wajib open source GPL juga.
- Dengan clean room, kamu hanya meniru **ide dan fungsionalitas** yang tidak dilindungi hak cipta. Ekspresi (kode, UI, icon, teks) adalah buatan baru → kamu pemilik penuh, bisa pakai lisensi MIT / Proprietary.

## Bukti Kepatuhan
Simpan folder ini sebagai bukti:
- `/docs/prd/` -> spesifikasi fungsional
- `/docs/legal/` -> file ini
- Log prompt Codex