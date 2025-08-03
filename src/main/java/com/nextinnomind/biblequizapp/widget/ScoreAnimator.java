package com.nextinnomind.biblequizapp.widget;


import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
import javafx.util.Duration;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;


public class ScoreAnimator {
    private static final double ANIMATION_DURATION = 1.5;
    private static final double TROPHY_ANIMATION_DELAY = 0.3;
    private static final double TROPHY_ANIMATION_DURATION = 0.5;

    public void animateScoreDisplay(Circle scoreProgressCircle, double score, ImageView trophyImage) {
        animateProgressCircle(scoreProgressCircle, score);
        animateTrophyAppearance(trophyImage);
    }

    private void animateProgressCircle(Circle circle, double percentage) {
        double radius = circle.getRadius();
        double circumference = 2 * Math.PI * radius;
        double arcLength = circumference * (percentage / 100.0);

        configureProgressCircle(circle, circumference, percentage);
        createProgressAnimation(circle, circumference, arcLength);
    }

    private void configureProgressCircle(Circle circle, double circumference, double percentage) {
        circle.setStrokeWidth(6);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeLineCap(StrokeLineCap.ROUND);
        circle.setRotate(-90);
        circle.setScaleY(-1);

        Color strokeColor = percentage >= 50 ? Color.web("#3DF6A3") : Color.web("#FD3D3D");
        circle.setStroke(strokeColor);
        circle.getStrokeDashArray().setAll(circumference);
        circle.setStrokeDashOffset(circumference);
    }

    private void createProgressAnimation(Circle circle, double circumference, double arcLength) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(circle.strokeDashOffsetProperty(),
                circumference - arcLength, Interpolator.EASE_BOTH);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(ANIMATION_DURATION), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void animateTrophyAppearance(ImageView trophyImage) {
        Customthread(trophyImage, TROPHY_ANIMATION_DURATION, TROPHY_ANIMATION_DELAY);
    }

    public static void Customthread(ImageView trophyImage, double trophyAnimationDuration, double trophyAnimationDelay) {
        Platform.runLater(() -> {
            trophyImage.setScaleX(0);
            trophyImage.setScaleY(0);

            Timeline trophyAnimation = new Timeline(
                    new KeyFrame(Duration.seconds(trophyAnimationDuration),
                            new KeyValue(trophyImage.scaleXProperty(), 1.0, Interpolator.EASE_OUT),
                            new KeyValue(trophyImage.scaleYProperty(), 1.0, Interpolator.EASE_OUT)
                    ));
            trophyAnimation.setDelay(Duration.seconds(trophyAnimationDelay));
            trophyAnimation.play();
        });
    }
}