package com.experiment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

public class InitActivity extends Activity
{
    private Resources resources;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //using this to get local resources for testing, comment out for production
        //resources = getResources();
    }

    public void onClick(View v)
    {
        //ImplSFTCService service = new ImplSFTCService();
        //ArrayList<Matchup> matchups = service.getMatchups(2012, 8, 12, resources);

        Intent myIntent = new Intent(InitActivity.this, MatchupsActivity.class);
        startActivity(myIntent);

        //new DownloadFilesTask().execute(5,6,7,8,8,67,45,45,5,5);
    }

    private void setAmount(Integer value)
    {

        if(value.intValue() > 0)
            Toast.makeText(this, "Progress is " + value.intValue(), Toast.LENGTH_SHORT).show();

        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);


    }

    private class DownloadFilesTask extends AsyncTask<Integer, Integer, Long> {
    protected Long doInBackground(Integer... numbers) {
        try{Thread.sleep(1000);    }catch (InterruptedException e) {}
        int count = numbers.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            totalSize += numbers[i];
            publishProgress((int) ((i / (float) count) * 100));
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
        setAmount(progress[0]);
    }

    protected void onPostExecute(Long result) {
        //Toast.makeText("All done!");
        //showDialog("Downloaded " + result + " bytes");
    }
}
}
