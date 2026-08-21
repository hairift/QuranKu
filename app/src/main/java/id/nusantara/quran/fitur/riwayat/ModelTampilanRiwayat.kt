package id.nusantara.quran.fitur.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.entitas.EntitasRiwayat
import id.nusantara.quran.data.repositori.RepositoriQuran
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan riwayat bacaan yang diamati langsung dari Room. */
@HiltViewModel
class ModelTampilanRiwayat @Inject constructor(
    private val repositori: RepositoriQuran,
) : ViewModel() {

    /** Seluruh riwayat bacaan, terbaru di urutan atas. */
    val riwayat: StateFlow<List<EntitasRiwayat>> =
        repositori.riwayat().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** Menghapus satu butir riwayat. */
    fun hapus(surah: Int, ayat: Int) {
        viewModelScope.launch { repositori.hapusRiwayat(surah, ayat) }
    }

    /** Mengosongkan seluruh riwayat bacaan. */
    fun bersihkanSemua() {
        viewModelScope.launch { repositori.bersihkanRiwayat() }
    }
}
