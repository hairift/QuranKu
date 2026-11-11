package id.nusantara.quran.inti.ui.tema

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/** Skema warna terang bernuansa kertas mushaf gading dan zamrud Nusantara. */
private val SkemaTerang = lightColorScheme(
    primary = HijauZamrud,
    onPrimary = PermukaanKertas,
    primaryContainer = HijauZamrudMuda,
    onPrimaryContainer = HijauZamrudTua,
    secondary = EmasNusantara,
    onSecondary = PermukaanKertas,
    secondaryContainer = EmasLembut,
    onSecondaryContainer = HijauZamrudTua,
    tertiary = HijauZamrudTerang,
    onTertiary = PermukaanKertas,
    background = KremKertas,
    onBackground = TintaUtama,
    surface = PermukaanKertas,
    onSurface = TintaUtama,
    surfaceVariant = PermukaanKertasTeduh,
    onSurfaceVariant = TintaRedup,
    surfaceTint = Color.Transparent,
    outline = GarisKertas,
    outlineVariant = GarisKertas,
)

/** Skema warna gelap bernuansa malam zamrud teduh yang nyaman di mata. */
private val SkemaGelap = darkColorScheme(
    primary = HijauMalam,
    onPrimary = HijauLatarMalam,
    primaryContainer = HijauZamrudTua,
    onPrimaryContainer = HijauMalam,
    secondary = EmasTerang,
    onSecondary = HijauLatarMalam,
    secondaryContainer = PermukaanMalamNaik,
    onSecondaryContainer = EmasTerang,
    tertiary = HijauZamrudTerang,
    onTertiary = TintaMalam,
    background = HijauLatarMalam,
    onBackground = TintaMalam,
    surface = PermukaanMalam,
    onSurface = TintaMalam,
    surfaceVariant = PermukaanMalamNaik,
    onSurfaceVariant = TintaMalamRedup,
    surfaceTint = Color.Transparent,
    outline = GarisMalam,
    outlineVariant = GarisMalam,
)

/** Tema utama QuranKu dengan sistem warna dan tipografi Material 3. */
@Composable
fun TemaNusantara(gelap: Boolean, isi: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (gelap) SkemaGelap else SkemaTerang,
        typography = TipografiQuranKu,
        content = isi,
    )
}
