package org.kelvinho.physics.rayTracing.simple;

import processing.core.PApplet;
import processing.core.PVector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * This is just a bunch of cells working together to form a terrain, or a scenery
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Scenery {
    private HashSet<Cell> cells;
    private int width, height;
    private float displaySpace;

    private Scenery(@Nonnull ArrayList<Cell> cells, int width, int height, float displaySpace) {
        this.cells = new HashSet<>(cells);
        this.width = width;
        this.height = height;
        this.displaySpace = displaySpace;
    }

    public static Scenery simpleBox() {
        return new Scenery(cellRect(0, 0, 5, 3), 5, 5, 50);
    }

    public static Scenery biggerBoxWithObstacle() {
        ArrayList<Cell> cells = cellRect(0, 0, 7, 7);
        cells.add(new Cell(3, 3));
        return new Scenery(cells, 7, 7, 40);
    }

    public static ArrayList<Cell> cellRect(int x, int y, int width, int height) {
        ArrayList<Cell> answer = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            answer.add(new Cell(x + i, y));
            answer.add(new Cell(x + i, y + height - 1));
        }
        for (int i = 1; i < height - 1; i++) {
            answer.add(new Cell(x, y + i));
            answer.add(new Cell(x + width - 1, y + i));
        }
        return answer;
    }

    private void line(@Nonnull PApplet sketch, float x1, float y1, float x2, float y2) {
        sketch.line(x1 * displaySpace, y1 * displaySpace, x2 * displaySpace, y2 * displaySpace);
    }

    private void line(@Nonnull PApplet sketch, float x, float y, float angle) {
        sketch.line(x * displaySpace, y * displaySpace, (x + PApplet.cos(angle)) * displaySpace, (y + PApplet.sin(angle)) * displaySpace);
    }

    public void draw(@Nonnull PApplet sketch) {
        for (Cell cell : cells) {
            cell.draw(sketch, displaySpace);
        }
    }

    public void rayTrace(@Nonnull PApplet sketch, @Nonnull ViewPoint viewPoint) {
        int x = (int) Math.floor(viewPoint.getLocation().x);
        int y = (int) Math.floor(viewPoint.getLocation().y);
        float directionAngle = PApplet.atan2(viewPoint.getDirection().y, viewPoint.getDirection().x);
        float topRightAngle = PApplet.atan2(y + 1 - viewPoint.getLocation().y, x + 1 - viewPoint.getLocation().x);
        float topLeftAngle = PApplet.atan2(y + 1 - viewPoint.getLocation().y, x - viewPoint.getLocation().x);
        float bottomLeftAngle = PApplet.atan2(y - viewPoint.getLocation().y, x - viewPoint.getLocation().x);
        float bottomRightAngle = PApplet.atan2(y - viewPoint.getLocation().y, x + 1 - viewPoint.getLocation().x);
        line(sketch, viewPoint.getLocation().x, viewPoint.getLocation().y, directionAngle);
        line(sketch, viewPoint.getLocation().x, viewPoint.getLocation().y, topRightAngle);
        line(sketch, viewPoint.getLocation().x, viewPoint.getLocation().y, topLeftAngle);
        line(sketch, viewPoint.getLocation().x, viewPoint.getLocation().y, bottomLeftAngle);
        line(sketch, viewPoint.getLocation().x, viewPoint.getLocation().y, bottomRightAngle);
        int heading = 3; // 0 for bottom line, 1 for right line, 2 for top line, 3 for left line
        float[] angles = {bottomLeftAngle, bottomRightAngle, topRightAngle, topLeftAngle};
        for (int i = 0; i < angles.length - 1; i++) {
            if (directionAngle >= angles[i] && directionAngle < angles[i + 1]) {
                heading = i;
                break;
            }
        }
        sketch.ellipse(viewPoint.getLocation().x * displaySpace, viewPoint.getLocation().y * displaySpace, 20, 20);
    }

    public boolean inScenery(@Nonnull PVector location) {
        return (location.x >= 0 && location.x < width) && (location.y >= 0 && location.y < height);
    }
}
