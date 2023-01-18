package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class EarthquackeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Modu>> {
    private EarthquakeAdapter mAdapter;
    private TextView mEmptyStateTextView;

    private static final String USGS_REQUEST_DATA =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Modu>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Modu currentModu = mAdapter.getItem(position);
                Uri earthQuakeUri = Uri.parse(currentModu.getmUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,earthQuakeUri);
                startActivity(webIntent);
            }
        });
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if (networkInfo!=null&&networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }
    @Override
    public Loader<List<Modu>> onCreateLoader(int i, Bundle bundle){
        return new EarthquakeLoader(this, USGS_REQUEST_DATA);
    }

    @Override
    public void onLoadFinished(Loader<List<Modu>> loader, List<Modu> modus) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_data);
        mAdapter.clear();
        if (modus!=null&&!modus.isEmpty()){
            mAdapter.addAll(modus);
        }
    }
    @Override
    public void onLoaderReset(Loader<List<Modu>> loader){
        mAdapter.clear();
    }
}