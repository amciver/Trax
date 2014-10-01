package com.experiment.trax.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by amciver on 8/16/14.
 */
public final class Generator {
    private SecureRandom mRandom = new SecureRandom();

    public java.lang.String getRandomID(java.lang.String prefix) {
        return prefix + new BigInteger(130, mRandom).toString(32);
    }
}
