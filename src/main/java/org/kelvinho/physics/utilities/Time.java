package org.kelvinho.physics.utilities;

/**
 * Represents the time problem usually encountered in these simulations.
 */
@SuppressWarnings("unused")
public class Time {
    private float timeRatio;
    private int iterations;
    private float timeStep;
    private float timePerFrame;

    /**
     * The systems that control the update time should update (iterations) time in 1 draw() pass, and each iteration update for getTimeStep() time.
     * Any analysis tools, like graphing or logging, should happen each frame, or 1 pass over draw(), and each frame's time is getTimePerFrame().
     *
     * @param timeRatio  how much 1 second in the real world correspond to in the simulated world?
     * @param iterations how much iterations in 1 second in the real world? The larger, the smoother the physics
     */
    public Time(float timeRatio, int iterations) {
        this.timeRatio = timeRatio;
        this.iterations = iterations;
        this.timeStep = timeRatio / (iterations * 30.0f);// 30.0f here is for the typical frameRate.
        this.timePerFrame = timeRatio / 30.0f;
    }

    public float getTimeStep() {
        return timeStep;
    }

    public float getTimePerFrame() {
        return timePerFrame;
    }

    public int getIterations() {
        return iterations;
    }

    public float getTimeRatio() {
        return timeRatio;
    }
}
