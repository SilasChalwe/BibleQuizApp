package com.nextinnomind.biblequizapp.utils;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class SoundPlayer {

    private static final Logger logger = LogManager.getLogger(SoundPlayer.class);

    // Hold references to allow stopping
    private static MediaPlayer currentMediaPlayer;
    private static Clip currentClip;

    // Play and allow immediate stop
    public static void play(String resourcePath) {
        stop(); // Stop any currently playing sound first
        if (!tryJavaFXAudio(resourcePath)) {
            tryJavaSoundAudio(resourcePath);
        }
    }



    // Play and stop after given seconds
    public static void play(String resourcePath, int seconds) {
        stop(); // Ensure nothing else is playing
        if (!tryJavaFXAudioTimed(resourcePath, seconds)) {
            tryJavaSoundAudioTimed(resourcePath, seconds);
        }
    }

    public static void stop() {
        try {
            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
                currentMediaPlayer.dispose();
                currentMediaPlayer = null;
            }
            if (currentClip != null && currentClip.isRunning()) {
                currentClip.stop();
                currentClip.close();
                currentClip = null;
            }
        } catch (Exception e) {
            logger.warn("Error stopping sound: {}", e.getMessage());
        }
    }

    private static boolean tryJavaFXAudio(String resourcePath) {
        try {
            URL url = SoundPlayer.class.getResource(resourcePath);
            if (url == null) {
                logger.warn("JavaFX audio resource not found: {}", resourcePath);
                return false;
            }

            Media media = new Media(url.toExternalForm());
            currentMediaPlayer = new MediaPlayer(media);
            currentMediaPlayer.setOnReady(() -> {
                currentMediaPlayer.setVolume(0.6);
                currentMediaPlayer.play();
                currentMediaPlayer.setOnEndOfMedia(() -> {
                    currentMediaPlayer.dispose();
                    currentMediaPlayer = null;
                });
            });
            currentMediaPlayer.setOnError(() -> {
                MediaException error = currentMediaPlayer.getError();
                logger.warn("JavaFX MediaPlayer error: {}", error != null ? error.getMessage() : "Unknown error");
            });

            return true;

        } catch (Exception e) {
            logger.warn("JavaFX MediaPlayer failed: {} - {}", e.getClass().getSimpleName(), e.getMessage());
            return false;
        }
    }

    private static boolean tryJavaFXAudioTimed(String resourcePath, int seconds) {
        try {
            URL url = SoundPlayer.class.getResource(resourcePath);
            if (url == null) return false;

            Media media = new Media(url.toExternalForm());
            currentMediaPlayer = new MediaPlayer(media);
            currentMediaPlayer.setOnReady(() -> {
                currentMediaPlayer.play();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        stop();
                    }
                }, seconds* 1000L);
            });

            return true;
        } catch (Exception e) {
            logger.warn("JavaFX timed play failed: {}", e.getMessage());
            return false;
        }
    }

    private static void tryJavaSoundAudio(String resourcePath) {
        try (InputStream stream = SoundPlayer.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                logger.warn("Fallback audio not found: {}", resourcePath);
                return;
            }

            helper(stream);

        } catch (Exception e) {
            logger.warn("Java Sound API failed: {}", e.getMessage());
        }
    }


    private static void tryJavaSoundAudioTimed(String resourcePath, int seconds) {
        try (InputStream stream = SoundPlayer.class.getResourceAsStream(resourcePath)) {
            if (stream == null) return;

            helper(stream);

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    stop();
                }
            }, seconds * 1000L);

        } catch (Exception e) {
            logger.warn("Java Sound timed play failed: {}", e.getMessage());
        }
    }

    private static AudioFormat getPlayableFormat(AudioFormat baseFormat) {
        if (baseFormat.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
            return new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
        }
        return baseFormat;
    }
    private static void helper(InputStream stream) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        BufferedInputStream bufferedIn = new BufferedInputStream(stream);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
        AudioFormat format = getPlayableFormat(audioStream.getFormat());
        audioStream = AudioSystem.getAudioInputStream(format, audioStream);

        currentClip = AudioSystem.getClip();
        currentClip.open(audioStream);
        currentClip.start();
    }

}
