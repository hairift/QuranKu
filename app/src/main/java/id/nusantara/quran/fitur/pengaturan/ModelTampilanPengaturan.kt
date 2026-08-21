package id.nusantara.quran.fitur.pengaturan

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.fitur.pengingat.PekerjaAyatHarian
import id.nusantara.quran.inti.util.PengelolaCadangan
import id.nusantara.quran.inti.util.PengelolaPenyimpanan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Model tampilan pengaturan: tema, pengingat ayat harian, penyimpanan,
 * serta ekspor dan impor cadangan data pengguna.
 */
@HiltViewModel
class ModelTampilanPengaturan @Inject constructor(
    @ApplicationContext private val konteks: Context,
    private val pengaturan: PengaturanAplikasi,
    private val cadangan: PengelolaCadangan,
) : ViewModel() {

    val temaGelap = pengaturan.temaGelap.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val pengingatAyat = pengaturan.pengingatAyat.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val jamPengingat = pengaturan.jamPengingatAyat.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 6)
    val namaKota = pengaturan.namaKota.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "Kota Jakarta")

    /** Ukuran cache saat ini dalam format siap tampil. */
    private val _ukuranCache = MutableStateFlow("Menghitung…")
    val ukuranCache: StateFlow<String> = _ukuranCache.asStateFlow()

    /** Pesan status untuk hasil ekspor, impor, atau pembersihan. */
    private val _pesanStatus = MutableStateFlow<String?>(null)
    val pesanStatus: StateFlow<String?> = _pesanStatus.asStateFlow()

    private val _sedangProses = MutableStateFlow(false)
    val sedangProses: StateFlow<Boolean> = _sedangProses.asStateFlow()

    init {
        muatUlangUkuranCache()
    }

    fun aturTema(gelap: Boolean) = viewModelScope.launch { pengaturan.aturTemaGelap(gelap) }

    /** Menyalakan/mematikan pengingat ayat harian beserta pekerja latarnya. */
    fun alihkanPengingatAyat(aktif: Boolean) = viewModelScope.launch {
        pengaturan.aturPengingatAyat(aktif)
        if (aktif) {
            PekerjaAyatHarian.aktifkan(konteks, pengaturan.jamPengingatAyat.first())
        } else {
            PekerjaAyatHarian.matikan(konteks)
        }
    }

    /** Mengubah jam pengingat lalu menjadwalkan ulang bila sedang aktif. */
    fun aturJamPengingat(jam: Int) = viewModelScope.launch {
        pengaturan.aturJamPengingatAyat(jam)
        if (pengaturan.pengingatAyat.first()) {
            PekerjaAyatHarian.aktifkan(konteks, jam)
        }
    }

    /** Menghitung ulang ukuran cache di thread latar. */
    fun muatUlangUkuranCache() = viewModelScope.launch {
        _ukuranCache.value = withContext(Dispatchers.IO) {
            PengelolaPenyimpanan.formatUkuran(PengelolaPenyimpanan.ukuranCache(konteks))
        }
    }

    /** Membersihkan seluruh cache sementara aplikasi. */
    fun bersihkanPenyimpanan() = viewModelScope.launch {
        _sedangProses.value = true
        val berhasil = withContext(Dispatchers.IO) { PengelolaPenyimpanan.bersihkanCache(konteks) }
        _pesanStatus.value = if (berhasil) "Penyimpanan sementara berhasil dibersihkan."
        else "Sebagian berkas gagal dihapus. Coba lagi."
        muatUlangUkuranCache()
        _sedangProses.value = false
    }

    /** Mengekspor cadangan data pengguna ke berkas pilihan. */
    fun eksporData(uri: Uri) = viewModelScope.launch {
        _sedangProses.value = true
        val berhasil = cadangan.ekspor(uri)
        _pesanStatus.value = if (berhasil) "Cadangan berhasil diekspor."
        else "Ekspor gagal. Pastikan lokasi berkas dapat ditulis."
        _sedangProses.value = false
    }

    /** Mengimpor cadangan dari berkas yang pernah diekspor. */
    fun imporData(uri: Uri) = viewModelScope.launch {
        _sedangProses.value = true
        val berhasil = cadangan.impor(uri)
        _pesanStatus.value = if (berhasil) "Cadangan berhasil diimpor dan digabungkan."
        else "Impor gagal. Pastikan berkas adalah cadangan QuranKu yang valid."
        _sedangProses.value = false
    }

    /** Menutup pesan status setelah dibaca pengguna. */
    fun tutupPesan() {
        _pesanStatus.value = null
    }
}
