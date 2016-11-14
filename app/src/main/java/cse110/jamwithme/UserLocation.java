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

    private LocationListener locationListener;

    public UserLocation(Context c){
        mContext = c;
        this.lng = 0;
        this.lat = 0;

        /*LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lng = location.getLongitude();
                lat = location.getLatitude();
            }
        };*/

        getLocation();
        Toast.makeText(mContext, "lat" + lat, Toast.LENGTH_LONG).show();
        Toast.makeText(mContext, "long" + lng, Toast.LENGTH_LONG).show();
    }

    public Location getLocation() {
        try {
           /*Our location manager*/
            locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

           /*Booleans to tell us which providers were found*/
            boolean hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if ( Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(mContext, android.Manifest.permission
                            .ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission( mContext, android.Manifest.permission
                            .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(mContext, "Can't access location! No permissions!", Toast
                        .LENGTH_LONG).show();

                if(ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, Manifest
                        .permission.ACCESS_FINE_LOCATION)) {

                }
                else {
                    ActivityCompat.requestPermissions((Activity)mContext, new String[]{Manifest
                            .permission
                                    .ACCESS_FINE_LOCATION, Manifest.permission
                            .ACCESS_COARSE_LOCATION}, 1);
                }

                int perm = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission
                        .ACCESS_FINE_LOCATION );
                Toast.makeText(mContext, "perm: " + perm, Toast.LENGTH_LONG)
                        .show();
                int grant = PackageManager.PERMISSION_GRANTED;
                Toast.makeText(mContext, "granted: " + grant, Toast.LENGTH_LONG).show();
                //return null;
            }

           /*If we can't find a GPS or Network provider, gg LOL*/
            if (!hasGPS && !hasNetwork) {
                Toast.makeText(mContext, "Can't access location!", Toast.LENGTH_LONG).show();
            }
            else {
                /*If we found a GPS provider*/
                if (hasGPS) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                            1000, this);

                    if (locationManager != null) {
                        userLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                       /*If we found a location, set lat and long*/
                        if (userLoc != null) {
                            lat = userLoc.getLatitude();
                            lng = userLoc.getLongitude();
                        }
                    }
                }
               /*If we found a network provider*/
                if (hasNetwork && userLoc == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        userLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                       /*If we have a location, set lat and long*/
                        if (userLoc != null) {
                            lat = userLoc.getLatitude();
                            lng = userLoc.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userLoc;
    }

    public double[] getLongLat() {
        double[] retval = {lng, lat};
        return retval;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }

    @Override
    public void onLocationChanged(Location location) {
        lng = location.getLongitude();
        lat = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String s) {
        //do stuff
    }

    @Override
    public void onProviderEnabled(String s) {
        //do stuff
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle b) {
        Toast.makeText(mContext, "Status Changed", Toast.LENGTH_LONG).show(); //DEBUG
        getLocation();
        Toast.makeText(mContext, "lat" + lat, Toast.LENGTH_LONG).show();
        Toast.makeText(mContext, "long" + lng, Toast.LENGTH_LONG).show();
    }
}
