package com.example.amexhack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.amexhack.yelpapi.YelpRestaurant;
import com.example.amexhack.yelpapi.YelpSearchResult;
import com.example.amexhack.yelpapi.YelpService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import java.net.ConnectException;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static List<Place> AllPlaces;

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean permissionGranted = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SupportMapFragment mapFragment;
    private static final float DEF_ZOOM = 15f;

    private double[][] coordinateArray;
    private LatLng[] latlngArray;
    private String[] titleArray;

    private Button viewMoreButton;

    private SharedPreferences viewThisPage;

    SharedPreferences.Editor viewThisPageEditor;

    private static final String myPreference = "viewThisPage";

    private static final String locations = "itineraryLocations";

    private String BASE_URL = "https://api.yelp.com/v3/businesses/";

    private String API_KEY = "LWAv17INzFEQdx0yv-OisJHiqJnSRtRA3nHr_IJVfNgPWCWY0D_8y85km7Ifo6NtWROpFfk8JLAKFxbqS6gyXaFWMPvrTrAg8Gh9SwEXYnixrKzUwqMaar5g9yNRX3Yx";

    ArrayList<YelpRestaurant> restaurants = new ArrayList<>();

    ArrayList<String> placesToVisit = new ArrayList<>();

    ArrayList<String> placesToVisitLocations = new ArrayList<>();

    LinearLayout itineraryScroller;



    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady");
        gMap = googleMap;

        if (permissionGranted) {

            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);

        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_map);

        getLocationPermission();

        for(Place place : AllPlaces){
            System.out.println("CHECK THE NUMBER OF DECIMAL PLACES NEEDED");
            System.out.println(place.latitude);
            System.out.println(place.longitude);
            System.out.println(place.name);
        }

        itineraryScroller = findViewById(R.id.scrollerForItinerary);

        //SET UP ARRAY TO TEST OUT ITINERARY DISPLAY
        //Access from AllPlaces

        placesToVisit.add("Department of Coffee and Social Affairs");
        placesToVisit.add("Apple Regent Centre");
        placesToVisit.add("Chipotle mexican grill");

        placesToVisitLocations.add("London");
        placesToVisitLocations.add("London");
        placesToVisitLocations.add("London");

        callYelpAPI();

        viewThisPage = this.getSharedPreferences(myPreference, Context.MODE_PRIVATE);
        viewThisPageEditor = viewThisPage.edit();

        viewMoreButton = findViewById(R.id.viewMoreInfoButton);
        viewMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItineraryDisplay();
            }
        });
    }


    public void callYelpAPI() {
        startAsyncTask();
    }

    public void startAsyncTask(){
        APICallAsyncTask apiCallAsyncTask = new APICallAsyncTask();
        apiCallAsyncTask.doInBackground();

    }

    private class APICallAsyncTask extends AsyncTask<View, VisitingPlace, String>{

        @Override
        protected String doInBackground(View... views) {
            for (Place place: AllPlaces){
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                YelpService yelpService = retrofit.create(YelpService.class);
                //yelpService.searchRestaurants("Bearer "+ API_KEY,place.name, place.latitude, place.longitude).enqueue(new Callback<YelpSearchResult>() {

                yelpService.searchRestaurants("Bearer "+ API_KEY,place.name, "London").enqueue(new Callback<YelpSearchResult>() {

                    @Override
                    public void onResponse(Call<YelpSearchResult> call, Response<YelpSearchResult> response) {
                        Log.i(TAG, "onResponse "+response);
                        YelpSearchResult result = response.body();
                        VisitingPlace visitingPlaces;

                        if (result == null){
                            Log.w(TAG, "Did not receive valid response");
                            visitingPlaces = new VisitingPlace(null,place.name, null, true);
                            publishProgress(visitingPlaces);
                            return;
                        }

                        System.out.println(result);

                        if (result.getTotal() == 0){
                            System.out.println("in total equals 0");
                            //placeOnToView("", place.name, true);
                            visitingPlaces = new VisitingPlace(null,place.name, null, true);
                        }
                        else {

                            final YelpRestaurant restaurant = result.getRestaurants().get(0);

                            System.out.println(restaurant);

                            System.out.println("Name = " + restaurant.getName());

                            visitingPlaces = new VisitingPlace(restaurant.getId(),restaurant.getName(), restaurant.getImageUrl(), restaurant.getOpenNow());

                        }
                        publishProgress(visitingPlaces);
                    }

                    @Override public void onFailure(Call<YelpSearchResult> call, Throwable t) {
                        Log.i(TAG, "onFailure "+t);
                    }
                });
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(VisitingPlace... values) {
            super.onProgressUpdate(values);
            placeOnToView(values[0].id, values[0].imageURL, values[0].placeName, values[0].openNow);
        }
    }

    public void placeOnToView(String id, String imageUrl, String placeName, Boolean openNow) {
        LayoutInflater layoutInflater =
                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.row_intinerary,null);

        final ImageSlider imageView = addView.findViewById(R.id.imageOfPlace);

        final TextView placeNameTextView  = addView.findViewById(R.id.placeName);

        final TextView openTextView = addView.findViewById(R.id.openNow);

        final Button button = addView.findViewById(R.id.moreInfoButton);

        if (imageUrl == null){
            placeNameTextView.setText(placeName);
            openTextView.setText("Unfortunately no information on Yelp available at the moment");
            button.setVisibility(View.INVISIBLE);
        }
        else {
            List<SlideModel> slideModelList = new ArrayList<>();

            slideModelList.add(new SlideModel(imageUrl, ScaleTypes.CENTER_CROP));
            imageView.setImageList(slideModelList, ScaleTypes.CENTER_CROP);

            placeNameTextView.setText(placeName);

            if (openNow) {
                openTextView.setText("Open Now");
            } else {
                openTextView.setText("Closed");
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //store id in Systems preference and then load new page.
                    //myPreference;
                    viewMoreInformationPage(id);
                }
            });
        }
        itineraryScroller.addView(addView);
    }

    public void viewMoreInformationPage(String id){
        viewThisPageEditor.putString(myPreference, id);
        viewThisPageEditor.commit();

        Intent intent = new Intent(this, InformationAboutPlace.class);
        startActivity(intent);

    }

    public void openItineraryDisplay(){
        saveInfoToPreferences();

        Intent intent = new Intent(this, InformationAboutPlace.class);
        startActivity(intent);
    }

    public void saveInfoToPreferences(){
        String variableThatContainsAnswers = "Department of Coffee";
        viewThisPageEditor.putString(myPreference, variableThatContainsAnswers);
        //CALL THE FUNCTION THAT RETURNS THE BEST LOCATIONS THAT THE USER SHOULD VISIT
        viewThisPageEditor.commit();
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation()");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if(permissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "getDeviceLocation(): onComplete() successful");
                            Location curLocation = (Location) task.getResult();
                            moveCamera(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()), DEF_ZOOM);
                        }
                        else {
                            Log.d(TAG, "getDeviceLocation(): onComplete() unsuccessful");
                            Toast.makeText(MapActivity.this, "getDeviceLocation unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
        catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation(): SecurityException" + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera() Lat: " + latLng.latitude + " Lng: " + latLng.longitude);

        createCoordinateArray();
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlngArray[0],zoom));
        createMarker();
        createTaskDirection();
    }

    private void createMarker() {
        for (int i = 0; i < AllPlaces.size(); i++) {
            gMap.addMarker(new MarkerOptions().position(latlngArray[i]).title(AllPlaces.get(i).getName()));

        }
    }

    private void createCoordinateArray() {
//        coordinateArray = new double[][]{{51.525280, -0.136347}, {51.528557, -0.132258}, {51.526966, -0.125864}, {51.530882, -0.119044}};
        latlngArray = new LatLng[AllPlaces.size()];
        System.out.println("THIS IS SIZE " + AllPlaces.size());
        for (int i = 0; i < latlngArray.length; i++) {
            System.out.println(AllPlaces.get(i).name);
            System.out.println(AllPlaces.get(i).getLatitude()  + " : " +  AllPlaces.get(i).getLongitude());
            latlngArray[i] = new LatLng( AllPlaces.get(i).getLongitude(), AllPlaces.get(i).getLatitude());
        }
    }

    private void createTaskDirection() {
        for (int i = 0; i < latlngArray.length-1; i++) {
            new TaskDirectionRequest().execute((buildRequestUrl(latlngArray[i], latlngArray[i+1])));
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap()");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) MapActivity.this);
    }

    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission()");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),  FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                initMap();
            }
            else {
                ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(this,permissions,LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantedResults) {
        Log.d(TAG, "onRequestPermissionsResult(): called");

        permissionGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantedResults.length > 0 && grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                    for (int i = 0; i < grantedResults.length; i++) {
                        if (grantedResults[i] != PackageManager.PERMISSION_GRANTED) {
                            permissionGranted = false;
                            Log.d(TAG, "onRequestPermissionResult(): permission failed");
                            return;
                        }
                    }
                    permissionGranted = true;
                    Log.d(TAG, "onRequestPermissionResult(): permission granted");
                    initMap();
                }
            }
        }

    }

    private String getRequestedURL(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.maps_api_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + APIKEY;
        return url;
    }

    private String requestDirection(String requestedUrl) {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(requestedUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            responseString = stringBuffer.toString();
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        httpURLConnection.disconnect();
        return responseString;
    }

    //Parse JSON Object from Google Direction API & display it on Map
    public class TaskParseDirection extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonString) {
            List<List<HashMap<String, String>>> routes = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(jsonString[0]);
                DirectionParser parser = new DirectionParser();
                routes = parser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lng"));

                    points.add(new LatLng(lat, lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15f);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }
            if (polylineOptions != null) {
                gMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Get JSON data from Google Direction
    public class TaskDirectionRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String responseString) {
            super.onPostExecute(responseString);
            //Json object parsing
            TaskParseDirection parseResult = new TaskParseDirection();
            parseResult.execute(responseString);
        }
    }

    /**
     * Create requested url for Direction API to get routes from origin to destination
     *
     * @param origin
     * @param destination
     * @return
     */
    private String buildRequestUrl(LatLng origin, LatLng destination) {
        String strOrigin = "origin=" + origin.latitude + "," + origin.longitude;
        String strDestination = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=walking";

        String param = strOrigin + "&" + strDestination + "&" + sensor + "&" + mode;
        String output = "json";
        String APIKEY = getResources().getString(R.string.maps_api_key);

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + "&key="+APIKEY;
        Log.d("TAG", url);
        return url;
    }

    /**
     * Request app permission for API 23/ Android 6.0
     *
     * @param permission
     */
    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

}

