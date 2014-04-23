package com.experiment.trax.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by amciver on 4/12/14.
 */
public class Convert {

    public static int getAsDIP(int size, Resources res) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, res.getDisplayMetrics());
    }
}
