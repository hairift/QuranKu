"""Pengunduh data legal Tanzil dan QuranEnc untuk pengisian aset lokal.

Skrip ini sengaja hanya menyimpan hasil unduhan ke folder data-lokal dan tidak
memuat kode aplikasi lain. Jalankan dengan Python 3 pada mesin pengembang.
"""
from pathlib import Path
from urllib.request import urlopen

SUMBER = {
    "tanzil_arab.txt": "https://tanzil.net/pub/download/index.php?Name=quran-uthmani&Format=txt",
    "quranenc_id.json": "https://quranenc.com/api/v1/translation/sura/indonesian_kemenag/1",
}

def unduh_semua():
    tujuan = Path("app/src/main/assets/data-lokal")
    tujuan.mkdir(parents=True, exist_ok=True)
    for nama, alamat in SUMBER.items():
        print(f"Mengunduh {nama} dari sumber resmi...")
        with urlopen(alamat, timeout=30) as tanggapan:
            (tujuan / nama).write_bytes(tanggapan.read())

if __name__ == "__main__":
    unduh_semua()
