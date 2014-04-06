package com.experiment.trax.utils;

/*
 * Copyright 2013 Simple Finance Corporation. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.TypedValue;
import com.experiment.trax.TraxApplication;

/**
 * Style a {@link android.text.Spannable} with a custom {@link Typeface}.
 *
 * @author Tristan Waddington
 */
public class TypefaceSpan extends MetricAffectingSpan {

    private Typeface mTypeface;

    /**
     * Load the {@link Typeface} and apply to a {@link android.text.Spannable}.
     */
    public TypefaceSpan(java.lang.String typefaceName) {
        TraxApplication traxApplication = TraxApplication.INSTANCE;
        mTypeface = traxApplication.getTypeface(typefaceName);
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTextSize(getPixelSize(24));
        p.setTypeface(mTypeface);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTextSize(getPixelSize(24));
        tp.setTypeface(mTypeface);
    }

    //http://stackoverflow.com/questions/3061930/how-to-set-unit-for-paint-settextsize
    private float getPixelSize(int dipSize) {

        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipSize, TraxApplication.INSTANCE.getResources().getDisplayMetrics());
    }
}