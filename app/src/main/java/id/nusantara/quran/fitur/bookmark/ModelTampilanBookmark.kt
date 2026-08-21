package id.nusantara.quran.fitur.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.domain.usecase.KelolaBookmark
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan bookmark yang menyimpan perubahan ke Room. */
@HiltViewModel
class ModelTampilanBookmark @Inject constructor(private val kelolaBookmark: KelolaBookmark) : ViewModel() {
    val daftar = kelolaBookmark.daftar().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun simpan(idAyat: Int, catatan: String) = viewModelScope.launch {
        kelolaBookmark.simpan(EntitasBookmark(idAyatAwal = idAyat, judulCatatan = "Ayat tersimpan", isiCatatan = catatan))
    }

    fun hapus(data: EntitasBookmark) = viewModelScope.launch { kelolaBookmark.hapus(data) }
    fun ubahCatatan(data: EntitasBookmark, judul: String, isi: String) = viewModelScope.launch { kelolaBookmark.ubahCatatan(data.id, judul, isi) }
}
