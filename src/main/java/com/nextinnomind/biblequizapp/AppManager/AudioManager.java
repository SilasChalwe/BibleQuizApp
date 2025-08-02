package com.nextinnomind.biblequizapp.AppManager;

import com.nextinnomind.biblequizapp.utils.SoundPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AudioManager {

    private static final Logger logger = LogManager.getLogger(AudioManager.class);

    public static void stopAllAudio() {
        SoundPlayer.stop();
        logger.debug("All audio stopped via SoundPlayer");
    }
}