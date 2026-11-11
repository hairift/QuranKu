package id.nusantara.quran.inti.ui.tema

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Ragam huruf aplikasi: sans-serif bersih untuk antarmuka, serif klasik untuk kaligrafi Arab. */
val KeluargaHurufUtama = FontFamily.SansSerif
val KeluargaHurufArab = FontFamily.Serif

/** Skala tipografi Material 3 yang disetel agar proporsional, jelas, dan mudah dibaca. */
val TipografiQuranKu = Typography(
    displaySmall = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 31.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Bold,
        fontSize = 21.sp,
        lineHeight = 27.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.5.sp,
        lineHeight = 21.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 19.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Normal,
        fontSize = 15.5.sp,
        lineHeight = 23.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Normal,
        fontSize = 13.8.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Normal,
        fontSize = 12.2.sp,
        lineHeight = 17.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.5.sp,
        lineHeight = 18.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = KeluargaHurufUtama,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.3.sp,
    ),
)
