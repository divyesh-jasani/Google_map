package com.project.mapgoogle;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.project.Constants;
import com.project.SortPlaces;
import com.project.model.PlacesModel;
import com.project.room.ViewModal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    private static final int REQUEST_CODE = 1;
    //direction
    protected LatLng start = null;
    protected LatLng end = null;
    Button ivShowAllList;
    //for current location
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    Place place, sourcePlace, destinationPlace;
    PlacesModel placesModel = null;
    String address;
    String addComponent;
    String name;
    String letLog;
    int id;
    String clickType;
    private GoogleMap mMap;
    private FloatingActionButton floatingMyLocation, floatingDirection;
    private EditText searchAddress, source, destination;
    private ImageView modeSatelight, modeNormal;
    //polyline object
    private List<Polyline> polylines = null;
    private ViewModal viewmodal;
    LatLng currLocation;
    ArrayList<PlacesModel> placesModels = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Object fromString(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
    ArrayList<Place> places = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        floatingMyLocation = findViewById(R.id.floatingMyLocation);
        modeSatelight = findViewById(R.id.modeSatelight);
        modeNormal = findViewById(R.id.modeNormal);
        searchAddress = findViewById(R.id.searchAddress);
        floatingDirection = findViewById(R.id.floatingDirection);
        destination = findViewById(R.id.destination);
        source = findViewById(R.id.source);
        ivShowAllList = findViewById(R.id.ivShowAllList);

        viewmodal = ViewModelProviders.of(this).get(ViewModal.class);

        if (getIntent() != null) {
            address = getIntent().getStringExtra("address");
            addComponent = getIntent().getStringExtra("addComponent");
            name = getIntent().getStringExtra("name");
            letLog = getIntent().getStringExtra("letLog");
            clickType = getIntent().getStringExtra("clickType");

            if (clickType.equals(Constants.ALL_ROUT)) {
                viewmodal.getAllPlaces().observe(this, new Observer<List<PlacesModel>>() {
                    @Override
                    public void onChanged(List<PlacesModel> models) {

                        for (PlacesModel placesModel: models) {
                            places.add(new Place() {
                                @Nullable
                                @Override
                                public Uri getWebsiteUri() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public LatLng getLatLng() {
                                    return new LatLng(placesModel.getLatitude(), placesModel.getLongitude());
                                }

                                @Nullable
                                @Override
                                public LatLngBounds getViewport() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public AddressComponents getAddressComponents() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public OpeningHours getOpeningHours() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public BusinessStatus getBusinessStatus() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public PlusCode getPlusCode() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public Double getRating() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public Integer getIconBackgroundColor() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public Integer getPriceLevel() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public Integer getUserRatingsTotal() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public Integer getUtcOffsetMinutes() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public String getAddress() {
                                    return placesModel.getPlaceAddress();
                                }

                                @Nullable
                                @Override
                                public String getIconUrl() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public String getId() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public String getName() {
                                    return placesModel.getPlaceName();
                                }

                                @Nullable
                                @Override
                                public String getPhoneNumber() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public List<String> getAttributions() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public List<PhotoMetadata> getPhotoMetadatas() {
                                    return null;
                                }

                                @Nullable
                                @Override
                                public List<Type> getTypes() {
                                    return null;
                                }

                                @Override
                                public int describeContents() {
                                    return 0;
                                }

                                @Override
                                public void writeToParcel(Parcel parcel, int i) {

                                }
                            });
                        }
                        Collections.sort(places, new SortPlaces(currLocation));
                        for (Place place:places) {
                            placesModels.add(new PlacesModel(place.getName(),place.getAddress(),"",place.getLatLng().latitude,place.getLatLng().longitude));
                        }

                        for (PlacesModel placesModel:placesModels){
                            if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(MapsActivity.this, new String[]
                                        {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                                return;
                            }
                            mMap.addMarker(new MarkerOptions().title(placesModel.getPlaceName()).position(new LatLng(placesModel.getLatitude(),placesModel.getLongitude())));
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, -5));
                    }
                });
            }else {
                viewmodal = ViewModelProviders.of(this).get(ViewModal.class);
                viewmodal.getSinglePlaces(name).observe(this, new Observer<List<PlacesModel>>() {
                    @Override
                    public void onChanged(List<PlacesModel> models) {
                        searchAddress.setFocusable(true);
                        searchAddress.setText(address);
                        LatLng loc = new LatLng(models.get(0).getLatitude(), models.get(0).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(loc).title(name));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
                        id = models.get(0).getId();
                    }
                });
            }

        }
        ivShowAllList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, PlaceListActivity.class);
                startActivity(intent);
            }
        });

        //for current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //for search in map
