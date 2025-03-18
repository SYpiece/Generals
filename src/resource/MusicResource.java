package resource;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public final class MusicResource extends Resource {
    private static final Clip _clip = getClip();
    public static final String gongMusicName = "gong.wav";
    public static final URL gongMusicURL = MusicResource.class.getResource(gongMusicName);
    private MusicResource() {}
    private static Clip getClip() {
        try {
            return AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void playMusic(String name) {
        playMusic(MusicResource.class.getResource(name));
    }
    public static void playMusic(URL url) {
        try {
            _clip.open(AudioSystem.getAudioInputStream(url));
            _clip.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }
}
