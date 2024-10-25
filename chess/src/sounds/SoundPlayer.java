package sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundPlayer {

    public void playSound(String soundPath) {
        try {
            // Sử dụng getResourceAsStream() để lấy tệp âm thanh từ đường dẫn
            InputStream audioSrc = getClass().getResourceAsStream(soundPath);
            if (audioSrc == null) {
                throw new IllegalArgumentException("Tệp âm thanh không tồn tại: " + soundPath);
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Phát âm thanh
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