//        Places.initialize(getApplicationContext(), "AIzaSyCTN3we7lszdsULFsUZKunGpwXTXCQYFag");
        Places.initialize(getApplicationContext(), "AIzaSyCg-f707-2pz-W_tJszHvQxuX3ZAFEOUMk");
        searchAddress.setFocusable(false);
        searchAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);


                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList)
                        .build(MapsActivity.this);
                startActivityForResult(intent, 100);

            }
        });

        source.setFocusable(false);
        source.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fieldListSource = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldListSource)
                        .build(MapsActivity.this);
                startActivityForResult(intent, 200);

            }
        });

        destination.setFocusable(false);
        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fieldListDestination = Arrays.asList(Place.Field.ADDRESS,
                        Place.Field.LAT_LNG, Place.Field.NAME);

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldListDestination)
                        .build(MapsActivity.this);
                startActivityForResult(intent, 300);

            }
        });

        floatingDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                source.setVisibility(View.VISIBLE);
                destination.setVisibility(View.VISIBLE);
                searchAddress.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//         Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(22.227258, 72.833592);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                end = latLng;
                mMap.clear();

                start = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                //start route finding
                Findroutes(start, end);
            }
        });

        floatingMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LatLng loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(loc).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));

                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                supportMapFragment.getMapAsync(MapsActivity.this);
            }
        });
    }

    //for current location
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(MapsActivity.this, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    currLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currLocation).title("My Location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currLocation, 15));

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });

        modeSatelight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                modeSatelight.setVisibility(View.GONE);
                modeNormal.setVisibility(View.VISIBLE);
            }
        });
        modeNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                modeSatelight.setVisibility(View.VISIBLE);
                modeNormal.setVisibility(View.GONE);
            }
        });

    }

    //for current location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }

    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(MapsActivity.this, "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key(getString(R.string.google_maps_key))  //also define your api key here.
//                    .key("AIzaSyCTN3we7lszdsULFsUZKunGpwXTXCQYFag")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Override
    public void onRoutingStart() {
        Toast.makeText(MapsActivity.this, "Finding Route...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;
        polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.black));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = mMap.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);

            } else {

            }
        }

        MarkerOptions startMarker = new MarkerOptions();
        startMarker.position(polylineStartLatLng);
        startMarker.title("My Location");
        mMap.addMarker(startMarker);

        MarkerOptions endMarker = new MarkerOptions();
        endMarker.position(polylineEndLatLng);
        endMarker.title("Destination");
        mMap.addMarker(endMarker);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start, end);
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(parentLayout, e.toString(), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    //for search in map
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {

            place = Autocomplete.getPlaceFromIntent(data);

            if (clickType.equals(Constants.VIEW_CLICK)) {
                PlacesModel model = new PlacesModel(place.getName(), place.getAddress(), place.getAttributions().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                viewmodal.insert(model);
                Toast.makeText(this, "Place insert", Toast.LENGTH_SHORT).show();
            } else {
                PlacesModel model = new PlacesModel(place.getName(), place.getAddress(), place.getAttributions().toString(), place.getLatLng().latitude, place.getLatLng().longitude);
                model.setId(id);
                viewmodal.update(model);
                Toast.makeText(this, "Place update", Toast.LENGTH_SHORT).show();
            }

//            searchAddress.setText(place.getAddress());
//            mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName()));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),10));

            Intent intent = new Intent(MapsActivity.this, PlaceListActivity.class);
            startActivity(intent);
            finish();
        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Status", status.getStatusMessage());
        }

        if (requestCode == 200 && resultCode == RESULT_OK) {
            sourcePlace = Autocomplete.getPlaceFromIntent(data);
            source.setText(sourcePlace.getAddress());

        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Status", status.getStatusMessage());
        }


        if (requestCode == 300 && resultCode == RESULT_OK) {


            destinationPlace = Autocomplete.getPlaceFromIntent(data);
            destination.setText(destinationPlace.getAddress());

            if (!source.getText().toString().trim().isEmpty()) {
                mMap.clear();
                LatLng sLatLng = sourcePlace.getLatLng();
                LatLng dLatLng = destinationPlace.getLatLng();

                Findroutes(sLatLng, dLatLng);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sourcePlace.getLatLng(), 10));
            }

        } else if (requestCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Status", status.getStatusMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        source.setVisibility(View.GONE);
        destination.setVisibility(View.GONE);
        searchAddress.setVisibility(View.VISIBLE);

    }

}