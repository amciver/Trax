package com.experiment.trax;

import android.os.Bundle;
import com.experiment.trax.core.BaseActivity;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/19/13
 * Time: 10:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotDetailsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lot_details);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout, container, false);
//    }

//    @Override
//    public boolean onContextItemSelected(MenuItem aItem) {
//        Log.d("MakingCall", "Im in oncontextitemselected");
//        //AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem.getMenuInfo();
//        return false;
//    }
}