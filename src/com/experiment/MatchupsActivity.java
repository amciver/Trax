package com.experiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.experiment.adapters.MatchupAdapter;
import com.experiment.models.Line;
import com.experiment.models.Matchup;
import com.experiment.services.*;

import java.io.Console;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amciver
 * Date: 9/20/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchupsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.matchups);
        updateMatchups();
    }

    private void updateMatchups() {

        ISFTCService service = new ImplSFTCService();
        List<Matchup> matchups = service.getMatchups(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH) + 1,
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        MatchupAdapter adapter = new MatchupAdapter(this, android.R.layout.simple_list_item_1, matchups);

        ListView listView = (ListView)findViewById(R.id.matchupListView);
        listView.setEmptyView(findViewById(R.id.emptyView));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MatchupsActivity.this, MatchupStatsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("matchup", (Matchup)((ListView)adapterView).getItemAtPosition(i));
                intent.putExtra("bundle", bundle);

                startActivity(intent);
            }
        });
    }
}