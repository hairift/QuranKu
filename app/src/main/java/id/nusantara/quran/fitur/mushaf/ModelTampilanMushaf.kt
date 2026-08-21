package id.nusantara.quran.fitur.mushaf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.data.lokal.entitas.EntitasRiwayat
import id.nusantara.quran.data.repositori.RepositoriQuran
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelSurah
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Model tampilan mushaf: daftar surah, pembaca ayat per surah,
 * bookmark persisten Room, ukuran teks, dan posisi baca terakhir.
 */
@HiltViewModel
class ModelTampilanMushaf @Inject constructor(
    private val sumber: SumberDataLokal,
    private val repositori: RepositoriQuran,
    private val pengaturan: PengaturanAplikasi,
) : ViewModel() {

    private val _daftarSurah = MutableStateFlow<List<ModelSurah>>(emptyList())
    val daftarSurah: StateFlow<List<ModelSurah>> = _daftarSurah.asStateFlow()

    private val _surahAktif = MutableStateFlow<ModelSurah?>(null)
    val surahAktif: StateFlow<ModelSurah?> = _surahAktif.asStateFlow()

    private val _ayat = MutableStateFlow<List<ModelAyat>>(emptyList())
    val ayat: StateFlow<List<ModelAyat>> = _ayat.asStateFlow()

    private val _sedangMemuat = MutableStateFlow(true)
    val sedangMemuat: StateFlow<Boolean> = _sedangMemuat.asStateFlow()

    /** Bookmark tersimpan diamati langsung dari Room. */
    val bookmark = repositori.bookmark().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val ukuranArab = pengaturan.ukuranArab.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 30)
    val ukuranTerjemahan = pengaturan.ukuranTerjemahan.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 16)

    init {
        viewModelScope.launch {
            _daftarSurah.value = sumber.daftarSurah()
            _sedangMemuat.value = false
        }
    }

    /** Membuka satu surah dan memuat seluruh ayatnya. */
    fun bukaSurah(nomor: Int) {
        viewModelScope.launch {
            _sedangMemuat.value = true
            _surahAktif.value = sumber.daftarSurah().firstOrNull { it.nomor == nomor }
            _ayat.value = sumber.ayatSurah(nomor)
            _sedangMemuat.value = false
        }
    }

    /** Menutup pembaca dan kembali ke daftar surah. */
    fun tutupSurah() {
        _surahAktif.value = null
        _ayat.value = emptyList()
    }

    /** Menyimpan posisi baca terakhir sekaligus mencatat riwayat bacaan. */
    fun catatPosisiBaca(surah: Int, ayat: Int) {
        viewModelScope.launch {
            pengaturan.aturPosisiBaca(surah, ayat)
            val nama = sumber.daftarSurah().firstOrNull { it.nomor == surah }?.namaLatin ?: ""
            repositori.simpanRiwayat(EntitasRiwayat(surah = surah, ayat = ayat, namaSurah = nama))
        }
    }

    /** Menambah atau menghapus bookmark satu ayat. */
    fun alihkanBookmark(ayat: ModelAyat) {
        viewModelScope.launch {
            val ada = bookmark.first().firstOrNull { it.idAyatAwal == ayat.id }
            if (ada != null) {
                repositori.hapusBookmark(ada)
            } else {
                repositori.simpanBookmark(
                    EntitasBookmark(idAyatAwal = ayat.id, judulCatatan = "QS ${ayat.surah}:${ayat.nomor}")
                )
            }
        }
    }

    /** Mengubah ukuran teks Arab dan terjemahan lalu menyimpannya. */
    fun aturUkuranTeks(arab: Int, terjemahan: Int) {
        viewModelScope.launch { pengaturan.aturUkuranTeks(arab, terjemahan) }
    }

    /** Mencari surah berdasarkan nama latin atau nomor. */
    fun cariSurah(kata: String): List<ModelSurah> {
        if (kata.isBlank()) return _daftarSurah.value
        val kunci = kata.trim().lowercase()
        return _daftarSurah.value.filter {
            it.namaLatin.lowercase().contains(kunci) || it.nomor.toString() == kunci
        }
    }
}
