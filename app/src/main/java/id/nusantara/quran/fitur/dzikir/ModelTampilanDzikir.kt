package id.nusantara.quran.fitur.dzikir

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelDzikir
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Model tampilan Dzikir & Tasbih Digital: memuat dzikir pagi, petang,
 * dan setelah sholat dari bundel lokal.
 */
@HiltViewModel
class ModelTampilanDzikir @Inject constructor(
    private val sumber: SumberDataLokal,
) : ViewModel() {

    private val _daftar = MutableStateFlow<List<ModelDzikir>>(emptyList())
    val daftar: StateFlow<List<ModelDzikir>> = _daftar.asStateFlow()

    init {
        viewModelScope.launch {
            _daftar.value = sumber.daftarDzikir()
        }
    }

    /** Mengambil daftar dzikir sesuai kategori ("pagi", "petang", "sholat"). */
    fun berdasarkanKategori(kategori: String): List<ModelDzikir> =
        _daftar.value.filter { it.kategori == kategori }
}

