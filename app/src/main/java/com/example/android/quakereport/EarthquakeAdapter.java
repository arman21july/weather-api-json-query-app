package com.example.android.quakereport;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;


public class EarthquakeAdapter extends ArrayAdapter<Modu> {
    private static final String PLACE_SEPARATOR = " of ";
    public EarthquakeAdapter(Context context, List<Modu> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquack_list_item, parent, false);
        }

        Modu currentModu = getItem(position);
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(currentModu.getmMagnitude());
        magnitudeView.setText(formattedMagnitude);

        GradientDrawable magCir = (GradientDrawable) magnitudeView.getBackground();
        int magColor = getMagColor (currentModu.getmMagnitude());
        magCir.setColor(magColor);

        String originalPlace = currentModu.getmPlace();
        String primaryPlace;
        String offsetPlace;
        if (originalPlace.contains(PLACE_SEPARATOR)){
            String[] parts = originalPlace.split(PLACE_SEPARATOR);
            offsetPlace = parts[0] + PLACE_SEPARATOR;
            primaryPlace  = parts[1];
        }
        else {
            offsetPlace = getContext().getString(R.string.near_the);
            primaryPlace = originalPlace;
        }

        TextView primaryPlaceView = (TextView) listItemView.findViewById(R.id.primary_place);
        primaryPlaceView.setText(primaryPlace);

        TextView offsetPlaceView = (TextView) listItemView.findViewById(R.id.offset_place);
        offsetPlaceView.setText(offsetPlace);
        primaryPlaceView.setText(primaryPlace);

        Date dateObject = new Date(currentModu.getmMilliSecond());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);
        return listItemView;
    }
    private int getMagColor(double magnitude){
        int magColResId;
        int magFloor = (int) Math.floor(magnitude);
        switch (magFloor){
            case 0:
            case 1:
                magColResId = R.color.magnitude1;
                break;
            case 2:
                magColResId = R.color.magnitude2;
                break;
            case 3:
                magColResId = R.color.magnitude3;
                break;
            case 4:
                magColResId = R.color.magnitude4;
                break;
            case 5:
                magColResId = R.color.magnitude5;
                break;
            case 6:
                magColResId = R.color.magnitude6;
                break;
            case 7:
                magColResId = R.color.magnitude7;
                break;
            case 8:
                magColResId = R.color.magnitude8;
                break;
            case 9:
                magColResId = R.color.magnitude9;
                break;
            default:
                magColResId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),magColResId);
    }

    public String formatDate(Date dateObject){
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    public String formatTime(Date dateObject){
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    public String formatMagnitude(double magnitude){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
}
