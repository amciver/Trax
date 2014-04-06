package com.experiment.trax;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.Spannable;
import android.text.SpannableString;
import com.experiment.trax.services.AWSClientManager;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/24/13
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TraxApplication extends Application {

    public final String PREFS = "traxPrefs";

    //This font should not include extension and should be a .ttf font placed
    //within assets/fonts folder
    private final String FONT = "kayleigh";

    private Typeface mTypeface;
    private static LruCache<String, Typeface> sTypefaceCache =
            new LruCache<java.lang.String, Typeface>(12);

    public static TraxApplication INSTANCE = null;
    public static AWSClientManager CLIENT_MANAGER = null;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        CLIENT_MANAGER = new AWSClientManager(getSharedPreferences(
                "com.experiment.trax", Context.MODE_PRIVATE));
    }

    /*
    Return the application title in a given font size and font.
     */
    public CharSequence getTitle() {

        SpannableString s = new SpannableString(getApplicationContext().getString(R.string.app_name));
        s.setSpan(new com.experiment.trax.utils.TypefaceSpan(FONT), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    /*
    Returns a CharSequence with the passed in typefaceName applied to it. If no typefaceName is passed in, the
     default application typeface will be used.
     */
    public CharSequence getTextAs(CharSequence text, String typefaceName) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new com.experiment.trax.utils.TypefaceSpan(typefaceName), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return s;
    }

    /*
    Returns a Typeface instance based on the past in name of the typeface. If no typefaceName is passed in, the
     default application typeface will be used.
     */
    public Typeface getTypeface(String typefaceName) {

        //if someone doesn't pass in a typefaceName, give them the default application typeface
        if (typefaceName.isEmpty()) {
            typefaceName = FONT;
        }

        mTypeface = sTypefaceCache.get(typefaceName);

        if (mTypeface == null) {
            mTypeface = Typeface.createFromAsset(this.getApplicationContext()
                    .getAssets(), java.lang.String.format("fonts/%s.ttf", typefaceName));

            //cache the loaded Typeface
            sTypefaceCache.put(typefaceName, mTypeface);
        }
        return mTypeface;

    }
}

