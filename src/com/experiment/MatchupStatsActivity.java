package com.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.experiment.adapters.MatchupAdapter;
import com.experiment.adapters.MatchupStatsAdapter;
import com.experiment.models.Line;
import com.experiment.models.Matchup;
import com.experiment.services.IInteropsService;
import com.experiment.services.ImplInteropService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 12/13/12
 * Time: 11:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class MatchupStatsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.matchup_stats);
        updateStats();
    }

    private void updateStats()
    {
        Matchup matchup = null;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Bundle bundle = extras.getBundle("bundle");
            if(bundle != null) {
                matchup = (Matchup)bundle.getSerializable("matchup");

            }
        }

        if(matchup != null) {
            IInteropsService service = new ImplInteropService(this);
            Line l = service.getLine(matchup);

            List<String> stats = new ArrayList<String>();
            stats.add(l.getLine1());

            //MatchupStatsAdapter adapter = new MatchupStatsAdapter(this, android.R.layout.simple_list_item_1, stats);

            //ListView listView = (ListView)findViewById(R.id.matchupStatsListView);
            //listView.setEmptyView(findViewById(R.id.emptyView));
            //listView.setAdapter(adapter);

//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                    Intent intent = new Intent(MatchupsActivity.this, MatchupsActivity.class);
//
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("matchup", (Matchup)((ListView)adapterView).getItemAtPosition(i));
//                    intent.putExtra("bundle", bundle);
//
//                    MatchupsActivity.this.startActivity(intent);
//                }
//            });
        }
    }
}