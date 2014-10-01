package com.experiment.trax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.experiment.trax.R;
import com.experiment.trax.models.LotLocation;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 1/4/13
 * Time: 10:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class LotAdapter extends ArrayAdapter<LotLocation> {

    private List<LotLocation> mLots;

    private Context mContext;

    public LotAdapter(Context context, int textViewResourceId, List<LotLocation> objects) {
        super(context, textViewResourceId, objects);

        mContext = context;
        mLots = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("LotAdapter", "getView called for position [" + position + "]");

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.lot, null);
        }

        LotLocation lotLocation = mLots.get(position);
        if (lotLocation != null) {
            TextView location = (TextView) v.findViewById(R.id.lot_location);
            if (location != null)
                location.setText(lotLocation.getLocation());

//            RatingBar rating = (RatingBar) v.findViewById(R.id.lot_ratingBar);
//            if (rating != null)
//                rating.setRating(location.getRating());

            showPhone(v, lotLocation);

//            ImageView icon = (ImageView) v.findViewById(R.id.lot_location);
//            icon.setImageResource(R.drawable.ic_action_location);

            showHours(v, lotLocation);
            showPaymentMethods(v, lotLocation);
        }
        return v;
    }

    private void showName(View v, LotLocation lotLocation) {
        TextView location = (TextView) v.findViewById(R.id.lot_location);
        if (location != null) {
            if (lotLocation.getLocation() != null &&
                    lotLocation.getLocation().length() != 0) {
                location.setVisibility(View.VISIBLE);
                location.setText(lotLocation.getLocation());
            } else
                location.setVisibility(View.GONE);
        }
    }

    private void showPhone(View v, LotLocation lotLocation) {
//        TextView phone = (TextView) v.findViewById(R.id.lot_phone);
//        if (phone != null) {
//            if (location.getPhone() != null && !location.getPhone().isEmpty())
//                phone.setText(com.experiment.trax.utils.String.formatPhoneNumber(location.getPhone(), "-"));
//            else
//                phone.setText(R.string.not_available);
//        }
    }

    private void showHours(View v, LotLocation lotLocation) {

        int day = DateTime.now().getDayOfWeek();
        String openingTime = lotLocation.getOpeningTime(day);
        String closingTime = lotLocation.getClosingTime(day);
        if (openingTime == null ||
                openingTime.length() == 0 ||
                closingTime == null ||
                closingTime.length() == 0) {

            //we have no hours, hide items
            TextView open = (TextView) v.findViewById(R.id.lot_hours_open);
            if (open != null) {
                open.setVisibility(View.VISIBLE);
                open.setText(getContext().getString(R.string.not_available));
            }

            TextView seperator = (TextView) v.findViewById(R.id.lot_time_seperator);
            if (seperator != null)
                seperator.setVisibility(View.GONE);

            TextView close = (TextView) v.findViewById(R.id.lot_hours_close);
            if (close != null) {
                close.setVisibility(View.GONE);
            }
        } else {

            //we have hours of operation, make sure the seperator is there
            TextView seperator = (TextView) v.findViewById(R.id.lot_time_seperator);
            seperator.setVisibility(View.VISIBLE);

            TextView open = (TextView) v.findViewById(R.id.lot_hours_open);
            if (open != null) {
                open.setVisibility(View.VISIBLE);
                open.setText(openingTime);
            }

            TextView close = (TextView) v.findViewById(R.id.lot_hours_close);
            if (close != null) {
                close.setVisibility(View.VISIBLE);
                close.setText(closingTime);
            }
        }
    }

    private void showPaymentMethods(View v, LotLocation lotLocation) {
        ImageView amex = (ImageView) v.findViewById(R.id.lot_amex);
        if (amex != null) {
            if (!lotLocation.isAcceptsAmex())
                amex.setVisibility(View.GONE);
            else
                amex.setVisibility(View.VISIBLE);
        }

        ImageView visa = (ImageView) v.findViewById(R.id.lot_visa);
        if (visa != null) {
            if (!lotLocation.isAcceptsVisa())
                visa.setVisibility(View.GONE);
            else
                visa.setVisibility(View.VISIBLE);
        }

        ImageView mastercard = (ImageView) v.findViewById(R.id.lot_mastercard);
        if (mastercard != null) {
            if (!lotLocation.isAcceptsMastercard())
                mastercard.setVisibility(View.GONE);
            else
                mastercard.setVisibility(View.VISIBLE);
        }

        ImageView discover = (ImageView) v.findViewById(R.id.lot_discover);
        if (discover != null) {
            if (!lotLocation.isAcceptsDiscover())
                discover.setVisibility(View.GONE);
            else
                discover.setVisibility(View.VISIBLE);
        }
    }
}
