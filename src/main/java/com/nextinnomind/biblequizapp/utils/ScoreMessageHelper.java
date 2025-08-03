package com.nextinnomind.biblequizapp.utils;

import com.nextinnomind.biblequizapp.Loader.ImageLoader;
import com.nextinnomind.biblequizapp.widget.ScoreUIConstants;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class ScoreMessageHelper {

    public ScoreMessageHelper() {

    }

    public void configureSuccessUI(ImageView trophyImage, Circle trophyBackground,
                                   Label resultTitle, Label resultMessage) {
        trophyImage.setImage(ImageLoader.imageLoad(ScoreUIConstants.TROPHY_W_IMAGE_PATH));

        trophyBackground.setStyle(ScoreUIConstants.SUCCESS_TROPHY_STYLE);

        int randomIndex = getRandomIndex(ScoreUIConstants.SUCCESS_TITLES.length);
        resultTitle.setText(ScoreUIConstants.SUCCESS_TITLES[randomIndex]);
        resultMessage.setText(ScoreUIConstants.SUCCESS_MESSAGES[randomIndex]);
        resultTitle.setStyle(ScoreUIConstants.SUCCESS_TITLE_STYLE);
    }

    public void configureFailureUI(ImageView trophyImage, Circle trophyBackground,
                                   Label resultTitle, Label resultMessage) {
        trophyImage.setImage(ImageLoader.imageLoad(ScoreUIConstants.TROPHY_F_IMAGE_PATH));
        trophyBackground.setStyle(ScoreUIConstants.FAILURE_TROPHY_STYLE);

        int randomIndex = getRandomIndex(ScoreUIConstants.FAILURE_TITLES.length);
        resultTitle.setText(ScoreUIConstants.FAILURE_TITLES[randomIndex]);
        resultMessage.setText(ScoreUIConstants.FAILURE_MESSAGES[randomIndex]);
        resultTitle.setStyle(ScoreUIConstants.FAILURE_TITLE_STYLE);
    }

    private int getRandomIndex(int arrayLength) {
        return (int) (Math.random() * arrayLength);
    }
}