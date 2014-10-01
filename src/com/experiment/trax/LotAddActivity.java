package com.experiment.trax;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import com.actionbarsherlock.view.Window;
import com.experiment.trax.core.BaseActivity;
import com.experiment.trax.listeners.AddLotLocationCompleteListener;
import com.experiment.trax.models.LotLocation;
import com.experiment.trax.services.ImplSimpleDBService;

/**
 * Created by amciver on 4/26/14.
 */
public class LotAddActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {

        //this must be done before we add anything other UI component
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.lot_add);
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
//        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
//        inflater.inflate(R.menu.lot_add_menu, menu);
        return true;
    }

    public boolean onPersistLot(com.actionbarsherlock.view.MenuItem item) {

        Log.d("LotAddActivity", "onPersistLot called");
        return true;
    }

    public void onPersistLot(View view) {

        Log.d("LotAddActivity", "onPersistLot called by View " + view);

        final TextView business = (TextView) this.findViewById(R.id.add_lot_business);
        business.setError(null);
        final TextView landmark = (TextView) this.findViewById(R.id.add_lot_landmark);
        landmark.setError(null);

        Log.d("LotAddActivity", "Clearing errors for new persistence analysis");

        boolean canProceed = true;


        if (business.getText().toString() == null || business.getText().toString().isEmpty()) {
            canProceed = false;
            business.setError("Business Name is required (example: Mast-Roth Farms)");
        }

        if (landmark.getText().toString() == null || landmark.getText().toString().isEmpty()) {
            canProceed = false;
            landmark.setError("Landmark is required (example: Paradise Valley Mall)");
        }

        if (canProceed) {

            final ProgressDialog progressDialog = ProgressDialog.show(this, "Please wait...", "Adding lot...");

            enableView(true);

            LotLocation location = new LotLocation();

            location.setBusiness(business.getText().toString());
            location.setLocation(landmark.getText().toString());

            final TextView crossStreets = (TextView) this.findViewById(R.id.add_lot_cross);
            location.setDescription(crossStreets.getText().toString());

            final TextView phone = (TextView) this.findViewById(R.id.add_lot_phone);
            location.setPhone(phone.getText().toString());

            final CheckBox amex = (CheckBox) this.findViewById(R.id.add_lot_amex);
            location.setAcceptsAmex(amex.isChecked());

            final CheckBox visa = (CheckBox) this.findViewById(R.id.add_lot_visa);
            location.setAcceptsVisa(visa.isChecked());

            final CheckBox mc = (CheckBox) this.findViewById(R.id.add_lot_mastercard);
            location.setAcceptsMastercard(mc.isChecked());

            final CheckBox discover = (CheckBox) this.findViewById(R.id.add_lot_discover);
            location.setAcceptsDiscover(discover.isChecked());

            ImplSimpleDBService srvc = new ImplSimpleDBService();
            srvc.setOnAddLocationCompleteListener(new AddLotLocationCompleteListener() {

                @Override
                public void onLotLocationAddComplete(Boolean result) {

                    progressDialog.dismiss();
                    enableView(false);

                    finish();
                }
            });

            srvc.addLotLocationAsync(this, location);
        }
    }

    private void enableView(boolean value) {


        View view = this.findViewById(R.id.add_lot_root);
        view.setClickable(!value);

        setSupportProgressBarIndeterminate(value);
        setProgressBarIndeterminateVisibility(value);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return true;
//    }
}