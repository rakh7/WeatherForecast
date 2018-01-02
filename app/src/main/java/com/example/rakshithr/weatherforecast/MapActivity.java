package com.example.rakshithr.weatherforecast;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.AerisEngine;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.communication.parameter.PlaceParameter;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.model.AerisResponse;

/**
 * Created by rakshithr on 11/23/15.
 */
public class MapActivity extends Activity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_interactive_maps);
        Bundle bundle = getIntent().getExtras();
        long lat = bundle.getLong("latitude"),lon = bundle.getLong("longitude");
        Bundle bundles = new Bundle();
        bundles.putLong("lat", lat);
        bundles.putLong("lon", lon);

        // setting up secret key and client id for oauth to aeris
        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);
        PlaceParameter place = new PlaceParameter(lat,lon);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapFragment map = new MapFragment();
        map.setArguments(bundles);
        fragmentTransaction.replace(R.id.aerisfragment_map, map).commit();

    }
}
