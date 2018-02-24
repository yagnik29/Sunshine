package com.example.yash.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.GetTempResponse;
import Model.Main;
import Webservices.ApiHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yash on 02-Jan-18.
 */

public class ForecastFragment extends Fragment {


    private List<Model.List> listTemp;

    private  ArrayAdapter<String> mForecastAdapter;

    ListView listView;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            callFetchTemp();

            return true;

        }
        return super.onOptionsItemSelected(item);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        final String[] forecastArray = {
                "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 48/63",
                "Weds - Sunny - 88/63",
                "Thurs - Heavy Rain- 88/63",
                "Fri - Rain - 88/63",
                "Sat - Cloudy - 88/63",
                "Sun - Sunny - 88/63"
        };


        /*List<String> weekForcast = new ArrayList<String>(Arrays.asList(forecastArray));

        mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forcast, R.id.list_item_forcast_textview, weekForcast);*/


        listView = rootView.findViewById(R.id.listview_forecast);
        /*listView.setAdapter(mForecastAdapter);*/

        callFetchTemp();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forcast = mForecastAdapter.getItem(i);

                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forcast);
                startActivity(intent);
            }
        });



        return rootView;
    }

    private void callFetchTemp() {

        ApiHandler.getApiService().getTempResponse(getTempMap(), new Callback<GetTempResponse>() {
            @Override
            public void success(GetTempResponse getTempResponse, Response response) {

                if (getTempResponse.getCod().equals("200")) {
                    Log.e("Yes", "true");

                    Log.e("test", String.valueOf(getTempResponse.getList().get(0).getMain().getTemp()));

                    listTemp = new ArrayList<>();
                    listTemp = getTempResponse.getList();

                } else if (getTempResponse.getCod().equals("404")) {
                    Toast.makeText(getActivity(), getTempResponse.getMessage().toString(), Toast.LENGTH_SHORT).show();

                }

                Log.e("ListTemp", String.valueOf(listTemp));


                Time dayTime = new Time();
                dayTime.setToNow();


                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                dayTime = new Time();

                String[] resultStrs = new String[listTemp.size()];

                for (int i = 0; i < listTemp.size(); i++) {
                    String day, description, highAndLow;
                    long dateTime;

                    dateTime = dayTime.setJulianDay(julianStartDay );
                    day = getReadableDateString(dateTime);

                    description = listTemp.get(i).getWeather().get(0).getDescription();

                    double high = listTemp.get(i).getMain().getTempMax();
                    double low = listTemp.get(i).getMain().getTempMin();

                    highAndLow = formatHighLows(high, low);

                    resultStrs[i] = listTemp.get(i).getDtTxt() + " - " + description + " - " + highAndLow;
                }

                for (String s : resultStrs) {
                    Log.e("Entry", "Forecast entry: " + s);
                }

                /*return resultStrs;*/



                mForecastAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forcast, R.id.list_item_forcast_textview, resultStrs);
                listView.setAdapter(mForecastAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Failed", String.valueOf(error));
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private String formatHighLows(double high, double low) {

        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;

    }

    private String getReadableDateString(long dateTime) {

        SimpleDateFormat shortDateFormat = new SimpleDateFormat("EEE MMM dd");

        return shortDateFormat.format(dateTime);
    }

    private Map<String, String> getTempMap() {
        Map<String, String> map = new HashMap<>();

        /*map.put("zip", "94043");*/
        map.put("q", "Ahmedabad,IN");
        map.put("mode", "json");
        map.put("units", "metric");
        map.put("appid", "3277cd3bdf4f5e347c993c7b5600470d");
        map.put("cnt", "7");


        Log.e("GetTempMap", "" + map);

        return map;
    }
}


