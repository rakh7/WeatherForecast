package com.example.rakshithr.weatherforecast;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.communication.AerisCallback;
import com.hamweather.aeris.communication.EndpointType;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;
import com.hamweather.aeris.model.AerisResponse;
import com.google.android.gms.*;
import com.hamweather.aeris.tiles.AerisTile;

/**
 * Created by rakshithr on 12/8/15.
 */
public class MapFragment extends MapViewFragment implements
        OnAerisMapLongClickListener, AerisCallback {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_interactive_maps, container, false);

        mapView = (AerisMapView)view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapView.AerisMapType.GOOGLE);
        mapView.setOnAerisMapLongClickListener(this);
        mapView.addLayer(AerisTile.RADSAT);
        long lat = getArguments().getLong("lat");
        long lon = getArguments().getLong("lon");
        mapView.moveToLocation(new LatLng(lat, lon), 7);
        //mapView.loca
      //  String strtext = getArguments().getString("edttext");
        return view;
    }

    @Override
    public void onMapLongClick(double lat, double longitude) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }

    @Override
    public void onResult(EndpointType endpointType, AerisResponse aerisResponse) {

    }
}