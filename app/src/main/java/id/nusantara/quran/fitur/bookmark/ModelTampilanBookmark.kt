package id.nusantara.quran.fitur.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.nusantara.quran.data.lokal.entitas.EntitasBookmark
import id.nusantara.quran.domain.usecase.KelolaBookmark
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import javax.inject.Inject

/** Model tampilan bookmark yang menyimpan perubahan ke Room. */
@HiltViewModel
class ModelTampilanBookmark @Inject constructor(private val kelolaBookmark: KelolaBookmark) : ViewModel() {

    val daftar = kelolaBookmark.daftar().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun simpan(idAyat: Int, catatan: String) = viewModelScope.launch {
        kelolaBookmark.simpan(EntitasBookmark(idAyatAwal = idAyat, judulCatatan = "Ayat tersimpan", isiCatatan = catatan))
    }

    fun hapus(data: EntitasBookmark) = viewModelScope.launch { kelolaBookmark.hapus(data) }

    fun ubahCatatan(data: EntitasBookmark, judul: String, isi: String) = viewModelScope.launch {
        kelolaBookmark.ubahCatatan(data.id, judul, isi)
    }

    /**
     * Mengimpor bookmark dari teks JSON hasil ekspor QuranKu.
     * Mengembalikan jumlah yang berhasil diimpor, atau -1 bila berkas tidak valid.
     */
    suspend fun imporJson(teks: String): Int {
        val larik = runCatching { JSONArray(teks) }.getOrNull() ?: return -1
        var jumlah = 0
        for (indeks in 0 until larik.length()) {
            val butir = larik.optJSONObject(indeks) ?: continue
            val idAyat = butir.optInt("idAyatAwal", 0)
            if (idAyat <= 0) continue
            kelolaBookmark.simpan(
                EntitasBookmark(
                    idAyatAwal = idAyat,
                    idAyatAkhir = if (butir.isNull("idAyatAkhir")) null else butir.optInt("idAyatAkhir"),
                    judulCatatan = butir.optString("judulCatatan", ""),
                    isiCatatan = butir.optString("isiCatatan", ""),
                    warnaTag = butir.optString("warnaTag", "#C8A951"),
                    tanggalDibuat = butir.optLong("tanggalDibuat", System.currentTimeMillis()),
                ),
            )
            jumlah++
        }
        return jumlah
    }
}
