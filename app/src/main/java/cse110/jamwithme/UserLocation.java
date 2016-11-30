package cse110.jamwithme;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Matthew on 11/13/2016.
 */

public class UserLocation implements LocationListener {
    //The minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    //The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000;//1000 * 60 * 1; // 1 minute

    Context mContext;
    private LocationManager locationManager;
    public Location userLoc;
    private double lng;
    private double lat;
    private GeoFire geoFire;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private String userkey;

    public UserLocation(Context c, FirebaseAuth fa, DatabaseReference userData){
        //initialize object data
        mContext = c;
        this.lng = 0;
        this.lat = 0;

        myRef = userData;
        mAuth = fa;

        userkey = "geofire/" + fa.getCurrentUser().getUid();

        geoFire = new GeoFire(myRef);

        getLocation();  //First-time get user location
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

           //know which providers are allowed on phone
            boolean hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //Check for correct version and if permissions are allowed; request if needed
            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission
                            .ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( mContext, android.Manifest.permission
                            .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, Manifest
                        .permission.ACCESS_FINE_LOCATION)) {
                    Toast.makeText(mContext, "Can't access location! No permissions!", Toast
                            .LENGTH_LONG).show();
                }
                else {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest
                            .permission
                                    .ACCESS_FINE_LOCATION, Manifest.permission
                            .ACCESS_COARSE_LOCATION}, 1);
                }
            }

           //If no permissions, inform user that location can't be accessed
            if (!hasGPS && !hasNetwork) {
                Toast.makeText(mContext, "Can't access location!", Toast.LENGTH_LONG).show();
            }
            else {
                //If there is a GPS provider, take location
                if (hasGPS) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                            1000, this);

                    if (locationManager != null) {
                        userLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                       //set lat and lng
                        if (userLoc != null) {
                            lat = userLoc.getLatitude();
                            lng = userLoc.getLongitude();
                        }
                    }
                }
               //If no GPS, check for network location
                if (hasNetwork && userLoc == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        userLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                       //Set lat and lng as needed
                        if (userLoc != null) {
                            lat = userLoc.getLatitude();
                            lng = userLoc.getLongitude();
                        }
                    }
                }
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        //Add location to geofire
        geoFire.setLocation(userkey, getLongLat(), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                    Toast.makeText(mContext, "ERROR SAVING LOCATION", Toast.LENGTH_LONG).show();
                } else {
                    System.out.println("Location saved on server successfully!");
                    //Toast.makeText(mContext, "Location saved", Toast.LENGTH_LONG).show();
                }
            }
        });

        return userLoc;
    }

    //Return a geolocation of the location last found
    public GeoLocation getLongLat() {
        GeoLocation retval = new GeoLocation(lat, lng);
        return retval;
    }

    //Getter for just latitude if ever needed
    public double getLatitude() {
        getLocation();
        return lat;
    }

    //Getter for longitude if ever needed
    public double getLongitude() {
        getLocation();
        return lng;
    }

    //Because this is a location listener, need to override following methods
    @Override
    public void onLocationChanged(Location location) {
        lng = location.getLongitude();
        lat = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String s) {
        //Do nothing
    }

    @Override
    public void onProviderEnabled(String s) {
        //Do nothing
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle b) {
        getLocation();  //If status of location is changed, check and update location
    }
}
