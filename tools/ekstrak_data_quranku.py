# Skrip ekstraksi data referensi (QuranApp, GPL v3) menjadi berkas JSON ringkas milik QuranKu.
# QuranKu juga berlisensi GPL v3 sehingga penggunaan ulang data ini sah secara lisensi.
# Keluaran:
#   1. app/src/main/assets/data-lokal/surah_info.json  -> metadata 114 surah
#   2. app/src/main/assets/data-lokal/quran_lokal.json -> ditambah kolom "juz" per ayat
#   3. app/src/main/assets/data-lokal/tematik.json     -> konten tematik berbahasa Indonesia
import json
import sqlite3
from pathlib import Path

AKAR = Path(__file__).resolve().parent.parent
DB_REFERENSI = AKAR / "QuranKu" / "QuranKu-Master" / "app" / "src" / "main" / "assets" / "db"
BERKAS_REFERENSI = AKAR / "QuranKu" / "QuranKu-Master" / "app" / "src" / "main" / "assets" / "verses"
KELUARAN = AKAR / "app" / "src" / "main" / "assets" / "data-lokal"


def ekstrak_surah():
    """Ambil metadata 114 surah: nomor, nama Arab, nama Indonesia, arti, jumlah ayat, tipe wahyu."""
    koneksi = sqlite3.connect(DB_REFERENSI / "quranapp.db")
    kursor = koneksi.cursor()
    nama_arab = {r[0]: r[1] for r in kursor.execute(
        "SELECT surah_no, name FROM surah_localizations WHERE lang_code='ar'")}
    nama_id = {r[0]: (r[1], r[2]) for r in kursor.execute(
        "SELECT surah_no, name, meaning FROM surah_localizations WHERE lang_code='id'")}
    nama_en = {r[0]: r[1] for r in kursor.execute(
        "SELECT surah_no, name FROM surah_localizations WHERE lang_code='en'")}
    hasil = []
    for nomor, jumlah, _urutan, _ruku, tipe in kursor.execute(
            "SELECT surah_no, ayah_count, revelation_order, rukus_count, revelation_type FROM surahs ORDER BY surah_no"):
        latin, arti = nama_id.get(nomor, (nama_en.get(nomor, ""), ""))
        hasil.append({
            "nomor": nomor,
            "namaArab": nama_arab.get(nomor, ""),
            "namaLatin": latin or nama_en.get(nomor, ""),
            "arti": arti or "",
            "jumlahAyat": jumlah,
            "tipe": "Makkiyah" if tipe == "meccan" else "Madaniyah",
        })
    koneksi.close()
    assert len(hasil) == 114, f"Jumlah surah tidak valid: {len(hasil)}"
    (KELUARAN / "surah_info.json").write_text(
        json.dumps(hasil, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print(f"surah_info.json: {len(hasil)} surah")


def tanam_juz():
    """Tambahkan nomor juz ke setiap ayat di quran_lokal.json agar navigasi juz berfungsi."""
    koneksi = sqlite3.connect(DB_REFERENSI / "quranapp.db")
    peta = {(s, a): j for s, a, j in koneksi.execute("SELECT surah_no, ayah_no, juz_no FROM ayahs")}
    koneksi.close()
    berkas = KELUARAN / "quran_lokal.json"
    data = json.loads(berkas.read_text(encoding="utf-8"))
    for ayat in data["ayat"]:
        ayat["juz"] = peta.get((ayat["sura"] if isinstance(ayat["sura"], int) else int(ayat["sura"]),
                                ayat["aya"] if isinstance(ayat["aya"], int) else int(ayat["aya"])), 1)
    berkas.write_text(json.dumps(data, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    print(f"quran_lokal.json: {len(data['ayat'])} ayat ditanami nomor juz")


# Pemetaan slug nabi dari basis data referensi ke nama Indonesia.
NAMA_NABI = {
    "adam": "Adam", "nuh": "Nuh", "ibrahim": "Ibrahim", "ishmael": "Ismail",
    "isaac": "Ishak", "yaqub": "Ya'qub", "yusuf": "Yusuf", "ayyub": "Ayub",
    "yunus": "Yunus", "harun": "Harun", "david": "Daud", "solomon": "Sulaiman",
    "zechariah": "Zakaria", "yahya": "Yahya", "jesus": "Isa", "lut": "Luth",
    "idris": "Idris", "elijah": "Ilyas", "elisha": "Ilyasa", "muhammad": "Muhammad",
}


def _referensi_ayat(teks):
    """Pecah daftar referensi '2:201,3:8-9' menjadi daftar pasangan surah/ayat individual."""
    hasil = []
    for bagian in teks.split(","):
        bagian = bagian.strip()
        if ":" not in bagian:
            continue
        surah, rentang = bagian.split(":", 1)
        if "-" in rentang:
            awal, akhir = rentang.split("-", 1)
            for nomor in range(int(awal), int(akhir) + 1):
                hasil.append([int(surah), nomor])
        else:
            hasil.append([int(surah), int(rentang)])
    return hasil


def ekstrak_tematik():
    """Bangun tematik.json berbahasa Indonesia dari konten referensi."""
    kategori = []

    # Doa dari Al-Quran (type1), Solusi (type0), Adab (type2), Dosa besar (major_sins).
    sumber = [
        ("doa", "Doa dari Al-Quran", "Kumpulan doa yang diajarkan langsung oleh Al-Quran.", "type1"),
        ("solusi", "Solusi dari Al-Quran", "Ayat penenang untuk berbagai keadaan hati.", "type0"),
        ("adab", "Adab dalam Al-Quran", "Tuntunan akhlak dan perilaku keseharian.", "type2"),
        ("dosa", "Dosa-Dosa Besar", "Peringatan Al-Quran atas dosa-dosa besar.", "major_sins"),
    ]
    for id_kategori, judul, deskripsi, folder in sumber:
        peta = json.loads((BERKAS_REFERENSI / folder / "map.json").read_text(encoding="utf-8"))
        nama_berkas = "major_sins.json" if folder == "major_sins" else f"{folder}.json"
        judul_mentah = json.loads((BERKAS_REFERENSI / folder / "id" / nama_berkas).read_text(encoding="utf-8"))
        item = []
        for kunci, teks_judul in judul_mentah.items():
            if isinstance(teks_judul, dict):
                teks_judul = teks_judul.get("title", "")
            referensi = peta.get(kunci, "")
            if referensi.startswith("prayers_"):
                referensi = peta.get("2", "")  # doa para nabi memakai daftar gabungan
            ayat = _referensi_ayat(referensi)
            if ayat:
                item.append({"judul": teks_judul, "ayat": ayat})
        kategori.append({"id": id_kategori, "judul": judul, "deskripsi": deskripsi, "item": item})

    # Para nabi yang disebut dalam Al-Quran dari topics.db.
    koneksi = sqlite3.connect(DB_REFERENSI / "topics.db")
    kursor = koneksi.cursor()
    nabi = []
    daftar_nabi = list(kursor.execute(
        "SELECT id, slug FROM topics WHERE type='prophet' ORDER BY id"))
    for id_topik, slug in daftar_nabi:
        if slug not in NAMA_NABI:
            continue
        ayat_mentah = [r[0] for r in kursor.execute(
            "SELECT ayah_id FROM topic_ayahs WHERE topic_id=? LIMIT 6", (id_topik,))]
        ayat = [[a // 1000, a % 1000] for a in ayat_mentah]
        if ayat:
            nabi.append({"judul": f"Nabi {NAMA_NABI[slug]}", "ayat": ayat})
    koneksi.close()
    kategori.append({
        "id": "nabi", "judul": "Para Nabi dalam Al-Quran",
        "deskripsi": "Kisah dan sebutan para nabi di dalam ayat suci.", "item": nabi,
    })

    (KELUARAN / "tematik.json").write_text(
        json.dumps({"kategori": kategori}, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")
    for k in kategori:
        print(f"tematik {k['id']}: {len(k['item'])} item")


if __name__ == "__main__":
    ekstrak_surah()
    tanam_juz()
    ekstrak_tematik()
    print("Ekstraksi selesai.")
