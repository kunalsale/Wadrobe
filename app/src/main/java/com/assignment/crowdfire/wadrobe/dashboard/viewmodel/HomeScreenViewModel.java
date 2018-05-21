package com.assignment.crowdfire.wadrobe.dashboard.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.assignment.crowdfire.wadrobe.data.database.AppDatabase;
import com.assignment.crowdfire.wadrobe.data.entities.BottomWearModel;
import com.assignment.crowdfire.wadrobe.data.entities.FavoriteModel;
import com.assignment.crowdfire.wadrobe.data.entities.TopWearModel;

import java.util.List;

/**
 * Created by hp on 20-05-2018.
 */

public class HomeScreenViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> isCombinationFavorite = new MutableLiveData<>();
    private MutableLiveData<List<TopWearModel>> mTopWearModelList = new MutableLiveData<>();
    private MutableLiveData<List<BottomWearModel>> mBottomWearModelList = new MutableLiveData<>();

    private AppDatabase           appDatabase;
    private DBOperationsAsyncTask dbOperationsAsyncTask;

    public HomeScreenViewModel(@NonNull Application application) {
        super(application);
        appDatabase = AppDatabase.getDatabase(application);
        loadTopWearList();
        loadBottomWearList();
    }

    public MutableLiveData<Boolean> getIsCombinationFavorite() {
        return isCombinationFavorite;
    }

    public void setIsCombinationFavorite(MutableLiveData<Boolean> isCombinationFavorite) {
        this.isCombinationFavorite = isCombinationFavorite;
    }

    public MutableLiveData<List<TopWearModel>> getTopWearModelList() {
        if (mTopWearModelList == null) {
            mTopWearModelList = new MutableLiveData<>();
        }
        return mTopWearModelList;
    }

    public MutableLiveData<List<BottomWearModel>> getBottomWearModelList() {
        if (mBottomWearModelList == null) {
            mBottomWearModelList = new MutableLiveData<>();
        }
        return mBottomWearModelList;
    }

    private void loadTopWearList(){
        executeDBOperation(allTopWearRunnable());
    }

    private void loadBottomWearList(){
        executeDBOperation(allBottomWearRunnable());
    }

    /*-------- methods exposed to called from ui -----------*/
    public void insertTopWearToDB(TopWearModel topWearModel){
        executeDBOperation(topWearInsertionRunnable(topWearModel));
    }

    public void insertBottomWearToDB(BottomWearModel bottomWearModel){
        executeDBOperation(bottomWearInsertionRunnable(bottomWearModel));
    }

    public void insertFavoriteWearToDB(FavoriteModel favoriteModel){
        executeDBOperation(favoriteWearInsertionRunnable(favoriteModel));
    }

    public void checkIsCombinationFavorite(String imgPathTopWear,String imgPathBottomWear){
        executeDBOperation(favoriteValidationRunnable(imgPathTopWear,imgPathBottomWear));
    }

    /*---------- DB Operation Runnable ----------*/
    private Runnable topWearInsertionRunnable(final TopWearModel topWearModel){
        return new Runnable(){
            @Override
            public void run() {
                appDatabase.getTopWearDao().insertTopWear(topWearModel);
            }
        };
    }

    private Runnable allTopWearRunnable(){
        return new Runnable(){
            @Override
            public void run() {
                mTopWearModelList.postValue(appDatabase.getTopWearDao().getTopWearModels());
            }
        };
    }

    private Runnable bottomWearInsertionRunnable(final BottomWearModel bottomWearModel){
        return new Runnable(){
            @Override
            public void run() {
                appDatabase.getBottomWearDao().insertBottomWear(bottomWearModel);
            }
        };
    }

    private Runnable allBottomWearRunnable(){
        return new Runnable(){
            @Override
            public void run() {
                mBottomWearModelList.postValue(appDatabase.getBottomWearDao().getBottomWearModels());
            }
        };
    }

    private Runnable favoriteWearInsertionRunnable(final FavoriteModel favoriteModel){
        return new Runnable(){
            @Override
            public void run() {
                isCombinationFavorite.postValue(appDatabase.getFavoriteWearDao().insertFavoriteWear(favoriteModel) > 0);
            }
        };
    }

    private Runnable favoriteValidationRunnable(final String imgPathTopWear, final String imgPathBottomWear){
        return new Runnable(){
            @Override
            public void run() {
                FavoriteModel favoriteModel = appDatabase.getFavoriteWearDao().getFavoriteWearModel(imgPathTopWear,imgPathBottomWear);
                isCombinationFavorite.postValue(favoriteModel != null);
            }
        };
    }

    private void executeDBOperation(Runnable dbOperationRunnable){
        if(dbOperationsAsyncTask == null)
        {
            dbOperationsAsyncTask = new DBOperationsAsyncTask();
        }
        dbOperationsAsyncTask.execute(dbOperationRunnable);
    }

    /*----------- Asynctask for db operation---------*/
    private static class DBOperationsAsyncTask extends AsyncTask<Runnable,Void,Void>
    {
        @Override
        protected Void doInBackground(Runnable... runnables) {
            Runnable dbOperationRunnable = runnables[0];
            dbOperationRunnable.run();
            return null;
        }
    }

    public void saveFavoriteToDB(int topPagerPostion,int bottomPagerPosition) {
        List<TopWearModel> topWearModelArrayList = mTopWearModelList.getValue();
        List<BottomWearModel> bottomWearModelArrayList = mBottomWearModelList.getValue();
        if(topWearModelArrayList != null && bottomWearModelArrayList != null){
            int topWearArraySize = topWearModelArrayList.size();
            int bottomWearArraySize = bottomWearModelArrayList.size();
            if(topWearArraySize == 0){
                Toast.makeText(this.getApplication(), "Please select top wear",Toast.LENGTH_SHORT).show();
            }

            if(bottomWearArraySize == 0){
                Toast.makeText(this.getApplication(), "Please select bottom wear",Toast.LENGTH_SHORT).show();
            }

            if(bottomWearArraySize > bottomPagerPosition && topWearArraySize > topPagerPostion){
                TopWearModel topWearModel       = topWearModelArrayList.get(topPagerPostion);
                BottomWearModel bottomWearModel = bottomWearModelArrayList.get(bottomPagerPosition);

                if(topWearModel != null && bottomWearModel != null){
                    String topWearImagePath = topWearModel.getImgPath();
                    String bottomWearImagePath = bottomWearModel.getImgPath();

                    FavoriteModel favoriteModel = new FavoriteModel(topWearImagePath,bottomWearImagePath);
                    insertFavoriteWearToDB(favoriteModel);
                }
            }
        }
    }


    public void checkIfCombinationFavorite( int topPagerPostion, int bottomPagerPosition){
        List<TopWearModel> topWearModelArrayList = mTopWearModelList.getValue();
        List<BottomWearModel> bottomWearModelArrayList = mBottomWearModelList.getValue();
        if(topWearModelArrayList != null && bottomWearModelArrayList != null){
            int topWearArraySize = topWearModelArrayList.size();
            int bottomWearArraySize = bottomWearModelArrayList.size();
            if(bottomWearArraySize > bottomPagerPosition && topWearArraySize > topPagerPostion){
                TopWearModel topWearModel       = topWearModelArrayList.get(topPagerPostion);
                BottomWearModel bottomWearModel = bottomWearModelArrayList.get(bottomPagerPosition);

                if(topWearModel != null && bottomWearModel != null){
                    String topWearImagePath = topWearModel.getImgPath();
                    String bottomWearImagePath = bottomWearModel.getImgPath();
                    checkIsCombinationFavorite(topWearImagePath,bottomWearImagePath);
                }
            }
        }
    }
}
