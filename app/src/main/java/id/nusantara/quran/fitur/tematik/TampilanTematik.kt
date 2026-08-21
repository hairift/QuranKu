package id.nusantara.quran.fitur.tematik

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoStories
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Katalog tema doa, adab, nabi, dan sains dari ayat Al-Quran. */
@Composable
fun TampilanTematik(modifier: Modifier = Modifier, onKembali: () -> Unit = {}) { Column(modifier.padding(22.dp)) { IconButton(onClick = onKembali) { androidx.compose.material3.Icon(Icons.Rounded.AutoStories, "Kembali") }; Text("Tematik Al-Quran"); Text("Doa · Adab · Nabi · Solusi · Sains", Modifier.padding(top = 12.dp)) } }
