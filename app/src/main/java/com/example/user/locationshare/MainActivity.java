package com.example.user.locationshare;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener

{
    TextView tvLocation;
    Button btnShare;
    GoogleApiClient gac;
    Location loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocation = findViewById(R.id.tvLocation);
        btnShare= findViewById(R.id.btnShare);
        btnShare.setEnabled(false);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = tvLocation.getText().toString();
                Intent i =new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,""+s);
                startActivity(i);

            }
        });

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addOnConnectionFailedListener(this);
        builder.addConnectionCallbacks(this);
        builder.addApi(LocationServices.API);

        gac = builder.build();

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        if(gac != null)
            gac.connect();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if(gac != null)
            gac.disconnect();
        btnShare.setEnabled(false);
    }


    @Override
    public void onConnected(Bundle bundle) {
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc != null)
        {
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            String msg = "lat" + lat + "lon" + lon;

            Geocoder g= new Geocoder(MainActivity.this,Locale.ENGLISH);

            try{
                List<Address> addressList = g.getFromLocation(lat,lon,1);
                Address address =addressList.get(0);
                String add = address.getCountryName()
                        + " "+ address.getAdminArea()
                        +" "+ address.getSubAdminArea()
                        +" "+ address.getLocality()
                        +" "+address.getSubLocality()
                        +" "+address.getThoroughfare()
                        +" "+address.getSubThoroughfare()
                        +" "+address.getPostalCode();

                tvLocation.setText(add);
            }
            catch (IOException e)
            {
                Toast.makeText(this, "issue", Toast.LENGTH_SHORT).show();
            }
            btnShare.setEnabled(true);
        }
        else
        {
            Toast.makeText(this, "pls enable GPS", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
