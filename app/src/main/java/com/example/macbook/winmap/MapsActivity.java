package com.example.macbook.winmap;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener{

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            AddStore();

            googleMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));

            mListMarkers = new Marker[mListStoreAffiche.size()];
            for (int i = 0; i < mListStoreAffiche.size(); i++) {
                mListMarkers[i] = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mListStoreAffiche.get(i).getStoreLatitude(),
                                mListStoreAffiche.get(i).getStoreLongitude()))
                        .title(mListStoreAffiche.get(i).getCompanyName()));
            }

            init();
        }
    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
            new LatLng(-40, -168), new LatLng(71, 136));


    //widgets
    private AutoCompleteTextView mSearchText;
    private ImageView mGps;
    private FloatingActionButton mFabAdd;
    private RelativeLayout mRelLayout1;
    private RelativeLayout mRelLayout2;
    private RelativeLayout mRelLayout3;
    private RelativeLayout mRelLayout4;
    private RelativeLayout mRelLayout5;
    private RelativeLayout mRelLayout6;
    private RelativeLayout mRelLayout7;
    private EditText mInputCompany;
    private EditText mInputSubType;
    private EditText mInputSchedule;
    private Spinner mActivitySpinner;
    private Button mAddCompany;
    private Button mAnnular;
    private Marker[] mListMarkers;
    private ArrayList<StoreModel> mListStoreAffiche = new ArrayList<>();
    private ArrayList<StoreModel> mListStore = new ArrayList<>();

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences mySharedPref;
    private String mEmailEncode;
    private String mEmailDecode;

    //firebase
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mSearchText = (AutoCompleteTextView) findViewById(R.id.input_search);
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mFabAdd = (FloatingActionButton) findViewById(R.id.fab_add);
        mRelLayout1 = (RelativeLayout) findViewById(R.id.relLayout1);
        mInputCompany = (EditText) findViewById(R.id.input_company);
        mRelLayout2 = (RelativeLayout) findViewById(R.id.relLayout2);
        mInputSubType = (EditText) findViewById(R.id.input_sub_type);
        mInputSchedule = (EditText) findViewById(R.id.input_schedule);
        mRelLayout3 = (RelativeLayout) findViewById(R.id.relLayout3);
        mRelLayout4 = (RelativeLayout) findViewById(R.id.relLayout4);
        mRelLayout5 = (RelativeLayout) findViewById(R.id.relLayout5);
        mRelLayout6 = (RelativeLayout) findViewById(R.id.relLayout6);
        mRelLayout7 = (RelativeLayout) findViewById(R.id.relLayout7);
        mActivitySpinner = (Spinner) findViewById(R.id.activity_spinner);
        mAddCompany = (Button) findViewById(R.id.bt_add_company);
        mAnnular = (Button) findViewById(R.id.bt_annular);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActivitySpinner.setAdapter(adapter);




        mySharedPref = getSharedPreferences("SP", MODE_PRIVATE);
        mEmailDecode = mySharedPref.getString("email", "");
        mEmailEncode = mEmailDecode.replace(".",",");



       /* if (mEmailDecode.equals("chihaoui1098@gmail.com")) {
            mFabAdd.setVisibility(View.GONE);
        }*/

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Store/"+mEmailEncode);
        getLocationPermission();

        mListStore = SingletonStore.getInstance().getListStore();
        for (int i = 0; i < mListStore.size(); i++) {
                mListStoreAffiche.add(mListStore.get(i));
        }
    }

    private void init(){
        Log.d(TAG, "init: initializing");

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,
                LAT_LNG_BOUNDS, null);

        mSearchText.setAdapter(mPlaceAutocompleteAdapter);


        mAddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //execute our method for searching
                    geoLocate();

            }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();
            }
        });

        hideSoftKeyboard();
    }

    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));

            Double storeLatitude = address.getLatitude();
            Double storeLongitude = address.getLongitude();

            StoreModel user = new StoreModel(
                    mInputCompany.getText().toString(),
                    mSearchText.getText().toString(),
                    mActivitySpinner.getSelectedItem().toString(),
                    mInputSubType.getText().toString(),
                    mInputSchedule.getText().toString(),
                    storeLatitude,
                    storeLongitude);
            DatabaseReference userRef = mFirebaseDatabase.getReference("Store");
            userRef.child(mEmailEncode).setValue(user);

        }else{
            Toast.makeText(MapsActivity.this, "oups mauvaise adresse", Toast.LENGTH_LONG).show();
        }
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void AddStore(){
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRelLayout1.getVisibility() == View.GONE) {
                    mRelLayout1.setVisibility(View.VISIBLE);
                    mRelLayout2.setVisibility(View.VISIBLE);
                    mRelLayout3.setVisibility(View.VISIBLE);
                    mRelLayout4.setVisibility(View.VISIBLE);
                    mRelLayout5.setVisibility(View.VISIBLE);
                    mRelLayout6.setVisibility(View.VISIBLE);
                    mRelLayout7.setVisibility(View.VISIBLE);
                }else {
                    mRelLayout1.setVisibility(View.GONE);
                    mRelLayout2.setVisibility(View.GONE);
                    mRelLayout3.setVisibility(View.GONE);
                    mRelLayout4.setVisibility(View.GONE);
                    mRelLayout5.setVisibility(View.GONE);
                    mRelLayout6.setVisibility(View.GONE);
                    mRelLayout7.setVisibility(View.GONE);
                }
            }
        });
    }
}