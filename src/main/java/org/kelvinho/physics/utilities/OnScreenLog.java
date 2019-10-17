package org.kelvinho.physics.utilities;

import org.kelvinho.physics.errors.MismatchArraySize;
import processing.core.PApplet;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"unused"})
public class OnScreenLog {
    private List<String> labels;
    private Location location;
    private PApplet sketch;
    private int width; // user sets this so that they can gauge what the width of the piece of text looks like

    public enum Location {TOP_RIGHT, TOP_LEFT, BOTTOM_RIGHT, BOTTOM_LEFT}

    public OnScreenLog(@Nonnull PApplet sketch, @Nonnull List<String> labels, @Nonnull Location location, int width) {
        this.sketch = sketch;
        this.labels = labels;
        this.location = location;
        this.width = width;
    }

    public void display(@Nonnull List<String> texts) {
        if (texts.size() != labels.size()) {
            throw new MismatchArraySize(labels, texts);
        }
        float x = 0, y = 0;
        int lineHeight = 20;
        switch (location) {
            case TOP_LEFT:
                x = 30;
                y = 30;
                break;
            case TOP_RIGHT:
                x = sketch.width - 30 - width;
                y = 30;
                break;
            case BOTTOM_LEFT:
                x = 30;
                y = sketch.height - lineHeight * labels.size();
                break;
            case BOTTOM_RIGHT:
                x = sketch.width - 30 - width;
                y = sketch.height - lineHeight * labels.size();
                break;
        }
        for (int i = 0; i < labels.size(); i++) {
            sketch.text(labels.get(i) + texts.get(i), x, y + i * lineHeight);
        }
    }

    public void display() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            list.add("");
        }
        display(list);
    }
}
