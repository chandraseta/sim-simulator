package com.thesim.simsimulator;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.thesim.simsimulator.utils.Helper;

import java.util.concurrent.Executor;

/**
 * LocationService maintains a service that detects user's location at all time.
 */
public class LocationService extends Service implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

  private final String TAG = this.getClass().getSimpleName();

  private FusedLocationProviderClient fusedClient;
  private LocationRequest locationRequest;
  private LocationCallback locationCallback;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d(TAG, "LocationService onCreate");
    fusedClient = LocationServices.getFusedLocationProviderClient(this);

    locationRequest = new LocationRequest()
        .setInterval(30000)
        .setFastestInterval(10000)
        .setNumUpdates(1)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    locationCallback = new LocationCallback() {
      @Override
      public void onLocationResult(LocationResult locationResult) {
        Log.d(TAG, "LocationService onCreate onLocationResult");
        super.onLocationResult(locationResult);
        if (locationResult != null) {
          Location newLocation = locationResult.getLastLocation();
          onLocationChanged(newLocation);
        }
      }
    };

    if (Helper.checkLocationPermission(this)) {
      Log.d(TAG, "LocationService updateLocation check passed");
      fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    } else {
      Log.d(TAG, "LocationService updateLocation check failed");
    }

    updateLocation();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    fusedClient.removeLocationUpdates(locationCallback);
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.d(TAG, "LocationService onConnected");
    if (Helper.checkLocationPermission(this)) {
      Log.d(TAG, "LocationService onConnected withPermission");
      fusedClient.getLastLocation()
          .addOnSuccessListener((Executor) this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
              if (location != null) {
                onLocationChanged(location);
              }
            }
          });
      updateLocation();
    } else {
      Log.d(TAG, "LocationService onConnected noPermission");
      return;
    }
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  protected void updateLocation() {
    Log.d(TAG, "LocationService updateLocation");
    if (Helper.checkLocationPermission(this)) {
      Log.d(TAG, "LocationService updateLocation check passed");
      fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    Log.d(TAG, "LocationService onConnectionSuspended");
    if (i == CAUSE_NETWORK_LOST) {
      Toast.makeText(this, "Network connection lost", Toast.LENGTH_LONG).show();
    } else if (i == CAUSE_SERVICE_DISCONNECTED) {
      Toast.makeText(this, "Location service connection failed", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    Log.d(TAG, "LocationService onLocationChanged");
    Intent intent = new Intent("location");
    String latitude = String.valueOf(location.getLatitude());
    String longitude = String.valueOf(location.getLongitude());
    intent.putExtra("lat", latitude);
    intent.putExtra("long", longitude);
    Log.d(TAG, "Latitude " + location.getLatitude() + " Longitude " + location.getLongitude());
    sendBroadcast(intent);
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(TAG, "LocationService onConnectionFailed");
  }
}
