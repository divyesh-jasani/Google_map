package com.project.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.android.gms.maps.model.LatLng;

@Entity(tableName = "place_tbl")
public class PlacesModel {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String placeName;
    private String placeAddress;
    private String addressComponent;
    private Double latitude;
    private Double longitude;

    public PlacesModel(String placeName, String placeAddress, String addressComponent, Double latitude,Double longitude) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.addressComponent = addressComponent;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(String addressComponent) {
        this.addressComponent = addressComponent;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
