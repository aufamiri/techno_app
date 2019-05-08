package com.svr.techno;

import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.svr.techno.Adapters.Models.ItemModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.android.volley.VolleyLog.TAG;

public class fragmentMap extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationRequest locationRequest;
    private LatLng origin;
    private PlacesClient placesClient;

    private TextView locationText;
    private Long userType;

    private ItemModel itemModel;

    private FirebaseAuth authInstance;
    private FirebaseFirestore firestoreInstance;

    private boolean LocationPermissionGranted;

    private static final int REQUEST_LOCATION = 9;

    @Override
    public void onCreate(@Nullable Bundle savedInstacedState) {
        super.onCreate(savedInstacedState);

        if (getArguments() != null) {
            itemModel = (ItemModel) getArguments().getSerializable("item");
        }

        authInstance = FirebaseAuth.getInstance();
        firestoreInstance = FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup parent, Bundle savedInstacedstate) {
        View view = inflater.inflate(R.layout.fragment_map, parent, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(fragmentMap.this);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

        FloatingActionButton locationFab = view.findViewById(R.id.mylocation_fab);
        locationText = view.findViewById(R.id.tujuan_address);
        locationFab.setOnClickListener(this);

        TextView nameText = view.findViewById(R.id.maps_page_title);
        TextView detailsText = view.findViewById(R.id.maps_page_factory);
        nameText.setText(itemModel.getName());
        detailsText.setText(itemModel.getCategory());

        Button confirmButton = view.findViewById(R.id.confirm_button);
        confirmButton.setText(itemModel.getPriceText());
        confirmButton.setOnClickListener(this);

        Places.initialize(getActivity(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(Objects.requireNonNull(getContext()));

        mMap = googleMap;

        getLocationPermission();
        updateLocation();
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()).getApplicationContext(),
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        LocationPermissionGranted = false;

        // If request is cancelled, the result arrays are empty.
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionGranted = true;
            }
        }
    }

    // set up Location Service
    private void updateLocation() {
        try {
            if (LocationPermissionGranted) {

                //get Last Known Location
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(Objects.requireNonNull(getActivity()), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {

                                    origin = new LatLng(location.getLatitude(), location.getLongitude());
                                }
                            }
                        });

                locationRequest = LocationRequest.create();
                locationRequest.setInterval(10000); //interval per update
                locationRequest.setFastestInterval(1000); //max interval per update
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); //accuracy mode

                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);


                SettingsClient client = LocationServices.getSettingsClient(getActivity());
                client.checkLocationSettings(builder.build())
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<LocationSettingsResponse>() {
                            @Override
                            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                                fragmentMap.this.startLocationUpdates(); //start get location on success
                                fragmentMap.this.showNavigation();
                            }
                        })
                        .addOnFailureListener(getActivity(), new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (e instanceof ResolvableApiException) {
                                    try {
                                        ResolvableApiException resolvable = (ResolvableApiException) e;
                                        resolvable.startResolutionForResult(fragmentMap.this.getActivity(), REQUEST_LOCATION);
                                    } catch (IntentSender.SendIntentException ignored) {
                                    }
                                }
                            }
                        });
            }
        }
        catch (SecurityException e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void startLocationUpdates() {
        try {
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }

                    for (Location location : locationResult.getLocations()) {
                        origin = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                }
            };

            if (LocationPermissionGranted){
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
        catch (SecurityException e) {
            Log.d("Error", "startLocationUpdates: Permissions Error");
        }

    }

    private void showNavigation() {
        mMap.addMarker(new MarkerOptions().position(origin).title("User"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, 25));

        addressSearch();
    }

    private void addressSearch() {

        final double[] probability = {0};
        final String[] placeName = new String[1];


        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesClient.findCurrentPlace(request).addOnSuccessListener((new OnSuccessListener<FindCurrentPlaceResponse>() {
                @Override
                public void onSuccess(FindCurrentPlaceResponse response) {
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        //Mencari Lokasi terdekat
                        if (placeLikelihood.getLikelihood() > probability[0]) {
                            probability[0] = placeLikelihood.getLikelihood();
                            placeName[0] = placeLikelihood.getPlace().getName();
                        }
                    }
                    locationText.setText(placeName[0]);
                }
            })).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("Error", "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        } else {
            getLocationPermission();
        }
    }

    private void checkUserType() {

        String uid = authInstance.getCurrentUser().getUid();
        DocumentReference docRef = firestoreInstance.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        userType = (Long) document.get("type");
                        if (userType == 0){
                            confirmOrder();
                        }
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
//                        Log.d(TAG, "No such document");
                    }
                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }


    public void confirmOrder() {
            DocumentReference userReference = firestoreInstance.collection("orders").document();
            String key = userReference.getId();
            String buyerId = authInstance.getCurrentUser().getUid();
            String itemId = key;

            Map<String, Object> item = new HashMap<>();
            item.put("id_item", itemModel.getItemId());
            item.put("id_seller", itemModel.getSellerId());
            item.put("id_buyer", buyerId);
            item.put("confr_seller", 0);
            item.put("confr_buyer", 0);
            item.put("date",itemModel.getUploadDate());
            item.put("coordinat_buyer",origin);

            Log.i("TEST","FINISIH LAINE");


        userReference
                .set(item, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intentA = new Intent(getActivity(), HistoryActivity.class);
                        getActivity().startActivity(intentA);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mylocation_fab:
                showNavigation();
                break;
            case R.id.confirm_button:
                checkUserType();
                break;
        }
    }
}
