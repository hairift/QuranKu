package id.nusantara.quran.fitur.sholat

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.nusantara.quran.inti.util.PembantuNotifikasi

/**
 * Penerima alarm waktu sholat. Menampilkan notifikasi azan lalu
 * meminta penjadwal memasang alarm untuk hari berikutnya.
 */
class PenerimaAzan : BroadcastReceiver() {

    override fun onReceive(konteks: Context, niat: Intent) {
        val namaWaktu = niat.getStringExtra(PenjadwalAzan.TAMBAHAN_NAMA_WAKTU) ?: return
        PembantuNotifikasi.tampilkan(
            konteks = konteks,
            kanal = PembantuNotifikasi.KANAL_AZAN,
            idNotifikasi = ID_DASAR + namaWaktu.hashCode() % 1000,
            judul = "Waktunya Sholat $namaWaktu",
            isi = "Waktu sholat $namaWaktu telah tiba. Marilah tunaikan sholat tepat waktu.",
        )
        // Pasang ulang jadwal untuk esok hari agar pengingat terus berjalan.
        PenjadwalAzan.jadwalkanUlangDariPreferensi(konteks)
    }

    companion object {
        private const val ID_DASAR = 7000
    }
}
