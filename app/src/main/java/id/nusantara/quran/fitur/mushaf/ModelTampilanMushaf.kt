package id.nusantara.quran.fitur.mushaf

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.data.lokal.entitas.EntitasRiwayat
import id.nusantara.quran.data.repositori.RepositoriQuran
import id.nusantara.quran.domain.model.ModelAyat
import id.nusantara.quran.domain.model.ModelSurah
import id.nusantara.quran.fitur.audio.DAFTAR_QARI
import id.nusantara.quran.fitur.audio.ServisPemutarAudio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Ringkasan satu juz untuk daftar navigasi 30 Juz di Mushaf. */
data class InfoJuz(
    val nomorJuz: Int,
    val nomorSurahAwal: Int,
    val namaSurahAwal: String,
    val nomorAyatAwal: Int,
)

/**
 * Model tampilan mushaf: daftar surah, indeks 30 juz, pembaca ayat per surah,
 * pemutar audio per ayat, bookmark persisten Room, ukuran teks, dan posisi baca terakhir.
 */
@HiltViewModel
class ModelTampilanMushaf @Inject constructor(
    @ApplicationContext private val konteks: Context,
    private val sumber: SumberDataLokal,
    private val repositori: RepositoriQuran,
    private val pengaturan: PengaturanAplikasi,
) : ViewModel() {

    private val _daftarSurah = MutableStateFlow<List<ModelSurah>>(emptyList())
    val daftarSurah: StateFlow<List<ModelSurah>> = _daftarSurah.asStateFlow()

    private val _daftarJuz = MutableStateFlow<List<InfoJuz>>(emptyList())
    val daftarJuz: StateFlow<List<InfoJuz>> = _daftarJuz.asStateFlow()

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
    val tampilTerjemahan = pengaturan.tampilTerjemahan.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), true)

    private var pengontrolAudio: MediaController? = null

    init {
        viewModelScope.launch {
            val surah = sumber.daftarSurah()
            _daftarSurah.value = surah
            _sedangMemuat.value = false

            // Bangun indeks 30 Juz di latar belakang
            val semua = sumber.semuaAyat()
            val petaSurah = surah.associateBy { it.nomor }
            val listJuz = (1..30).mapNotNull { juz ->
                val awal = semua.firstOrNull { it.juz == juz } ?: return@mapNotNull null
                InfoJuz(
                    nomorJuz = juz,
                    nomorSurahAwal = awal.surah,
                    namaSurahAwal = petaSurah[awal.surah]?.namaLatin ?: "Surah ${awal.surah}",
                    nomorAyatAwal = awal.nomor,
                )
            }
            _daftarJuz.value = listJuz
        }
        hubungkanAudio()
    }

    private fun hubungkanAudio() {
        val token = SessionToken(konteks, ComponentName(konteks, ServisPemutarAudio::class.java))
        val masaDepan = MediaController.Builder(konteks, token).buildAsync()
        masaDepan.addListener({
            pengontrolAudio = runCatching { masaDepan.get() }.getOrNull()
        }, MoreExecutors.directExecutor())
    }

    /** Memutar satu ayat pilihan langsung dari kartu mushaf. */
    fun putarAyat(surah: ModelSurah, butirAyat: ModelAyat) {
        viewModelScope.launch {
            val idQari = pengaturan.qariPilihan.first()
            val namaQari = DAFTAR_QARI.firstOrNull { it.id == idQari }?.nama ?: "Murottal"
            val url = "https://everyayah.com/data/$idQari/" +
                "${surah.nomor.toString().padStart(3, '0')}${butirAyat.nomor.toString().padStart(3, '0')}.mp3"
            val item = MediaItem.Builder()
                .setMediaId("${surah.nomor}:${butirAyat.nomor}")
                .setUri(url)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle("${surah.namaLatin} · Ayat ${butirAyat.nomor}")
                        .setArtist(namaQari)
                        .build(),
                )
                .build()
            pengontrolAudio?.let {
                it.setMediaItem(item)
                it.prepare()
                it.play()
            }
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
                    EntitasBookmark(idAyatAwal = ayat.id, judulCatatan = "QS ${ayat.surah}:${ayat.nomor}"),
                )
            }
        }
    }

    /** Mengubah ukuran teks Arab dan terjemahan lalu menyimpannya. */
    fun aturUkuranTeks(arab: Int, terjemahan: Int) {
        viewModelScope.launch { pengaturan.aturUkuranTeks(arab, terjemahan) }
    }

    /** Menampilkan atau menyembunyikan teks terjemahan untuk mode fokus tilawah. */
    fun alihkanTampilTerjemahan() {
        viewModelScope.launch {
            val sekarang = tampilTerjemahan.value
            pengaturan.aturTampilTerjemahan(!sekarang)
        }
    }

    /** Mencari surah berdasarkan nama latin, arti, atau nomor, serta filter tipe. */
    fun cariSurah(kata: String, filterTipe: String = "Semua"): List<ModelSurah> {
        val dasar = when (filterTipe) {
            "Makkiyah" -> _daftarSurah.value.filter { it.tipe.equals("Makkiyah", ignoreCase = true) }
            "Madaniyah" -> _daftarSurah.value.filter { it.tipe.equals("Madaniyah", ignoreCase = true) }
            else -> _daftarSurah.value
        }
        if (kata.isBlank()) return dasar
        val kunci = kata.trim().lowercase()
        return dasar.filter {
            it.namaLatin.lowercase().contains(kunci) ||
                it.arti.lowercase().contains(kunci) ||
                it.nomor.toString() == kunci
        }
    }

    override fun onCleared() {
        pengontrolAudio?.release()
        pengontrolAudio = null
    }
}
