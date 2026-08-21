package id.nusantara.quran.inti.ui.tema

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** Skema warna terang bernuansa kertas dan zamrud. */
private val SkemaTerang = lightColorScheme(
    primary = HijauZamrud,
    onPrimary = PermukaanKertas,
    primaryContainer = HijauZamrudMuda,
    onPrimaryContainer = HijauZamrudTua,
    secondary = EmasNusantara,
    onSecondary = TintaUtama,
    secondaryContainer = EmasLembut,
    onSecondaryContainer = TintaUtama,
    background = KremKertas,
    onBackground = TintaUtama,
    surface = PermukaanKertas,
    onSurface = TintaUtama,
    surfaceVariant = HijauZamrudMuda,
    onSurfaceVariant = TintaRedup,
    outline = GarisKertas,
)

/** Skema warna gelap bernuansa malam hutan. */
private val SkemaGelap = darkColorScheme(
    primary = HijauMalam,
    onPrimary = HijauLatarMalam,
    primaryContainer = HijauZamrudTua,
    onPrimaryContainer = HijauMalam,
    secondary = EmasNusantara,
    onSecondary = HijauLatarMalam,
    secondaryContainer = PermukaanMalamNaik,
    onSecondaryContainer = EmasNusantara,
    background = HijauLatarMalam,
    onBackground = TintaMalam,
    surface = PermukaanMalam,
    onSurface = TintaMalam,
    surfaceVariant = PermukaanMalamNaik,
    onSurfaceVariant = TintaMalamRedup,
    outline = GarisMalam,
)

/** Tema utama QuranKu dengan Material 3. */
@Composable
fun TemaNusantara(gelap: Boolean, isi: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (gelap) SkemaGelap else SkemaTerang,
        typography = TipografiQuranKu,
        content = isi,
    )
}
