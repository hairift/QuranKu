package id.nusantara.quran.inti.ui.tema

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Tema Material 3 dengan warna hijau zamrud dan krem. */
@Composable
fun TemaNusantara(gelap: Boolean, isi: @Composable () -> Unit) {
    val warna = if (gelap) darkColorScheme(
        primary = HijauMalam, secondary = EmasNusantara,
        background = HijauLatarMalam, surface = PermukaanMalam
    ) else lightColorScheme(
        primary = HijauZamrud, secondary = EmasNusantara,
        background = KremKertas, surface = ColorPutih
    )
    MaterialTheme(colorScheme = warna, content = isi)
}

private val ColorPutih = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
