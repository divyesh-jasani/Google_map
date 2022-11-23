package com.project.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.project.model.PlacesModel;

import java.util.List;

public class ViewModal extends AndroidViewModel {

    private PlaceRepository repository;

    private LiveData<List<PlacesModel>> allCourses;

    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new PlaceRepository(application);
        allCourses = repository.getAllCourses();
    }

    public void insert(PlacesModel model) {
        repository.insert(model);
    }

    public void update(PlacesModel model) {
        repository.update(model);
    }

    public void delete(PlacesModel model) {
        repository.delete(model);
    }

    public void deleteAllPlaces() {
        repository.deleteAllCourses();
    }

    public LiveData<List<PlacesModel>> getAllPlaces() {
        return allCourses;
    }

    public LiveData<List<PlacesModel>> getSinglePlaces(String name) {
        allCourses = repository.getSingleCourses(name);
        return allCourses;
    }
}