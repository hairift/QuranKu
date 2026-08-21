package id.nusantara.quran.fitur.pengaturan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.fitur.pengingat.PekerjaAyatHarian
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Model tampilan pengaturan: tema, pengingat ayat harian, dan preferensi lain. */
@HiltViewModel
class ModelTampilanPengaturan @Inject constructor(
    @ApplicationContext private val konteks: Context,
    private val pengaturan: PengaturanAplikasi,
) : ViewModel() {

    val temaGelap = pengaturan.temaGelap.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val pengingatAyat = pengaturan.pengingatAyat.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
    val jamPengingat = pengaturan.jamPengingatAyat.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 6)
    val namaKota = pengaturan.namaKota.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "Kota Jakarta")

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
}
