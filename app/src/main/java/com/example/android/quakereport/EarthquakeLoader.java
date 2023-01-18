package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Modu>> {
    private String mUrl;
    /**
     * @param context
     * @deprecated
     */
    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Override
    public List<Modu> loadInBackground() {
        if (mUrl==null){
        return null;
        }
        List<Modu> earthquake = QueryUtils.fetchEarthquakeData(mUrl);
        return earthquake;
    }
}
