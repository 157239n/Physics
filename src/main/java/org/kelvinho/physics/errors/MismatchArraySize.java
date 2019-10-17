package org.kelvinho.physics.errors;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings("unused")
public class MismatchArraySize extends RuntimeException {
    public MismatchArraySize(@Nonnull List a, @Nonnull List b) {
        super("First array contains " + a.size() + " while second array contains " + b.size());
    }
}
