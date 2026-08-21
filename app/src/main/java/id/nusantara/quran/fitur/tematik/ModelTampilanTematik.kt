package id.nusantara.quran.fitur.tematik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelKategoriTematik
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan tematik: kategori, butir terpilih, dan isi ayat rujukan. */
@HiltViewModel
class ModelTampilanTematik @Inject constructor(private val sumber: SumberDataLokal) : ViewModel() {

    private val _kategori = MutableStateFlow<List<ModelKategoriTematik>>(emptyList())
    val kategori: StateFlow<List<ModelKategoriTematik>> = _kategori.asStateFlow()

    private val _kategoriAktif = MutableStateFlow<ModelKategoriTematik?>(null)
    val kategoriAktif: StateFlow<ModelKategoriTematik?> = _kategoriAktif.asStateFlow()

    private val _ayatRujukan = MutableStateFlow<List<ModelAyat>>(emptyList())
    val ayatRujukan: StateFlow<List<ModelAyat>> = _ayatRujukan.asStateFlow()

    private val _judulButir = MutableStateFlow("")
    val judulButir: StateFlow<String> = _judulButir.asStateFlow()

    init {
        viewModelScope.launch { _kategori.value = sumber.daftarTematik() }
    }

    /** Membuka satu kategori tematik. */
    fun bukaKategori(kategori: ModelKategoriTematik) {
        _kategoriAktif.value = kategori
    }

    /** Menutup kategori dan kembali ke daftar. */
    fun tutupKategori() {
        _kategoriAktif.value = null
        _ayatRujukan.value = emptyList()
    }

    /** Memuat ayat-ayat rujukan satu butir tematik. */
    fun bukaButir(judul: String, rujukan: List<Pair<Int, Int>>) {
        viewModelScope.launch {
            _judulButir.value = judul
            _ayatRujukan.value = rujukan.mapNotNull { (surah, ayat) ->
                sumber.ayatTunggal(surah, ayat)
            }
        }
    }

    /** Menutup tampilan ayat rujukan. */
    fun tutupButir() {
        _ayatRujukan.value = emptyList()
        _judulButir.value = ""
    }
}
