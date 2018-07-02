package com.example.user.aalsi_student.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.bumptech.glide.Glide;
import com.example.user.aalsi_student.utils.MyProgressDialog;
import com.google.android.gms.location.LocationListener;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.aalsi_student.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class FirstScreenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final int REQUEST_CHECK_SETTINGS = 12345;
    private String requestForMarsh = "forMarsh";
    private String requestForLocation = "forLocation";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private boolean isLocated = false;
    public String finalAddress = "";
    private ImageView firstScreenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        firstScreenImage = (ImageView) findViewById(R.id.firstScreenImage);
        /******************Enable*********************/
//        Glide.with(this).load("http://bestanimations.com/Books/page-turning-book-animation-17.gif").into(firstScreenImage);
        /******************Enable*********************/

//        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//        if (pref.getBoolean("activity_executed", false)) {
//            Intent intent = new Intent(this, Main_ScreenActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Intent intent = new Intent(this, HomeScreenActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        MyProgressDialog.showPDialog(this);
        requestPermissionsIfNotGranted();
        buildGoogleApiClient();
        createLocationRequest();
        turnOnGpsForCurrentLocation();

    }

    private void requestPermissionsIfNotGranted() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
    }

    private void turnOnGpsForCurrentLocation() {
        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
        result.setResultCallback(mResultCallbackFromSettings);
    }

    // The callback for the management of the user settings regarding location
    private ResultCallback<LocationSettingsResult> mResultCallbackFromSettings = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult result) {

            final Status status = result.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                    Log.d("ResultCallback", "LocationSettingsStatusCodes.SUCCESS....");
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        Log.d("ResultCallback", "LocationSettingsStatusCodes.RESOLUTION_REQUIRED....");
                        status.startResolutionForResult(FirstScreenActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.d("ResultCallback", "Settings change unavailable. We have no way to fix the settings so we won't show the dialog.");
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {

                // All required changes were successfully made
                if (mGoogleApiClient.isConnected()) {
                    Log.d("onActivityResult", "resultCode == RESULT_OK....");
                }

            } else if (resultCode == RESULT_CANCELED) {
                // The user was asked to change settings, but chose not to
                openAlertDialog(R.string.gps_settings_disabled, R.string.choose_yes_on_dialog, requestForLocation);
                Log.d("onActivityResult", "resultCode == RESULT_CANCELED....");
            }
        }
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onBackPressed() {
        backButton(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServicesAvailable();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            stopLocationUpdates();
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    /**
     * Method to verify google play services on the device
     */
    public void checkPlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(FirstScreenActivity.this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 1000).show();
            } else {
                Toast.makeText(this, R.string.device_not_supported, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("ConnectionCallbacks", "onConnected....");
        // Register to get location updates
        if (!isLocated) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("ConnectionCallbacks", "onConnectionSuspended....");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("OnConnectionFailedLis", "onConnectionFailed....");
    }

    private String finalLatitude = "";
    private String finalLongitude = "";
    private String finalState = "";
    private String finalCountry = "";
    private String finalCity = "";


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d("onLocationChanged", "onLocationChanged....");
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            boolean isFetched = saveUserCurrentAddress(latLng);
            Log.d("=>>>>>>>>>>>>>>>>>>>>", saveUserCurrentAddress(latLng) + "");
            if (isFetched) {
                stopLocationUpdates();
                isLocated = true;

                openNextScreen();
            }
        }
    }

    private void openNextScreen() {
        // startActivity(new Intent(getApplicationContext(), HomeScreenActivity.class));

//        Intent intent = new Intent(this, HomeScreenActivity.class);
//        intent.putExtra("add", finalAddress);
//        Log.d("add", finalAddress);
//        startActivity(intent);
        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if (pref.getBoolean("activity_executed", false)) {
            Intent intent = new Intent(this, Main_ScreenActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            startActivity(intent);
            finish();
        }

        finish();
    }

    private boolean saveUserCurrentAddress(LatLng latLng) {

        Double lat = latLng.latitude;
        Double lng = latLng.longitude;
        Log.d("->>>>", lat + "," + lng + "");
        boolean isFetched = false;

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            finalLatitude = "" + latLng.latitude;
            finalLongitude = "" + latLng.longitude;
            finalState = (addresses.get(0).getAdminArea() == null) ? "" : addresses.get(0).getAdminArea().trim();
            finalCountry = (addresses.get(0).getCountryName() == null) ? "" : addresses.get(0).getCountryName().trim();
            finalCity = (addresses.get(0).getLocality() == null) ? "" : addresses.get(0).getLocality().trim();
            finalAddress = (addresses.get(0).getAddressLine(0) == null ? "" : addresses.get(0).getAddressLine(0).trim());

            Log.d("Lat>>>>>", "" + finalLatitude);
            Log.d("Long>>>>", "" + finalLongitude);
            Log.d("Address>>>>", "" + finalCity);
            Log.d("Address2>>>>", "" + finalCountry);
            Log.d("Address1>>>>", "" + finalState);
            Log.d("Address3>>>>", "" + finalAddress);


//            PreferenceStore.getPreference(this).putString(KEY_LAT_PREF, "" + finalLatitude);
//            PreferenceStore.getPreference(this).putString(KEY_LONG_PREF, "" + finalLongitude);
//            PreferenceStore.getPreference(this).putString(KEY_STATE_PREF, "" + finalState);
//            PreferenceStore.getPreference(this).putString(KEY_COUNTRY_PREF, "" + finalCountry);
//
//            PreferenceStore.getPreference(this).putString(KEY_CITY, "" + finalCity);

            isFetched = true;

        } catch (Exception e) {

            e.printStackTrace();
            isFetched = false;

            finalLatitude = "";
            finalLongitude = "";
            finalState = "";
            finalCountry = "";
        }

        return isFetched;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (!isLocated) {
                        startLocationUpdates();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    openAlertDialog(R.string.location_access_denied, R.string.location_denied_marsh, requestForMarsh);
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void openAlertDialog(final int title, int message, final String from) {

        AlertDialog.Builder builder = new AlertDialog.Builder(FirstScreenActivity.this);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (from.equals(requestForLocation)) {
                    turnOnGpsForCurrentLocation();
                } else if (from.equals(requestForMarsh)) {
                    requestPermissionsIfNotGranted();
                }
            }
        });

        builder.show();
    }

    public void backButton(View view) {

        // Put blank to disable back press at the time of fetching location..
    }

}
