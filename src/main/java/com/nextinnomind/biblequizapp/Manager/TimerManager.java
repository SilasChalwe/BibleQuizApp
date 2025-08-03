package com.nextinnomind.biblequizapp.Manager;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class TimerManager {
    private static final int TIMER_DURATION = 30;
    private Timeline timerTimeline;
    private Thread timerThread;
    private final AtomicBoolean timerActive = new AtomicBoolean(false);

    public void startTimer(Circle timerCircle, Label timerLabel, Runnable timeoutHandler) {
        stopTimer();
        timerActive.set(true);

        double radius = timerCircle.getRadius();
        double circleLength = 2 * Math.PI * radius;
        timerCircle.setStrokeDashOffset(0);

        // Visual timeline for circle animation
        timerTimeline = new Timeline();
        KeyValue kv = new KeyValue(timerCircle.strokeDashOffsetProperty(), circleLength);
        KeyFrame kf = new KeyFrame(Duration.seconds(TIMER_DURATION), kv);
        timerTimeline.getKeyFrames().add(kf);

        timerTimeline.setOnFinished(e -> {
            if (timerActive.get()) {
                timeoutHandler.run();
            }
        });

        // Countdown thread
        timerThread = new Thread(() -> {
            try {
                for (int i = TIMER_DURATION; i >= 0; i--) {
                    if (!timerActive.get()) return;

                    final int timeLeft = i;
                    Platform.runLater(() -> {
                        timerLabel.setText(String.valueOf(timeLeft));
                        updateTimerColor(timerCircle, timeLeft);
                    });
                    Thread.sleep(1000);
                }

                if (timerActive.get()) {
                    Platform.runLater(timeoutHandler);
                }
            } catch (InterruptedException ignored) {
                // Thread interrupted normally
            }
        });

        timerThread.setDaemon(true);
        timerThread.start();
        timerTimeline.play();
    }

    public void stopTimer() {
        timerActive.set(false);

        if (timerTimeline != null) {
            timerTimeline.stop();
            timerTimeline = null;
        }

        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
            timerThread = null;
        }
    }

    private void updateTimerColor(Circle timerCircle, int timeLeft) {
        if (timeLeft <= 10) {
            timerCircle.setStroke(Color.web("#FF4757")); // Critical red
        } else if (timeLeft <= 15) {
            timerCircle.setStroke(Color.web("#FFA726")); // Warning orange
        } else {
            timerCircle.setStroke(Color.web("#32D48E")); // Default Green
        }
    }

    public void resetTimerVisuals(Circle timerCircle, Label timerLabel) {
        Platform.runLater(() -> {
            timerLabel.setText(String.valueOf(TIMER_DURATION));
            timerCircle.setStrokeDashOffset(0);
            timerCircle.setStroke(Color.web("#FF6B6B"));
        });
    }
}