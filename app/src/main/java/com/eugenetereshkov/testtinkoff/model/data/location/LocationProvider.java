package com.eugenetereshkov.testtinkoff.model.data.location;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;

import com.eugenetereshkov.testtinkoff.di.scope.ActivityScoped;
import com.eugenetereshkov.testtinkoff.ui.depositionpointsmap.DepositionPointsMapFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.util.Date;

import javax.inject.Inject;


@ActivityScoped
public class LocationProvider {
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient fusedLocationClient;


    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient settingsClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest locationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest locationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback locationCallback;

    /**
     * Represents a geographical location.
     */
    private Location currentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates.
     */
    private Boolean requestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String lastUpdateTime;

    private Activity activity;
    private boolean permissionsGranted = false;
    private OnClientListener userListener;

    @Inject
    public LocationProvider(Activity activity) {
        this.activity = activity;
        requestingLocationUpdates = false;
        lastUpdateTime = "";

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        settingsClient = LocationServices.getSettingsClient(activity);

        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    public boolean isPermissionsGranted() {
        return permissionsGranted;
    }

    public final void setPermissionsGranted(boolean permissionsGranted) {
        this.permissionsGranted = permissionsGranted;
    }

    /**
     * Creates a observable for receiving location events.
     */
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                userListener.updateUserLocation(currentLocation);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                userListener.onUpdateLocationError();
            }
        };
    }

    /**
     * that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    public final void startLocationUpdates(OnLocationUpdatingListener listener) {
        if (!permissionsGranted) return;

        if (!requestingLocationUpdates) {
            requestingLocationUpdates = true;
            listener.setComplete(true);
            continueLocationUpdates(listener);
        }
    }

    @SuppressLint("MissingPermission")
    private void continueLocationUpdates(OnLocationUpdatingListener listener) {

        SettingsClient client = LocationServices.getSettingsClient(activity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(locationSettingsRequest);

        task.addOnSuccessListener(activity, locationSettingsResponse -> {
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, Looper.myLooper()
            );
        }).addOnFailureListener(activity, e -> {
            int statusCode = ((ApiException) e).getStatusCode();
            requestingLocationUpdates = false;
            listener.setComplete(false);

            switch (statusCode) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the
                        // result in onActivityResult().
                        ResolvableApiException rae = (ResolvableApiException) e;
                        rae.startResolutionForResult(activity,
                                DepositionPointsMapFragment.REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sie) {

                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    String errorMessage = "Location settings are inadequate, and cannot be " +
                            "fixed here. Fix in Settings.";

                    userListener.onGpsError();
            }
        });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    public void stopLocationUpdates(OnLocationUpdatingListener listener) {
        if (!requestingLocationUpdates) return;

        fusedLocationClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener(task -> {
                    requestingLocationUpdates = false;
                    listener.setComplete(false);
                });
    }


    public void setOnUpdateUserLocationListener(OnClientListener listener) {
        userListener = listener;
    }

    @SuppressLint("MissingPermission")
    public void updateLastUserLocation() {
        if (!permissionsGranted) return;

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) userListener.updateUserLocation(location);
                });
    }
}

