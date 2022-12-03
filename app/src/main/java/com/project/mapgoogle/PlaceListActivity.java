package com.project.mapgoogle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;
import com.project.Constants;
import com.project.SortPlaces;
import com.project.model.PlacesModel;
import com.project.room.ViewModal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class PlaceListActivity extends AppCompatActivity {

    RecyclerView rvPlaceDetails;
    TextView tvEmpty;
    private static final int ADD_COURSE_REQUEST = 1;
    private static final int EDIT_COURSE_REQUEST = 2;
    private static final int REQUEST_CODE = 1;
    private ViewModal viewmodal;
    ArrayList<Place> places = new ArrayList<>();
    ArrayList<PlacesModel> placesModels = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    PlacesRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        tvEmpty = findViewById(R.id.tvEmpty);
        rvPlaceDetails = findViewById(R.id.rvPlaceDetails);
        rvPlaceDetails.setLayoutManager(new LinearLayoutManager(this));
        rvPlaceDetails.setHasFixedSize(true);

        adapter = new PlacesRVAdapter();

        rvPlaceDetails.setAdapter(adapter);

        viewmodal = ViewModelProviders.of(this).get(ViewModal.class);

        viewmodal.getAllPlaces().observe(this, new Observer<List<PlacesModel>>() {
            @Override
            public void onChanged(List<PlacesModel> models) {

                if (models.size() > 0) {
                    rvPlaceDetails.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    rvPlaceDetails.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                }
                adapter.submitList(models);

                for (PlacesModel placesModel : models) {
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
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                viewmodal.delete(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                Toast.makeText(PlaceListActivity.this, "Course deleted", Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setOnItemClickListener(new PlacesRVAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(PlacesModel model, String clickType) {

                if (clickType.equals(Constants.VIEW_CLICK)) {
                    Intent intent = new Intent(PlaceListActivity.this, MapsActivity.class);
                    intent.putExtra("address", model.getPlaceAddress());
                    intent.putExtra("addComponent", model.getAddressComponent());
                    intent.putExtra("name", model.getPlaceName());
                    intent.putExtra("clickType", Constants.VIEW_CLICK);
                    startActivity(intent);

                } else if (clickType.equals(Constants.EDIT)) {
                    Intent intent = new Intent(PlaceListActivity.this, MapsActivity.class);
                    intent.putExtra("address", model.getPlaceAddress());
                    intent.putExtra("addComponent", model.getAddressComponent());
                    intent.putExtra("name", model.getPlaceName());
                    intent.putExtra("clickType", Constants.EDIT);
                    startActivity(intent);

                } else if (clickType.equals(Constants.DELETE)) {
                    viewmodal.delete(model);
                    Toast.makeText(PlaceListActivity.this, "Place delete", Toast.LENGTH_SHORT).show();
                }
//                intent.putExtra(NewCourseActivity.EXTRA_ID, model.getId());
//                intent.putExtra(NewCourseActivity.EXTRA_COURSE_NAME, model.getCourseName());
//                intent.putExtra(NewCourseActivity.EXTRA_DESCRIPTION, model.getCourseDescription());
//                intent.putExtra(NewCourseActivity.EXTRA_DURATION, model.getCourseDuration());


//                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

    LatLng currLocation;

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
//                    Toast.makeText(PlaceListActivity.this, location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    currLocation = new LatLng(location.getLatitude(), location.getLongitude());

                }
            }
        });

    }

    public void onBackClick(View view) {
        onBackPressed();
    }

    public void onsortClick(View view) {
        final Dialog dialog = new Dialog(PlaceListActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_sort);

        final RadioButton[] genderradioButton = new RadioButton[1];
        RadioGroup radioGroup;

        radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesModels = new ArrayList<>();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                genderradioButton[0] = (RadioButton) dialog.findViewById(selectedId);
                if (selectedId == -1) {
                    Toast.makeText(PlaceListActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                } else if (genderradioButton[0].getText().equals("Ascending")) {
                    Collections.sort(places, new SortPlaces(currLocation));
                    for (Place place : places) {
                        placesModels.add(new PlacesModel(place.getName(), place.getAddress(), "", place.getLatLng().latitude, place.getLatLng().longitude));
                    }

                    adapter.submitList(placesModels);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(PlaceListActivity.this, genderradioButton[0].getText(), Toast.LENGTH_SHORT).show();
                } else {
                    Collections.sort(places, new SortPlaces(currLocation));
                    Collections.reverse(places);
                    for (Place place : places) {
                        placesModels.add(new PlacesModel(place.getName(), place.getAddress(), "", place.getLatLng().latitude, place.getLatLng().longitude));
                    }

                    adapter.submitList(placesModels);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(PlaceListActivity.this, genderradioButton[0].getText(), Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onRoutClick(View view) {
        Intent intent = new Intent(PlaceListActivity.this, MapsActivity.class);
        intent.putExtra("address", "");
        intent.putExtra("addComponent", "");
        intent.putExtra("name", "");
        intent.putExtra("clickType", Constants.ALL_ROUT);
        startActivity(intent);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
//            String courseName = data.getStringExtra(NewCourseActivity.EXTRA_COURSE_NAME);
//            String courseDescription = data.getStringExtra(NewCourseActivity.EXTRA_DESCRIPTION);
//            String courseDuration = data.getStringExtra(NewCourseActivity.EXTRA_DURATION);
//            PlacesModel model = new PlacesModel(courseName, courseDescription, courseDuration);
//            viewmodal.insert(model);
//            Toast.makeText(this, "Course saved", Toast.LENGTH_SHORT).show();
//        } else if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {
//            int id = data.getIntExtra(NewCourseActivity.EXTRA_ID, -1);
//            if (id == -1) {
//                Toast.makeText(this, "Course can't be updated", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            String courseName = data.getStringExtra(NewCourseActivity.EXTRA_COURSE_NAME);
//            String courseDesc = data.getStringExtra(NewCourseActivity.EXTRA_DESCRIPTION);
//            String courseDuration = data.getStringExtra(NewCourseActivity.EXTRA_DURATION);
//            CourseModal model = new CourseModal(courseName, courseDesc, courseDuration);
//            model.setId(id);
//            viewmodal.update(model);
//            Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Course not saved", Toast.LENGTH_SHORT).show();
//        }
//    }
}