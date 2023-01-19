package com.f83260.foodwaste.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.f83260.foodwaste.R;
import com.f83260.foodwaste.data.AuthDataSource;
import com.f83260.foodwaste.data.StoreRepository;
import com.f83260.foodwaste.data.UserRepository;
import com.f83260.foodwaste.data.model.Opportunity;
import com.f83260.foodwaste.data.model.Store;
import com.f83260.foodwaste.databinding.ActivityMainBinding;
import com.f83260.foodwaste.ui.login.LoginActivity;
import com.f83260.foodwaste.ui.orders.PastOrdersActivity;
import com.f83260.foodwaste.ui.settings.SettingsActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleMap mGoogleMap;
    private LocationRequest mLocationRequest;
    private ActivityMainBinding binding;
    private FusedLocationProviderClient mFusedLocationClient;
    private StoreRepository storeRepository;
    private UserRepository userRepository;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (!locationList.isEmpty()) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                List<Store> stores = storeRepository.getStores();

                renderStoreMarkers(stores);

                mGoogleMap.setOnMarkerClickListener(marker -> {
                    // Open dialog window
                    final Dialog dialog = new Dialog(MainActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);

                    View layoutInf = getLayoutInflater().inflate(R.layout.store_dialog, null);
                    ScrollView scrollView = layoutInf.findViewById(R.id.custom_dialog);

                    LinearLayout layout = scrollView.findViewById(R.id.dialog_layout);

                    layout.findViewById(R.id.btnCancel).setOnClickListener(l -> dialog.dismiss());

                    Store store = storeRepository.getStoreByName(marker.getTitle());

                    //R.id.storeName
                    TextView storeNameLabel =  layout.findViewById(R.id.storeName);
                    storeNameLabel.setText(store.getName());

                    renderOpportunities(layout, store.getOpportunities());

                    dialog.setContentView(scrollView);

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.copyFrom(dialog.getWindow().getAttributes());

                    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
                    float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                    if (dpWidth > 800){
                        lp.width = 800;
                    }

                    dialog.show();
                    dialog.getWindow().setAttributes(lp);

                    return true;
                });

                // Place current location marker
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                //move map camera
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
            }
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storeRepository = StoreRepository.getInstance(getApplicationContext());
        userRepository = UserRepository.getInstance(new AuthDataSource());

        if (!UserRepository.isLoggedIn()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // bind menus
        binding.bottomNavigationView.setOnItemSelectedListener(i -> {
            if (i.getTitle().equals("Settings")) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (i.getTitle().equals("Orders")) {
                startActivity(new Intent(MainActivity.this, PastOrdersActivity.class));
            }
            return true;
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mLocationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setMinUpdateIntervalMillis(5000)
                .build();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", (dialogInterface, i) ->
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION)
                        )
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void renderStoreMarkers(List<Store> stores) {
        ExecutorService executor = Executors.newFixedThreadPool(10);

        List<PointDrawer> taskList = new ArrayList<>();
        for (Store store : stores) {
            taskList.add(new PointDrawer(store, mGoogleMap));
        }

        try {
            executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executor.shutdown();

        for (Store store : stores) {
            LatLng storeLoc = new LatLng(store.getLatitude(), store.getLongitude());

            MarkerOptions markerOptions2 = new MarkerOptions();
            markerOptions2.position(storeLoc);
            markerOptions2.title(store.getName());
            markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            mGoogleMap.addMarker(markerOptions2);
        }
    }

    // There is a bug here where if you reserve opportunities, then open "Orders" close it, and open the same store again,
    // the opportunities don't show as reserved.
    public void renderOpportunities(LinearLayout parentLayout, List<Opportunity> opps) {
        for (Opportunity opp : opps) {
            boolean isClaimed = opp.getUserClaimedId() != null;

            if (isClaimed && !Objects.equals(opp.getUserClaimedId(), userRepository.loggedUser().getUserId()))
                continue;

            View layoutInf2 = getLayoutInflater().inflate(R.layout.opportunity_template, null);
            LinearLayout oppLayout = layoutInf2.findViewById(R.id.opportunity_template);

            TextView productName = oppLayout.findViewById(R.id.productName);
            TextView addedAgoTime = oppLayout.findViewById(R.id.addedAgoTime);
            productName.setText(opp.getProductName());

            long addedAgoHours = ((new Date().getTime() - opp.getCreatedAt().getTime()) / 1000 / 60 / 60);
            String addedAgoMsg = "Added " + addedAgoHours + " hours ago.";
            addedAgoTime.setText(addedAgoMsg);

            if (isClaimed && opp.getUserClaimedId().equals(userRepository.loggedUser().getUserId())) {
                oppLayout.findViewById(R.id.Reserve).setVisibility(View.GONE);
                oppLayout.findViewById(R.id.CancelReservation).setVisibility(View.VISIBLE);
            }

            // Listener for Reserve button
            oppLayout.findViewById(R.id.Reserve).setOnClickListener(l -> {
                storeRepository.reserveOpportunity(opp, userRepository.loggedUser().getUserId());
                // Updates the UI with the new "Cancel" button
                oppLayout.findViewById(R.id.Reserve).setVisibility(View.GONE);
                oppLayout.findViewById(R.id.CancelReservation).setVisibility(View.VISIBLE);

            });

            // Listener for Cancel Reservation button
            oppLayout.findViewById(R.id.CancelReservation).setOnClickListener(l -> {
                storeRepository.removeReservation(opp);
                // Updates the UI with the new "Cancel" button
                oppLayout.findViewById(R.id.Reserve).setVisibility(View.VISIBLE);
                oppLayout.findViewById(R.id.CancelReservation).setVisibility(View.GONE);
            });

            parentLayout.addView(oppLayout);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    mGoogleMap.setMyLocationEnabled(true);
                }

            } else {
                // permission denied, boo!
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}

class PointDrawer implements Callable<Object> {
    private final Store store;
    private final GoogleMap map;

    PointDrawer(Store store, GoogleMap mapReference) {
        this.store = store;
        this.map = mapReference;
    }

    @Override
    public Object call() throws Exception {
        LatLng storeLoc = new LatLng(store.getLatitude(), store.getLongitude());

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(storeLoc);
        markerOptions2.title(store.getName());
        markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        map.addMarker(markerOptions2);
        return null;
    }
}