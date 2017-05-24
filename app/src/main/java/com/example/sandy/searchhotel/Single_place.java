package com.example.sandy.searchhotel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class Single_place extends AppCompatActivity {
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    // Alert Dialog Manager
    Peringatan alert = new Peringatan();

    // Google Places
    GooglePlaces googlePlaces;

    // Place Details
    PlaceDetail placeDetails;

    // Progress dialog
    ProgressDialog pDialog;

    Button b1;

    GPSTracker gps2;

    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_place);
        setTitle("Detail Penginapan");

        Intent i = getIntent();

        b1 = (Button) findViewById(R.id.track);

        // Place referece id
        final String reference = i.getStringExtra(KEY_REFERENCE);

        // Calling a Async Background thread
        new LoadSinglePlaceDetails().execute(reference);


       /* gps2 = new GPSTracker (this);
        Double latitude = gps2.getLatitude();
        Double longitude = gps2.getLongitude();

        Double latitude_tujuan = placeDetails.result.geometry.location.lat;
        Double longitude_tujuan = placeDetails.result.geometry.location.lng;

        Log.d("flag", "put2 " + placeDetails.result.geometry.location.lat);

        LatLng posisi_awal = new LatLng(latitude,longitude);
        LatLng posisi_akhir = new LatLng(latitude_tujuan,longitude_tujuan);

        String jarak = Double.toString(CalculationByDistance(posisi_awal,posisi_akhir));
       TextView result_jarak = (TextView)findViewById(R.id.jarak);
        result_jarak.setText(jarak);*/

        Button btn1 = (Button) findViewById(R.id.track);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent A = new Intent(Single_place.this, Tracker.class);
                Bundle b = new Bundle();
                b.putDouble("Longitude", placeDetails.result.geometry.location.lng);
                b.putDouble("Latitude", placeDetails.result.geometry.location.lat);
                b.putString("Nama", placeDetails.result.name);
                Log.d("flag", "put " + placeDetails.result.geometry.location.lat);
                A.putExtras(b);
                startActivity(A);
            }

        });
    }

    /**
     * Background Async Task to Load Google places
     * */
    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Single_place.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];

            // creating Places class object
            googlePlaces = new GooglePlaces();

            // Check if used is connected to Internet
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(final String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    if(placeDetails != null){
                        String status = placeDetails.status;

                        // check place deatils status
                        // Check for all possible status
                        if(status.equals("OK")){
                            if (placeDetails.result != null) {
                                gps2 = new GPSTracker (Single_place.this);
                                Double latitude_awal = gps2.getLatitude();
                                Double longitude_awal = gps2.getLongitude();

                                String name = placeDetails.result.name;
                                String id = placeDetails.result.icon;
                                String address = placeDetails.result.formatted_address;
                                String phone = placeDetails.result.formatted_phone_number;
                                String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                String longitude = Double.toString(placeDetails.result.geometry.location.lng);


                                Location posisi_awal = new Location("point A");

                                posisi_awal.setLatitude(latitude_awal);
                                posisi_awal.setLongitude(longitude_awal);

                                Location posisi_akhir = new Location("point B");

                                posisi_akhir.setLatitude(placeDetails.result.geometry.location.lat );
                                posisi_akhir.setLongitude(placeDetails.result.geometry.location.lng );

                                float jarak = posisi_awal.distanceTo(posisi_akhir) / 1000;

                                DecimalFormat  df = new DecimalFormat("#.##");

                               String jarak2 = df.format(jarak);

                                Log.d("Place ", id + name + address + phone + latitude + longitude  );

                                // Displaying all the details in the view
                                // single_place.xml
                                TextView lbl_name = (TextView) findViewById(R.id.name);
                                TextView lbl_address = (TextView) findViewById(R.id.address);
                                TextView lbl_phone = (TextView) findViewById(R.id.phone);
                                // TextView lbl_location = (TextView) findViewById(R.id.location);
                                TextView result_jarak = (TextView)findViewById(R.id.jarak);



                                // Check for null data from google
                                // Sometimes place details might missing
                                name = name == null ? "Not present" : name; // if name is null display as "Not present"
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;
                                latitude = latitude == null ? "Not present" : latitude;
                                longitude = longitude == null ? "Not present" : longitude;

                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                               // lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                                result_jarak.setText(Html.fromHtml(" <b>Jarak: </b> " + jarak2 + "Km"));

                            }


                        }
                        else if(status.equals("ZERO_RESULTS")){
                            alert.showAlertDialog(Single_place.this, "Near Places",
                                    "Sorry no place found.",
                                    false);
                        }
                        else if(status.equals("UNKNOWN_ERROR"))
                        {
                            alert.showAlertDialog(Single_place.this, "Places Error",
                                    "Sorry unknown error occured.",
                                    false);
                        }
                        else if(status.equals("OVER_QUERY_LIMIT"))
                        {
                            alert.showAlertDialog(Single_place.this, "Places Error",
                                    "Sorry query limit to google places is reached",
                                    false);
                        }
                        else if(status.equals("REQUEST_DENIED"))
                        {
                            alert.showAlertDialog(Single_place.this, "Places Error",
                                    "Sorry error occured. Request is denied",
                                    false);
                        }
                        else if(status.equals("INVALID_REQUEST"))
                        {
                            alert.showAlertDialog(Single_place.this, "Places Error",
                                    "Sorry error occured. Invalid Request",
                                    false);
                        }
                        else
                        {
                            alert.showAlertDialog(Single_place.this, "Places Error",
                                    "Sorry error occured.",
                                    false);
                        }
                    }else{
                        alert.showAlertDialog(Single_place.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }


                }
            });

        }

    }
}
