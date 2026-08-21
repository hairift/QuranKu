package id.nusantara.quran.fitur.nusantara

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.data.repositori.RepositoriSholat
import id.nusantara.quran.domain.model.ModelKota
import id.nusantara.quran.domain.model.ModelWaktuSholat
import id.nusantara.quran.fitur.sholat.PenjadwalAzan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Model tampilan Ruang Nusantara: jadwal sholat per kota,
 * koordinat kiblat, dan saklar pengingat azan.
 */
@HiltViewModel
class ModelTampilanNusantara @Inject constructor(
    @ApplicationContext private val konteks: Context,
    private val sumber: SumberDataLokal,
    private val repositoriSholat: RepositoriSholat,
    private val pengaturan: PengaturanAplikasi,
) : ViewModel() {

    private val _jadwal = MutableStateFlow(ModelWaktuSholat())
    val jadwal: StateFlow<ModelWaktuSholat> = _jadwal.asStateFlow()

    private val _semuaKota = MutableStateFlow<List<ModelKota>>(emptyList())
    val semuaKota: StateFlow<List<ModelKota>> = _semuaKota.asStateFlow()

    /** Kota terpilih: pasangan id dan nama. */
    val kotaTerpilih: StateFlow<Pair<String, String>> = combine(
        pengaturan.idKota, pengaturan.namaKota,
    ) { id, nama -> id to nama }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "1301" to "Kota Jakarta")

    /** Koordinat untuk perhitungan arah kiblat. */
    val koordinat: StateFlow<Pair<Double, Double>> = combine(
        pengaturan.latitud, pengaturan.longitud,
    ) { lat, lng -> lat to lng }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), -6.2 to 106.8)

    val pengingatAzan = pengaturan.pengingatAzan.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), false,
    )

    init {
        viewModelScope.launch {
            _semuaKota.value = sumber.daftarKota()
            muatJadwal()
        }
    }

    /** Memuat ulang jadwal sholat sesuai kota terpilih. */
    fun muatJadwal() {
        viewModelScope.launch {
            val (idKota, _) = kotaTerpilih.first()
            _jadwal.value = repositoriSholat.jadwalHariIni(idKota)
        }
    }

    /** Mengganti kota lalu memuat ulang jadwal dan alarm azan. */
    fun pilihKota(kota: ModelKota) {
        viewModelScope.launch {
            pengaturan.aturKota(kota.id, kota.nama)
            muatJadwal()
            if (pengaturan.pengingatAzan.first()) {
                PenjadwalAzan.jadwalkan(konteks, repositoriSholat.jadwalHariIni(kota.id))
            }
        }
    }

    /** Menyalakan atau mematikan pengingat azan. */
    fun alihkanPengingatAzan(aktif: Boolean) {
        viewModelScope.launch {
            pengaturan.aturPengingatAzan(aktif)
            if (aktif) {
                val (idKota, _) = kotaTerpilih.first()
                PenjadwalAzan.jadwalkan(konteks, repositoriSholat.jadwalHariIni(idKota))
            } else {
                PenjadwalAzan.batalkan(konteks)
            }
        }
    }

    /** Mencari kota berdasarkan potongan nama. */
    fun cariKota(kata: String): List<ModelKota> {
        val daftar = _semuaKota.value
        if (kata.isBlank()) return daftar
        val kunci = kata.trim().lowercase()
        return daftar.filter { it.nama.lowercase().contains(kunci) }
    }

    /**
     * Memakai lokasi perangkat sebagai titik hitung kiblat.
     * Dipanggil hanya setelah pengguna memberi izin lokasi.
     */
    @SuppressLint("MissingPermission")
    fun pakaiLokasiPerangkat() {
        val pengelola = konteks.getSystemService(LocationManager::class.java) ?: return
        val penyedia = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        for (nama in penyedia) {
            val lokasi = runCatching { pengelola.getLastKnownLocation(nama) }.getOrNull()
            if (lokasi != null) {
                viewModelScope.launch { pengaturan.aturKoordinat(lokasi.latitude, lokasi.longitude) }
                return
            }
        }
    }
}
