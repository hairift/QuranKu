package id.nusantara.quran.inti.util

import android.content.Context
import java.io.File
import java.util.Locale

/** Alat bantu menghitung dan membersihkan penyimpanan sementara aplikasi. */
object PengelolaPenyimpanan {

    /** Menghitung total ukuran cache internal dan eksternal dalam bita. */
    fun ukuranCache(konteks: Context): Long {
        var total = 0L
        total += ukuranBerkas(konteks.cacheDir)
        konteks.externalCacheDir?.let { total += ukuranBerkas(it) }
        return total
    }

    /** Menghapus seluruh isi cache tanpa menghapus map induknya. */
    fun bersihkanCache(konteks: Context): Boolean {
        var berhasil = true
        berhasil = kosongkanIsi(konteks.cacheDir) && berhasil
        konteks.externalCacheDir?.let { berhasil = kosongkanIsi(it) && berhasil }
        return berhasil
    }

    /** Format ukuran bita menjadi teks yang mudah dibaca (KB/MB/GB). */
    fun formatUkuran(bita: Long): String = when {
        bita >= 1_073_741_824L -> String.format(Locale.US, "%.1f GB", bita / 1_073_741_824.0)
        bita >= 1_048_576L -> String.format(Locale.US, "%.1f MB", bita / 1_048_576.0)
        bita >= 1_024L -> String.format(Locale.US, "%.1f KB", bita / 1_024.0)
        else -> "$bita B"
    }

    private fun ukuranBerkas(berkas: File?): Long {
        if (berkas == null || !berkas.exists()) return 0L
        if (berkas.isFile) return berkas.length()
        return berkas.listFiles()?.sumOf { ukuranBerkas(it) } ?: 0L
    }

    private fun kosongkanIsi(map: File?): Boolean {
        if (map == null || !map.exists()) return true
        return map.listFiles()?.all { it.deleteRecursively() } ?: true
    }
}
