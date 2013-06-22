package com.experiment.trax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.experiment.trax.R;
import com.experiment.trax.models.Type;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 5/8/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TypeAdapter extends ArrayAdapter<Type> {

    private List<Type> mTypes;
    private Context mContext;

    public TypeAdapter(Context context, int textViewResourceId, List<Type> objects) {
        super(context, textViewResourceId, objects);

        mContext = context;
        mTypes = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("TypeAdapter", "getView called for position [" + position + "]");

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.tree_type, null);
        }

        Type type = mTypes.get(position);
        if (type != null) {
            TextView name = (TextView) v.findViewById(R.id.type_name);
            if (name != null)
                name.setText(type.getName());

            TextView description = (TextView) v.findViewById(R.id.type_description);
            if (description != null)
                description.setText(type.getDescription());
        }
        return v;
    }
}
