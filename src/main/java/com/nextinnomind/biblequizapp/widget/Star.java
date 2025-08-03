package com.nextinnomind.biblequizapp.widget;

import javafx.scene.shape.Polygon;
import org.jetbrains.annotations.NotNull;

public class Star {

    @NotNull
    public static Polygon createStar() {
        Polygon star = new Polygon();
        int spikes = 5;
        double innerRadius = 3.5;

        for (int i = 0; i < spikes * 2; i++) {
            double angle = Math.PI / spikes * i;
            double r = (i % 2 == 0) ? 7 : innerRadius;
            double x = 7 + Math.cos(angle - Math.PI / 2) * r;
            double y = 7 + Math.sin(angle - Math.PI / 2) * r;
            star.getPoints().addAll(x, y);
        }

        return star;
    }

}
