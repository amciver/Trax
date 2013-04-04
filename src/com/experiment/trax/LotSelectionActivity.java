package com.experiment.trax;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 2/17/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotSelectionActivity extends SherlockFragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: this will provide a different font but currently not vertically centered
        String font = "billabong";
        SpannableString s = new SpannableString(getApplicationContext().getString(R.string.app_name));
        s.setSpan(new com.experiment.trax.utils.TypefaceSpan(this, font), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setTitle(s);

        setContentView(R.layout.lot_selection);
    }
}