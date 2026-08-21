"""Pengunduh data legal Tanzil dan QuranEnc untuk pengisian aset lokal.

Skrip ini sengaja hanya menyimpan hasil unduhan ke folder data-lokal dan tidak
memuat kode aplikasi lain. Jalankan dengan Python 3 pada mesin pengembang.
"""
import json
from pathlib import Path
from urllib.request import Request, urlopen

SUMBER = []

def unduh_semua():
    tujuan = Path("app/src/main/assets/data-lokal")
    tujuan.mkdir(parents=True, exist_ok=True)
    for nama, alamat in SUMBER:
        print(f"Mengunduh {nama} dari sumber resmi...")
        permintaan = Request(alamat, headers={"User-Agent": "QuranKu-DataDownloader/1.0"})
        try:
            with urlopen(permintaan, timeout=30) as tanggapan:
                (tujuan / nama).write_bytes(tanggapan.read())
        except Exception as kesalahan:
            print(f"Peringatan: {nama} belum berhasil diunduh: {kesalahan}")
    for nomor in range(1, 115):
        nama = f"quranenc_id_{nomor:03d}.json"
        alamat = f"https://quranenc.com/api/v1/translation/sura/indonesian_affairs/{nomor}"
        print(f"Mengunduh terjemahan surah {nomor}/114...")
        permintaan = Request(alamat, headers={"User-Agent": "QuranKu-DataDownloader/1.0"})
        try:
            with urlopen(permintaan, timeout=30) as tanggapan:
                (tujuan / nama).write_bytes(tanggapan.read())
        except Exception as kesalahan:
            print(f"Peringatan: {nama} belum berhasil diunduh: {kesalahan}")
    semua_ayat = []
    for nomor in range(1, 115):
        berkas = tujuan / f"quranenc_id_{nomor:03d}.json"
        if not berkas.exists():
            continue
        isi = json.loads(berkas.read_text(encoding="utf-8"))
        semua_ayat.extend(isi.get("result", []))
    (tujuan / "quran_lokal.json").write_text(json.dumps({"sumber": "QuranEnc", "ayat": semua_ayat}, ensure_ascii=False), encoding="utf-8")
    print(f"Data lokal selesai: {len(semua_ayat)} ayat.")

if __name__ == "__main__":
    unduh_semua()
