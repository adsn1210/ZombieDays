package ucjc.ev2.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;

public class SoundPlayer {

    private static Clip clip;

    // Reproduce una pista en loop (si no está ya sonando)
    public static void reproducir(String ruta) {
        if (clip != null && clip.isRunning()) return;

        try {
            var url = SoundPlayer.class.getResource(ruta);
            if (url == null) {
                System.err.println("NO SE ENCUENTRA EL AUDIO: " + ruta);
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(url.openStream())
            );

            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (Exception e) {
            System.err.println("Error al reproducir música");
            e.printStackTrace();
        }
    }

    // Detiene la música
    public static void detener() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
    }
}
