package id.nusantara.quran.fitur.audio

import android.content.ComponentName
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import id.nusantara.quran.data.lokal.PengaturanAplikasi
import id.nusantara.quran.data.lokal.SumberDataLokal
import id.nusantara.quran.domain.model.ModelSurah
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Qari murottal yang tersedia, memakai berkas EveryAyah. */
data class ModelQari(val id: String, val nama: String)

val DAFTAR_QARI = listOf(
    ModelQari("Alafasy_128kbps", "Misyari Rasyid Al-Afasy"),
    ModelQari("Abdul_Basit_Murattal_192kbps", "Abdul Basit Abdus Samad"),
    ModelQari("Abdurrahmaan_As-Sudais_192kbps", "Abdurrahman As-Sudais"),
    ModelQari("Saood_ash-Shuraym_128kbps", "Sa'ud Asy-Syuraim"),
    ModelQari("Hudhaify_128kbps", "Ali Al-Hudzaifi"),
)

/** Keadaan pemutar yang diamati tampilan. */
data class KeadaanPemutar(
    val terhubung: Boolean = false,
    val sedangMemutar: Boolean = false,
    val judulAktif: String = "",
    val posisiMilis: Long = 0L,
    val durasiMilis: Long = 0L,
)

/**
 * Model tampilan audio: menghubungkan layar ke ServisPemutarAudio
 * melalui MediaController dan menyiapkan daftar putar per surah.
 */
@HiltViewModel
class ModelTampilanAudio @Inject constructor(
    @ApplicationContext private val konteks: Context,
    private val sumber: SumberDataLokal,
    private val pengaturan: PengaturanAplikasi,
) : ViewModel() {

    private val _daftarSurah = MutableStateFlow<List<ModelSurah>>(emptyList())
    val daftarSurah: StateFlow<List<ModelSurah>> = _daftarSurah.asStateFlow()

    private val _keadaan = MutableStateFlow(KeadaanPemutar())
    val keadaan: StateFlow<KeadaanPemutar> = _keadaan.asStateFlow()

    val idQari = pengaturan.qariPilihan.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), DAFTAR_QARI.first().id,
    )

    private var pengontrol: MediaController? = null

    private val pendengar = object : Player.Listener {
        override fun onIsPlayingChanged(sedangMemutar: Boolean) = segarkanKeadaan()
        override fun onMediaItemTransition(item: MediaItem?, alasan: Int) = segarkanKeadaan()
        override fun onPlaybackStateChanged(status: Int) = segarkanKeadaan()
    }

    init {
        viewModelScope.launch { _daftarSurah.value = sumber.daftarSurah() }
        hubungkanPengontrol()
        // Segarkan progres pemutaran secara berkala.
        viewModelScope.launch {
            while (true) {
                if (pengontrol?.isPlaying == true) segarkanKeadaan()
                delay(1_000)
            }
        }
    }

    /** Menghubungkan MediaController ke layanan pemutar. */
    private fun hubungkanPengontrol() {
        val token = SessionToken(konteks, ComponentName(konteks, ServisPemutarAudio::class.java))
        val masaDepan = MediaController.Builder(konteks, token).buildAsync()
        masaDepan.addListener({
            pengontrol = runCatching { masaDepan.get() }.getOrNull()?.also {
                it.addListener(pendengar)
                segarkanKeadaan()
            }
        }, MoreExecutors.directExecutor())
    }

    /** Menyalin kondisi pemutar ke state tampilan. */
    private fun segarkanKeadaan() {
        val pemutar = pengontrol ?: return
        _keadaan.value = KeadaanPemutar(
            terhubung = true,
            sedangMemutar = pemutar.isPlaying,
            judulAktif = pemutar.currentMediaItem?.mediaMetadata?.title?.toString() ?: "",
            posisiMilis = pemutar.currentPosition.coerceAtLeast(0),
            durasiMilis = pemutar.duration.coerceAtLeast(0),
        )
    }

    /** Memutar seluruh ayat satu surah secara berurutan. */
    fun putarSurah(surah: ModelSurah, idQari: String) {
        viewModelScope.launch {
            pengaturan.aturQari(idQari)
            val ayat = sumber.ayatSurah(surah.nomor)
            val daftar = ayat.map { butir ->
                MediaItem.Builder()
                    .setMediaId("${surah.nomor}:${butir.nomor}")
                    .setUri(alamatAudio(idQari, surah.nomor, butir.nomor))
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("${surah.namaLatin} · Ayat ${butir.nomor}")
                            .setArtist(DAFTAR_QARI.firstOrNull { it.id == idQari }?.nama ?: "Murottal")
                            .build(),
                    )
                    .build()
            }
            pengontrol?.let {
                it.setMediaItems(daftar)
                it.prepare()
                it.play()
            }
        }
    }

    fun putarAtauJeda() {
        pengontrol?.let { if (it.isPlaying) it.pause() else it.play() }
    }

    /** Mengganti qari pilihan dan menyimpannya ke preferensi. */
    fun pilihQari(id: String) {
        viewModelScope.launch { pengaturan.aturQari(id) }
    }

    fun berikutnya() = pengontrol?.seekToNextMediaItem()
    fun sebelumnya() = pengontrol?.seekToPreviousMediaItem()
    fun geserKe(milis: Long) = pengontrol?.seekTo(milis)

    /** Membangun alamat berkas audio EveryAyah: SSSAAA.mp3 per ayat. */
    private fun alamatAudio(idQari: String, surah: Int, ayat: Int): String =
        "https://everyayah.com/data/$idQari/${surah.toString().padStart(3, '0')}${ayat.toString().padStart(3, '0')}.mp3"

    override fun onCleared() {
        pengontrol?.removeListener(pendengar)
        pengontrol?.release()
        pengontrol = null
    }
}
