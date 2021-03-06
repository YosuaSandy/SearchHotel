package com.example.sandy.searchhotel;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class GoogleTempat extends AppCompatActivity {
    // flag for Internet connection status
    Boolean isInternetPresent = false;

    // Connection detector class
    ConnectionDetector cd;

    // Alert Dialog Manager
    Peringatan alert = new Peringatan();

    // Google Places
    GooglePlaces googlePlaces;

    // Places List
    PlaceList nearPlaces;

    // GPS Location
    GPSTracker gps;

    // Button
    Button btnShowOnMap;

    // Progress dialog
    ProgressDialog pDialog;

    // Places Listview
    ListView lv;

    Double Radius;

    // ListItems data
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();


    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_tempat);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.navigationbar);

        Bundle a = getIntent().getExtras();
        Radius = a.getDouble("jarak");

        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(GoogleTempat.this, "Internet Connection Error",
                    "Tolong hubungkan device dengan koneksi internet seperti wi-fi atau paket data", false);
            // stop executing code by return
            return;
        }

        // creating GPS Class object
        gps = new GPSTracker(this);

        // check if GPS location can get
        if (gps.canGetLocation()) {
            Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
        } else {
            // Can't get user's current location
            alert.showAlertDialog(GoogleTempat.this, "GPS Status",
                    "Tidak bisa mendapatkan informasi lokasi,tolong nyalakan GPS pada setting --> location",
                    false);
            // stop executing code by return
            return;
        }

        // Getting listview
        lv = (ListView) findViewById(R.id.list);

        // button show on map
       // btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
        new LoadPlaces().execute();

        /** Button click event for shown on map */
        /** btnShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        PlacesMapActivity.class);
                // Sending user current geo location
                i.putExtra("user_latitude", Double.toString(gps.getLatitude()));
                i.putExtra("user_longitude", Double.toString(gps.getLongitude()));

                // passing near places to map activity
                i.putExtra("near_places", nearPlaces);
                // staring activity
                startActivity(i);
            }
        });**/


        /**
         * ListItem click event
         * On selecting a listitem SinglePlaceActivity is launched
         * */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        Single_place.class);

                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });

    }

    /**
     * Background Async Task to Load Google places
     * */
    class LoadPlaces extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GoogleTempat.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         * */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {
                // Separeate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                //
                String types = "lodging"; // Listing places only cafes, restaurants
                String keyword = "hotels";
                // Radius in meters - increase this value if you don't find any places
                double radius = Radius; // 1000 meters

                // get nearest places
                nearPlaces = googlePlaces.search(gps.getLatitude(),
                        gps.getLongitude(), radius, types, keyword);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;

                    // Check for all possible status
                    if(status.equals("OK")){
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.reference);

                                // Place name
                                map.put(KEY_NAME, p.name);

                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                            // list adapter
                            ListAdapter adapter = new SimpleAdapter(GoogleTempat.this, placesListItems,
                                    R.layout.list_item,
                                    new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
                                    R.id.reference, R.id.name });

                            // Adding data into listview
                            lv.setAdapter(adapter);
                        }
                    }
                    else if(status.equals("ZERO_RESULTS")){
                        // Zero results found
                        alert.showAlertDialog(GoogleTempat.this, "Peringatan",
                                "Maaf tidak ditemukan penginapan di sekitar anda,silakan menurunkan atau menaikan jarak pencarian",
                                false);
                    }
                    else if(status.equals("UNKNOWN_ERROR"))
                    {
                        alert.showAlertDialog(GoogleTempat.this, "Peringatan",
                                "Sorry unknown error occured.",
                                false);
                    }
                    else if(status.equals("OVER_QUERY_LIMIT"))
                    {
                        alert.showAlertDialog(GoogleTempat.this, "Peringatan",
                                "Sorry query limit to google places is reached",
                                false);
                    }
                    else if(status.equals("REQUEST_DENIED"))
                    {
                        alert.showAlertDialog(GoogleTempat.this, "Peringatan",
                                "Sorry error occured. Request is denied",
                                false);
                    }
                    else if(status.equals("INVALID_REQUEST"))
                    {
                        alert.showAlertDialog(GoogleTempat.this, "Peringatan",
                                "Sorry error occured. Invalid Request",
                                false);
                    }
                    else
                    {
                        alert.showAlertDialog(GoogleTempat.this, "Peringatan",
                                "Sorry error occured.",
                                false);
                    }
                }
            });

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pilihan_jarak, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.jarak500:
                Radius = 500.00;
                lv.setAdapter(null);
                placesListItems.clear();
                new LoadPlaces().execute();
                // User chose the "Settings" item, show the app settings UI...
                break;
            case R.id.jarak1km:
                Radius = 1000.00;
                lv.setAdapter(null);
                placesListItems.clear();
                new LoadPlaces().execute();
                // User chose the "Settings" item, show the app settings UI...
                break;
            case R.id.jarak2km:
                Radius = 2000.00;
                lv.setAdapter(null);
                placesListItems.clear();
                new LoadPlaces().execute();
                // User chose the "Settings" item, show the app settings UI...
                break;


//            case R.id.refresh:
//
//               break;
        }
        return true;
}}
