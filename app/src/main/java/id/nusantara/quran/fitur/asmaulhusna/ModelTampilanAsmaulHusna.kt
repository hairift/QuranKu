package id.nusantara.quran.fitur.asmaulhusna

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelAsmaulHusna
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Model tampilan 99 Asmaul Husna: memuat seluruh nama indah Allah
 * dari bundel aset lokal dan menyediakan penyaringan instan.
 */
@HiltViewModel
class ModelTampilanAsmaulHusna @Inject constructor(
    private val sumber: SumberDataLokal,
) : ViewModel() {

    private val _daftar = MutableStateFlow<List<ModelAsmaulHusna>>(emptyList())
    val daftar: StateFlow<List<ModelAsmaulHusna>> = _daftar.asStateFlow()

    init {
        viewModelScope.launch {
            _daftar.value = sumber.daftarAsmaulHusna()
        }
    }

    /** Menyaring daftar Asmaul Husna berdasarkan nomor, nama Latin, atau arti. */
    fun saring(kataKunci: String): List<ModelAsmaulHusna> {
        val asal = _daftar.value
        if (kataKunci.isBlank()) return asal
        val kunci = kataKunci.trim().lowercase()
        return asal.filter {
            it.nomor.toString() == kunci ||
                it.latin.lowercase().contains(kunci) ||
                it.arti.lowercase().contains(kunci) ||
                it.keterangan.lowercase().contains(kunci)
        }
    }
}

