package com.nextinnomind.biblequizapp.Manager;

import com.nextinnomind.biblequizapp.Loader.JsonDataLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShutdownManager {

    private static final Logger logger = LogManager.getLogger(ShutdownManager.class);

    public static void setupShutdownHook(ViewModeSelectorManager selector) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            cleanup(selector);
            logger.info("JVM shutdown hook triggered.");

            if (selector != null) {
                selector.clearSavedChoice();
            }
            AudioManager.stopAllAudio();
        }));
    }

    public static void cleanup(ViewModeSelectorManager selector) {
        try {
            JsonDataLoader.getInstance();
            if (selector != null) {
                selector.clearSavedChoice();
            }
            logger.info("Cleanup completed successfully.");
        } catch (Exception e) {
            logger.error("Error during cleanup: {}", e.getMessage(), e);
        }
    }
}