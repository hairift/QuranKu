package id.nusantara.quran.fitur.sains

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelButirSains
import id.nusantara.quran.domain.model.ModelSurah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan Quran & Sains beserta resolusi teks ayat rujukan. */
@HiltViewModel
class ModelTampilanSains @Inject constructor(
    private val sumber: SumberDataLokal,
) : ViewModel() {

    private val _daftar = MutableStateFlow<List<ModelButirSains>>(emptyList())
    val daftar: StateFlow<List<ModelButirSains>> = _daftar.asStateFlow()

    private var cacheSurah: List<ModelSurah> = emptyList()

    init {
        viewModelScope.launch {
            _daftar.value = sumber.daftarSains()
            cacheSurah = sumber.daftarSurah()
        }
    }

    /** Nama latin surah untuk label rujukan. */
    fun namaSurah(nomor: Int): String = cacheSurah.firstOrNull { it.nomor == nomor }?.namaLatin ?: "QS $nomor"

    /** Memuat teks satu ayat rujukan. */
    suspend fun ayatTunggal(surah: Int, nomor: Int): ModelAyat? = sumber.ayatTunggal(surah, nomor)
}
