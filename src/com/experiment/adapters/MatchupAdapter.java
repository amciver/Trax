package com.experiment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.experiment.R;
import com.experiment.models.Matchup;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/6/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchupAdapter extends ArrayAdapter<Matchup> {

    private List<Matchup> _matchups;

    private Context localContext;

    public MatchupAdapter(Context context, int textViewResourceId, List<Matchup> objects) {
        super(context, textViewResourceId, objects);

        localContext = context;
        _matchups = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)localContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.matchup, null);
        }

        Matchup matchup = _matchups.get(position);
        if(matchup != null)
        {
            TextView description = (TextView)v.findViewById(R.id.description);
            if(description != null)
                description.setText(matchup.getDescription());

            ImageView image = (ImageView)v.findViewById(R.id.sport);
            if(image != null)
            {
                switch (matchup.getCategory()) {
                    case NFL:
                        image.setImageResource(R.drawable.football);
                        break;
                    case NCB:
                        image.setImageResource(R.drawable.basketball);
                        break;
                    case NCW:
                        image.setImageResource(R.drawable.basketball);
                        break;
                    case Soccer:
                        image.setImageResource(R.drawable.soccer);
                        break;
                    case NBA:
                        image.setImageResource(R.drawable.basketball);
                        break;
                    case W_Vol:
                        image.setImageResource(R.drawable.volleyball);
                        break;
                    case M_Vol:
                        image.setImageResource(R.drawable.volleyball);
                        break;
                    case Unknown:
                        image.setImageResource(R.drawable.question);
                        break;
                }
            }

            TextView entry1 = (TextView)v.findViewById(R.id.entry1);
            TextView entry2 = (TextView)v.findViewById(R.id.entry2);

            if(entry1 != null)
                entry1.setText(matchup.getEntry1().getOpponent());

            if(entry2 != null)
                entry2.setText(matchup.getEntry2().getOpponent());
        }

        return v;
    }
}
