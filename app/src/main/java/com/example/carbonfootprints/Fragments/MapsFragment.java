package com.example.carbonfootprints.Fragments;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.carbonfootprints.Maps;
import com.example.carbonfootprints.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = Maps.class.getSimpleName();
    private GoogleMap map;
    private CameraPosition cameraPosition;
    SearchView searchView;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // the last-known location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    CardView cardView;
    View view;
    Button saveIt;
    TextView timeText;
    TextView walkingMiles;
    TextView drivingMiles;
    TextView milesSaved;
    TextView homeAddy;
    TextView destinationAddy;
    double drivingMilesNum;
    double walkingMilesNum;

    String placesAutoCompleteApiKey = "AIzaSyB4xzE3Pc_NP7MaWsuyXvx85J-JX2kYVYw";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapsFragment newInstance(String param1, String param2) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_maps, container, false);

        Places.initialize(view.getContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(view.getContext());

        searchView = view.findViewById(R.id.idSearchView);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view.getContext());

        saveIt = view.findViewById(R.id.saveIt);
        timeText = view.findViewById(R.id.timeText);
        walkingMiles = view.findViewById(R.id.walkingMiles);
        drivingMiles = view.findViewById(R.id.drivingMiles);
        milesSaved = view.findViewById(R.id.milesSaved);
        homeAddy = view.findViewById(R.id.homeAddy);
        destinationAddy = view.findViewById(R.id.destinationAddy);
        cardView = view.findViewById(R.id.cardView);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                cardView.setVisibility(View.VISIBLE);
                homeAddy.setText("Syracuse, NY");
                destinationAddy.setText(place.getName());
                DateTime now = new DateTime();
                String decider = "Driving";
                try {
                    DirectionsResult result = DirectionsApi.newRequest(createGeoApiContext())
                            .mode(TravelMode.DRIVING)
                            .origin(new com.google.maps.model.LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                            .destination(new com.google.maps.model.LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                            .departureTime(now).await();
                    addMarkersToMap(result,map, decider);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
                decider = "Walking";
                try {
                    DirectionsResult result = DirectionsApi.newRequest(createGeoApiContext())
                            .mode(TravelMode.WALKING)
                            .origin(new com.google.maps.model.LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
                            .destination(new com.google.maps.model.LatLng(place.getLatLng().latitude, place.getLatLng().longitude))
                            .departureTime(now).await();
                    addMarkersToMap(result,map, decider);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap, String decider) {
        for (int i=0;i<results.routes.length;i++) {
            if(decider == "Driving") {
                mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(results.routes[i].legs[0].startLocation.lat,
                                        results.routes[i].legs[0].startLocation.lng)).
                                title(results.routes[i].legs[0].startAddress));
                mMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(results.routes[i].legs[0].endLocation.lat,
                                        results.routes[i].legs[0].endLocation.lng)).
                                title(results.routes[i].legs[0].endAddress).snippet(getEndLocationTitle(results,i)));
            }
            addPolyline(results, map, i, decider);
        }
    }

    private String getEndLocationTitle(DirectionsResult results, int i) {
        return "Time :" + results.routes[i].legs[0].duration.humanReadable +
                " Distance :" + results.routes[i].legs[0].distance.humanReadable;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap, int i, String decider) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[i].overviewPolyline.getEncodedPath());
        if(decider == "Driving") {
            mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(5).color(Color.RED));
        } else {
            mMap.addPolyline(new PolylineOptions().addAll(decodedPath).width(5).color(R.color.green_dark));
        }
        setCardView(results, decider, i);
    }

    private void setCardView(DirectionsResult results, String decider, int i) {
        if(decider == "Driving") {
            drivingMiles.setText(results.routes[i].legs[0].distance.humanReadable);
            drivingMilesNum = (results.routes[i].legs[0].distance.inMeters/1609.344);
            drivingMilesNum = Math.round(drivingMilesNum*100.0)/100.0;
        } else {
            walkingMiles.setText(results.routes[i].legs[0].distance.humanReadable);
            timeText.setText(results.routes[i].legs[0].duration.humanReadable.substring(6));
            walkingMilesNum = (results.routes[i].legs[0].distance.inMeters/1609.344);
            walkingMilesNum = Math.round(walkingMilesNum*100.0)/100.0;
            milesSaved.setText("Save ".concat(String.valueOf(drivingMilesNum)).concat(" Miles!"));
        }
    }

    private GeoApiContext createGeoApiContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(5)
                .setApiKey(placesAutoCompleteApiKey)
                .setConnectTimeout(5, TimeUnit.SECONDS)
                .setReadTimeout(5, TimeUnit.SECONDS)
                .setWriteTimeout(5, TimeUnit.SECONDS);
    }

    private ArrayList<LatLng> convertTypes(List<com.google.maps.model.LatLng> langLngWalkingList) {
        ArrayList<LatLng> newLatLngs = new ArrayList<LatLng>();
        for(com.google.maps.model.LatLng latLng : langLngWalkingList) {
            newLatLngs.add(new LatLng(latLng.lat, latLng.lng));
        }
        return newLatLngs;
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}