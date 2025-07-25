//
//
//
//package com.nextinnomind.biblequizapp.utils;
//
//import javafx.application.Platform;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import javax.sound.sampled.*;
//import java.io.BufferedInputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class SoundPlayer {
//
//    private static final Logger logger = LogManager.getLogger(SoundPlayer.class);
//
//    private static MediaPlayer mediaPlayer;
//    private static Clip audioClip;
//    private static Timer timer;
//
//    /**
//     * Play audio normally until it finishes.
//     */
//    public static void play(String resourcePath) {
//        stop(); // Ensure no overlapping
//        if (!tryPlayWithJavaFX(resourcePath)) {
//            tryPlayWithJavaSound(resourcePath);
//        }
//    }
//
//    /**
//     * Play audio for N seconds then stop automatically.
//     */
//    public static void play(String resourcePath, int seconds) {
//        stop();
//        if (!tryPlayWithJavaFX(resourcePath, seconds)) {
//            tryPlayWithJavaSound(resourcePath, seconds);
//        }
//    }
//
//    /**
//     * Stop all playback.
//     */
//    public static void stop() {
//        try {
//            if (mediaPlayer != null) {
//                Platform.runLater(() -> {
//                    mediaPlayer.stop();
//                    mediaPlayer.dispose();
//                    mediaPlayer = null;
//                });
//            }
//        } catch (Exception e) {
//            logger.warn("Error stopping MediaPlayer: {}", e.getMessage());
//        }
//
//        try {
//            if (audioClip != null && audioClip.isRunning()) {
//                audioClip.stop();
//                audioClip.close();
//                audioClip = null;
//            }
//        } catch (Exception e) {
//            logger.warn("Error stopping Clip: {}", e.getMessage());
//        }
//
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    // -------- Internal Methods --------
//
//    private static boolean tryPlayWithJavaFX(String resourcePath) {
//        try {
//            URL url = SoundPlayer.class.getResource(resourcePath);
//            if (url == null) {
//                logger.warn("JavaFX resource not found: {}", resourcePath);
//                return false;
//            }
//
//            Media media = new Media(url.toExternalForm());
//            mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.setVolume(0.6);
//
//            mediaPlayer.setOnReady(() -> {
//                mediaPlayer.play();
//                mediaPlayer.setOnEndOfMedia(() -> {
//                    mediaPlayer.dispose();
//                    mediaPlayer = null;
//                });
//            });
//
//            mediaPlayer.setOnError(() -> {
//                logger.warn("JavaFX MediaPlayer error: {}", mediaPlayer.getError().getMessage());
//            });
//
//            return true;
//
//        } catch (Exception e) {
//            logger.warn("JavaFX MediaPlayer failed: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    private static boolean tryPlayWithJavaFX(String resourcePath, int seconds) {
//        try {
//            URL url = SoundPlayer.class.getResource(resourcePath);
//            if (url == null) {
//                logger.warn("JavaFX resource not found: {}", resourcePath);
//                return false;
//            }
//
//            Media media = new Media(url.toExternalForm());
//            mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.setVolume(0.6);
//
//            mediaPlayer.setOnReady(() -> {
//                mediaPlayer.play();
//
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        stop();
//                    }
//                }, seconds * 1000L);
//            });
//
//            mediaPlayer.setOnError(() -> {
//                logger.warn("JavaFX MediaPlayer error: {}", mediaPlayer.getError().getMessage());
//            });
//
//            return true;
//
//        } catch (Exception e) {
//            logger.warn("JavaFX MediaPlayer failed (timed): {}", e.getMessage());
//            return false;
//        }
//    }
//
//    private static void tryPlayWithJavaSound(String resourcePath) {
//        try (InputStream stream = SoundPlayer.class.getResourceAsStream(resourcePath)) {
//            if (stream == null) {
//                logger.warn("Java Sound resource not found: {}", resourcePath);
//                return;
//            }
//
//            BufferedInputStream bufferedIn = new BufferedInputStream(stream);
//            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
//
//            AudioFormat baseFormat = audioStream.getFormat();
//            AudioFormat decodedFormat = new AudioFormat(
//                    AudioFormat.Encoding.PCM_SIGNED,
//                    baseFormat.getSampleRate(),
//                    16,
//                    baseFormat.getChannels(),
//                    baseFormat.getChannels() * 2,
//                    baseFormat.getSampleRate(),
//                    false
//            );
//
//            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
//
//            audioClip = AudioSystem.getClip();
//            audioClip.open(decodedStream);
//            audioClip.start();
//
//        } catch (Exception e) {
//            logger.warn("Java Sound playback failed: {}", e.getMessage());
//        }
//    }
//
//    private static void tryPlayWithJavaSound(String resourcePath, int seconds) {
//        try (InputStream stream = SoundPlayer.class.getResourceAsStream(resourcePath)) {
//            if (stream == null) {
//                logger.warn("Java Sound resource not found: {}", resourcePath);
//                return;
//            }
//
//            BufferedInputStream bufferedIn = new BufferedInputStream(stream);
//            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
//
//            AudioFormat baseFormat = audioStream.getFormat();
//            AudioFormat decodedFormat = new AudioFormat(
//                    AudioFormat.Encoding.PCM_SIGNED,
//                    baseFormat.getSampleRate(),
//                    16,
//                    baseFormat.getChannels(),
//                    baseFormat.getChannels() * 2,
//                    baseFormat.getSampleRate(),
//                    false
//            );
//
//            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);
//
//            audioClip = AudioSystem.getClip();
//            audioClip.open(decodedStream);
//            audioClip.start();
//
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    stop();
//                }
//            }, seconds * 1000L);
//
//        } catch (Exception e) {
//            logger.warn("Java Sound (timed) playback failed: {}", e.getMessage());
//        }
//    }
//}






package com.nextinnomind.biblequizapp.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

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

    // Play and wait to complete naturally
//    public static void playToCompletion(String resourcePath) {
//        if (!tryJavaFXAudioWait(resourcePath)) {
//            tryJavaSoundAudioWait(resourcePath);
//        }
//    }

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

            BufferedInputStream bufferedIn = new BufferedInputStream(stream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = getPlayableFormat(audioStream.getFormat());
            audioStream = AudioSystem.getAudioInputStream(format, audioStream);

            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentClip.start();

        } catch (Exception e) {
            logger.warn("Java Sound API failed: {}", e.getMessage());
        }
    }

    private static void tryJavaSoundAudioTimed(String resourcePath, int seconds) {
        try (InputStream stream = SoundPlayer.class.getResourceAsStream(resourcePath)) {
            if (stream == null) return;

            BufferedInputStream bufferedIn = new BufferedInputStream(stream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);
            AudioFormat format = getPlayableFormat(audioStream.getFormat());
            audioStream = AudioSystem.getAudioInputStream(format, audioStream);

            currentClip = AudioSystem.getClip();
            currentClip.open(audioStream);
            currentClip.start();

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
}
