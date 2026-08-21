package id.nusantara.quran.fitur.pencarian

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelAyat
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan pencarian ayat dengan jeda ketik dan batas hasil. */
@OptIn(FlowPreview::class)
@HiltViewModel
class ModelTampilanPencarian @Inject constructor(private val sumber: SumberDataLokal) : ViewModel() {

    private val _kataKunci = MutableStateFlow("")
    val kataKunci: StateFlow<String> = _kataKunci.asStateFlow()

    private val _hasil = MutableStateFlow<List<ModelAyat>>(emptyList())
    val hasil: StateFlow<List<ModelAyat>> = _hasil.asStateFlow()

    private val _sedangMencari = MutableStateFlow(false)
    val sedangMencari: StateFlow<Boolean> = _sedangMencari.asStateFlow()

    init {
        viewModelScope.launch {
            _kataKunci.debounce(350).collect { kata ->
                val kunci = kata.trim()
                if (kunci.length < 3) {
                    _hasil.value = emptyList()
                    return@collect
                }
                _sedangMencari.value = true
                val semua = sumber.semuaAyat()
                val kunciKecil = kunci.lowercase()
                _hasil.value = semua.asSequence()
                    .filter {
                        it.terjemahan.lowercase().contains(kunciKecil) || it.arab.contains(kunci)
                    }
                    .take(50)
                    .toList()
                _sedangMencari.value = false
            }
        }
    }

    /** Memperbarui kata kunci pencarian. */
    fun ketik(kata: String) {
        _kataKunci.value = kata
    }
}
