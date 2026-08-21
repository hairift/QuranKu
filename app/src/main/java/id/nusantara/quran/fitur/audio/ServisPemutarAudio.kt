package id.nusantara.quran.fitur.audio

import android.content.Intent
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

/**
 * Layanan pemutar murottal yang berjalan di latar belakang.
 * Notifikasi pemutar media dikelola otomatis oleh MediaSession Media3
 * sehingga pemutaran tetap hidup saat layar mati atau aplikasi diminimalkan.
 */
class ServisPemutarAudio : MediaSessionService() {

    private var sesi: MediaSession? = null

    override fun onCreate() {
        super.onCreate()
        val pemutar = ExoPlayer.Builder(this)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(C.USAGE_MEDIA)
                    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
                    .build(),
                /* handleAudioFocus = */ true,
            )
            .setHandleAudioBecomingNoisy(true)
            .setSeekForwardIncrementMs(10_000)
            .setSeekBackIncrementMs(10_000)
            .build()
        pemutar.repeatMode = Player.REPEAT_MODE_OFF
        sesi = MediaSession.Builder(this, pemutar).build()
    }

    override fun onGetSession(infoKontroler: MediaSession.ControllerInfo): MediaSession? = sesi

    override fun onTaskRemoved(niatAkar: Intent?) {
        val pemutar = sesi?.player ?: return
        // Hentikan layanan bila tidak sedang memutar agar tidak menggantung di latar.
        if (!pemutar.playWhenReady || pemutar.mediaItemCount == 0) {
            stopSelf()
        }
    }

    override fun onDestroy() {
        sesi?.run {
            player.release()
            release()
        }
        sesi = null
        super.onDestroy()
    }
}
