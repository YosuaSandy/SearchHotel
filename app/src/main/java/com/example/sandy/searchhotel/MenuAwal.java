package com.example.sandy.searchhotel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

public class MenuAwal extends AppCompatActivity  {

    private ProgressBar spinner;
    ImageView gambar1;
    Button b1,b2;
    GPSTracker gps;
    private static final String LOG_TAG = "PlacesAPIActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSION_REQUEST_CODE = 100;
    Double Radius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_awal);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.navigationbar);


        gambar1 = (ImageView) findViewById(R.id.icon_aplikasi);
        spinner = (ProgressBar) findViewById(R.id.progress);
        b1 = (Button) findViewById(R.id.button_search);
        b2 = (Button) findViewById(R.id.button_exit);
        spinner.setVisibility(View.GONE);
        Button btn2 = (Button) findViewById(R.id.button_exit);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                        System.exit(0);
                    }
                });
        Button btn1 = (Button) findViewById(R.id.button_search);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radius = 5000.0;
                Intent A = new Intent(MenuAwal.this, GoogleTempat.class);
                A.putExtra("jarak", Radius);
                startActivity(A);
                }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent B = new Intent(MenuAwal.this, About.class);
                startActivity(B);
                // User chose the "Settings" item, show the app settings UI...
                break;

//            case R.id.refresh:
//
//               break;
        }
        return true;
    }


}
