package org.kelvinho.physics.rayTracing.simple;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Cell {
    private int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            Cell cell = (Cell) obj;
            return cell.x == this.x && cell.y == this.y;
        } else {
            return super.equals(obj);
        }
    }

    private void line(@Nonnull PApplet sketch, float x1, float y1, float x2, float y2, float space) {
        sketch.line(x1 * space, y1 * space, x2 * space, y2 * space);
    }

    public void draw(@Nonnull PApplet sketch, float space) {
        line(sketch, x, y, x + 1, y, space);
        line(sketch, x, y, x, y + 1, space);
        line(sketch, x + 1, y, x + 1, y + 1, space);
        line(sketch, x, y + 1, x + 1, y + 1, space);
    }

    public boolean inside(@Nonnull PVector point) {
        return point.x >= x && point.x < x + 1 && point.y >= y && point.y < y + 1;
    }
}
