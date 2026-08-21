package id.nusantara.quran.fitur.beranda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.data.repositori.RepositoriSholat
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelSurah
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Model tampilan beranda: ayat harian, posisi baca terakhir,
 * dan hitung mundur menuju waktu sholat berikutnya.
 */
@HiltViewModel
class ModelTampilanBeranda @Inject constructor(
    private val sumber: SumberDataLokal,
    private val repositoriSholat: RepositoriSholat,
    private val pengaturan: PengaturanAplikasi,
) : ViewModel() {

    private val _ayatHarian = MutableStateFlow<ModelAyat?>(null)
    val ayatHarian: StateFlow<ModelAyat?> = _ayatHarian.asStateFlow()

    private val _surahAyatHarian = MutableStateFlow<ModelSurah?>(null)
    val surahAyatHarian: StateFlow<ModelSurah?> = _surahAyatHarian.asStateFlow()

    private val _sholatBerikutnya = MutableStateFlow<Triple<String, String, Long>?>(null)
    val sholatBerikutnya: StateFlow<Triple<String, String, Long>?> = _sholatBerikutnya.asStateFlow()

    /** Posisi baca terakhir beserta nama surahnya. */
    val posisiBaca: StateFlow<Triple<Int, Int, String>> = combine(
        pengaturan.surahTerakhir, pengaturan.ayatTerakhir,
    ) { surah, ayat -> surah to ayat }
        .let { alur ->
            kotlinx.coroutines.flow.flow {
                alur.collect { (surah, ayat) ->
                    val nama = sumber.daftarSurah().firstOrNull { it.nomor == surah }?.namaLatin ?: ""
                    emit(Triple(surah, ayat, nama))
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Triple(1, 1, "Al-Fatihah"))

    val namaKota = pengaturan.namaKota.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "Kota Jakarta")

    init {
        viewModelScope.launch {
            val ayat = sumber.ayatHarian()
            _ayatHarian.value = ayat
            _surahAyatHarian.value = ayat?.let { a ->
                sumber.daftarSurah().firstOrNull { it.nomor == a.surah }
            }
        }
        // Muat jadwal lalu segarkan hitung mundur setiap menit.
        viewModelScope.launch {
            pengaturan.idKota.collect { idKota ->
                val jadwal = repositoriSholat.jadwalHariIni(idKota)
                while (true) {
                    _sholatBerikutnya.value = repositoriSholat.sholatBerikutnya(jadwal)
                    delay(60_000)
                }
            }
        }
    }
}
