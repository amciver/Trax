package com.experiment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.experiment.R;
import com.experiment.models.Matchup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/13/12
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchupStatsAdapter extends ArrayAdapter<String> {

    private List<String> _matchupStats;

    private Context localContext;

    public MatchupStatsAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);

        localContext = context;
        _matchupStats = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.matchup_stat, null);
        }

        String stat = _matchupStats.get(position);
        if(stat != null)
        {
            TextView s = (TextView)v.findViewById(R.id.stat);

            if(s != null)
                s.setText(stat);
        }

        return v;
    }
}
