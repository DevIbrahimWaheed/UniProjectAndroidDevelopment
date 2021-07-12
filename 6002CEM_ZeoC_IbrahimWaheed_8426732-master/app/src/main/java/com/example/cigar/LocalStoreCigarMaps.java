package com.example.cigar;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.View;

import java.io.IOException;

public class LocalStoreCigarMaps extends FragmentActivity implements OnMapReadyCallback  {
    GoogleMap googleMap;
    FloatingActionButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_store_cigar_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        button = (FloatingActionButton)findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(LocalStoreCigarMaps.this , HomeActivity.class);
                startActivity(back);
                finish();

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMinZoomPreference(6.0f);
        LatLng Havana = new LatLng(52.19305, -1.70716);
        LatLng JohnSons = new LatLng(52.4818, -1.89714);
        LatLng CigarClub = new LatLng(51.63887, -0.47037);
        LatLng HolmesCubanCigars = new LatLng(52.89898, -1.27155);
        LatLng CigarUl = new LatLng(51.47902, -0.19345);
        LatLng  HunterFrankau = new LatLng(51.46705, -0.19542);
        // add makers
        googleMap.addMarker(new MarkerOptions().position(Havana).title("Havana House Lands Cigar Shop"));
        googleMap.addMarker(new MarkerOptions().position(JohnSons).title("John Hollingsworth & Son"));
        googleMap.addMarker(new MarkerOptions().position(CigarClub).title("The Cigar Club | Cigars UK"));
        googleMap.addMarker(new MarkerOptions().position(HolmesCubanCigars).title("Holmes Cuban Cigars"));
        googleMap.addMarker(new MarkerOptions().position(CigarUl).title("Cigars Unlimited"));
        googleMap.addMarker(new MarkerOptions().position(HunterFrankau).title("Hunters & Frankau Limited").infoWindowAnchor(1,1));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Havana));



    }
}