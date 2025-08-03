package com.nextinnomind.biblequizapp.Loader;

import javafx.scene.image.Image;

import java.net.URL;
import java.util.logging.Logger;

public class ImageLoader {

    private static final Logger LOGGER = Logger.getLogger(ImageLoader.class.getName());

    public static Image imageLoad(String path) {
        URL imageUrl = ImageLoader.class.getResource(path);

        if (imageUrl == null) {
            LOGGER.warning("Image not found at: " + path);
            return null;
        }

        return new Image(imageUrl.toExternalForm());
    }
}
