package id.nusantara.quran.fitur.topik

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelKategoriTopik
import id.nusantara.quran.domain.model.ModelSurah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan penjelajah topik: kategori umum dan suasana hati. */
@HiltViewModel
class ModelTampilanTopik @Inject constructor(
    private val sumber: SumberDataLokal,
) : ViewModel() {

    private val _kategori = MutableStateFlow<List<ModelKategoriTopik>>(emptyList())
    val kategori: StateFlow<List<ModelKategoriTopik>> = _kategori.asStateFlow()

    private val _suasana = MutableStateFlow<List<ModelKategoriTopik>>(emptyList())
    val suasana: StateFlow<List<ModelKategoriTopik>> = _suasana.asStateFlow()

    private var cacheSurah: List<ModelSurah> = emptyList()

    init {
        viewModelScope.launch {
            _kategori.value = sumber.daftarTopikKategori()
            _suasana.value = sumber.daftarTopikSuasana()
            cacheSurah = sumber.daftarSurah()
        }
    }

    /** Nama latin surah untuk label rujukan. */
    fun namaSurah(nomor: Int): String = cacheSurah.firstOrNull { it.nomor == nomor }?.namaLatin ?: "QS $nomor"

    /** Memuat teks satu ayat rujukan. */
    suspend fun ayatTunggal(surah: Int, nomor: Int): ModelAyat? = sumber.ayatTunggal(surah, nomor)
}
