package id.nusantara.quran.fitur.unggulan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelBacaanUnggulan
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan bacaan unggulan dari bundel lokal. */
@HiltViewModel
class ModelTampilanUnggulan @Inject constructor(
    private val sumber: SumberDataLokal,
) : ViewModel() {

    private val _daftar = MutableStateFlow<List<ModelBacaanUnggulan>>(emptyList())
    val daftar: StateFlow<List<ModelBacaanUnggulan>> = _daftar.asStateFlow()

    init {
        viewModelScope.launch { _daftar.value = sumber.daftarUnggulan() }
    }
}
