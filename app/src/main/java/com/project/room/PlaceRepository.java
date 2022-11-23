package com.project.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.project.model.PlacesModel;

import java.util.List;

public class PlaceRepository {
    private Dao dao;
    private LiveData<List<PlacesModel>> allCourses;

    public PlaceRepository(Application application) {
        PlaceDatabase database = PlaceDatabase.getInstance(application);
        dao = database.Dao();
        allCourses = dao.getAllCourses();
    }

    public void insert(PlacesModel model) {
        new InsertCourseAsyncTask(dao).execute(model);
    }

    public void update(PlacesModel model) {
        new UpdateCourseAsyncTask(dao).execute(model);
    }

    public void delete(PlacesModel model) {
        new DeleteCourseAsyncTask(dao).execute(model);
    }

    public void deleteAllCourses() {
        new DeleteAllCoursesAsyncTask(dao).execute();
    }

    public LiveData<List<PlacesModel>> getAllCourses() {
        return allCourses;
    }
    public LiveData<List<PlacesModel>> getSingleCourses(String name) {
        allCourses = dao.getSingleCourses(name);
        return allCourses;
    }

    private static class InsertCourseAsyncTask extends AsyncTask<PlacesModel, Void, Void> {
        private Dao dao;

        private InsertCourseAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PlacesModel... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }

    private static class UpdateCourseAsyncTask extends AsyncTask<PlacesModel, Void, Void> {
        private Dao dao;

        private UpdateCourseAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PlacesModel... models) {
            dao.update(models[0]);
            return null;
        }
    }

    private static class DeleteCourseAsyncTask extends AsyncTask<PlacesModel, Void, Void> {
        private Dao dao;

        private DeleteCourseAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PlacesModel... models) {
            dao.delete(models[0]);
            return null;
        }
    }

    private static class DeleteAllCoursesAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllCoursesAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllCourses();
            return null;
        }
    }

}
