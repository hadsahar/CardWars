package com.example.cardwars;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_Map extends Fragment  implements OnMapReadyCallback{
    private GoogleMap mMap;
    private LatLng firstPoint;

    public Fragment_Map(){

    }

    public Fragment_Map(LatLng firstPoint){
        setFirstPoint(firstPoint);
    }

    public void setFirstPoint(LatLng firstPoint) {
        this.firstPoint = firstPoint;
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rt = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return rt;
    }

    // set the map's camera on the best high scorer location when the map is initialized
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                    .position(this.firstPoint));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(this.firstPoint));
    }

    // Update the map location, showing the location where the
    // high scorer got his high score and moving the camera of the map to that location.
    public void updateMap(LatLng point) {
        if (mMap == null){
            Log.d("pttt","mMap is null");
            return;
        }
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(point).title(getString(R.string.marker_msg)));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(point).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}