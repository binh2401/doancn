package sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class BackgroundMusicPlayer {
    private Clip clip;

    // Phát nhạc nền
    public void playBackgroundMusic(String soundPath) {
        try {
            // Lấy tệp âm thanh từ đường dẫn
            InputStream audioSrc = getClass().getResourceAsStream(soundPath);
            if (audioSrc == null) {
                throw new IllegalArgumentException("Tệp âm thanh không tồn tại: " + soundPath);
            }
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

            // Khởi tạo và mở Clip
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Lặp lại âm thanh
            clip.start(); // Bắt đầu phát âm thanh

            // Giảm âm lượng
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(0f); // Giảm âm lượng xuống 10dB, điều chỉnh giá trị này nếu cần

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Dừng nhạc nền
    public void stopBackgroundMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // Dừng phát âm thanh
            clip.close(); // Đóng Clip
        }
    }
}
