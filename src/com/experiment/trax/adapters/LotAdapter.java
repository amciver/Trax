package com.experiment.trax.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.experiment.R;
import com.experiment.trax.models.Location;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/4/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotAdapter extends ArrayAdapter<Location> {

    private List<Location> mLots;

    private Context mContext;

    public LotAdapter(Context context, int textViewResourceId, List<Location> objects) {
        super(context, textViewResourceId, objects);

        mContext = context;
        mLots = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.lot, null);
        }

        Location location = mLots.get(position);
        if (location != null) {
            TextView farm = (TextView) v.findViewById(R.id.farm);
            if (farm != null)
                farm.setText(location.getBusiness());

//            TextView phone = (TextView)v.findViewById(R.id.lot_phone);
//            if(phone != null)
//                phone.setText(location.getPhone());

            ImageView icon = (ImageView) v.findViewById(R.id.location);
            icon.setImageResource(R.drawable.ic_action_location);
//            if (icon != null) {
//                if (position == 0)
//                    icon.setImageResource(R.drawable.ic_green01);
//                if (position == 1)
//                    icon.setImageResource(R.drawable.ic_green02);
//                if (position == 2)
//                    icon.setImageResource(R.drawable.ic_green03);
//                if (position == 3)
//                    icon.setImageResource(R.drawable.ic_green04);
//                if (position == 4)
//                    icon.setImageResource(R.drawable.ic_green05);
//                if (position == 5)
//                    icon.setImageResource(R.drawable.ic_green06);
//                if (position == 6)
//                    icon.setImageResource(R.drawable.ic_green07);
//                if (position == 7)
//                    icon.setImageResource(R.drawable.ic_green08);
//                if (position == 8)
//                    icon.setImageResource(R.drawable.ic_green09);
//                if (position == 9)
//                    icon.setImageResource(R.drawable.ic_green10);
//            }

            ImageView amex = (ImageView) v.findViewById(R.id.lot_amex);
            if (amex != null) {
                if (location.isAcceptsAmex())
                    amex.setVisibility(View.VISIBLE);
                else
                    amex.setVisibility(View.GONE);
            }

            ImageView visa = (ImageView) v.findViewById(R.id.lot_visa);
            if (visa != null) {
                if (location.isAcceptsVisa())
                    visa.setVisibility(View.VISIBLE);
                else
                    visa.setVisibility(View.GONE);
            }

            ImageView mastercard = (ImageView) v.findViewById(R.id.lot_mastercard);
            if (mastercard != null) {
                if (location.isAcceptsMastercard())
                    mastercard.setVisibility(View.VISIBLE);
                else
                    mastercard.setVisibility(View.GONE);
            }

            ImageView discover = (ImageView) v.findViewById(R.id.lot_discover);
            if (discover != null) {
                if (location.isAcceptsDiscover())
                    discover.setVisibility(View.VISIBLE);
                else
                    discover.setVisibility(View.GONE);
            }

//            TextView lotName = (TextView)v.findViewById(R.id.lot_name);
//            if(lotName != null)
//                lotName.setText(location.getName());
        }
        return v;
    }
}
