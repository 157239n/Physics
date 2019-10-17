package org.kelvinho.physics.mirror;

import processing.core.PVector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is for manipulating 2x2 matrices only. Kind of a simple matrix to get the job done. It is arranged like so:
 * a b
 * c d
 */
@SuppressWarnings({"unused", "WeakerAccess"})
class Matrix {
    private float a, b, c, d;

    /**
     * Creates a matrix with these indices:
     * a b
     * c d
     * */
    Matrix(float a, float b, float c, float d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    /**
     * This concatenates 2 vectors. Say (a, c) and (b, d), this will return the matrix
     * a b
     * c d
     */
    Matrix(@Nonnull PVector a, @Nonnull PVector b) {
        this.a = a.x;
        this.b = b.x;
        this.c = a.y;
        this.d = b.y;
    }

    PVector operate(@Nonnull PVector vector) {
        return new PVector(a * vector.x + b * vector.y, c * vector.x + d * vector.y);
    }

    private float determinant() {
        return a * d - b * c;
    }

    @Nullable
    Matrix inverse() {
        float determinant = determinant();
        if (Config.equals(determinant, 0)) {
            return null;
        }
        return new Matrix(d / determinant, -b / determinant, -c / determinant, a / determinant);
    }
}
