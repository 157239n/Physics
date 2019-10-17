package org.kelvinho.physics.mirror;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Config {
    public static final float EPSILON = 0.000001f;
    public static final float RELATIVE_EPSILON = 0.01f;

    public static boolean equals(float a, float b) {
        return Math.abs(a - b) < a * RELATIVE_EPSILON;
    }
}
