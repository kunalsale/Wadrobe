package com.assignment.crowdfire.wadrobe.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.assignment.crowdfire.wadrobe.data.entities.BottomWearModel;
import com.assignment.crowdfire.wadrobe.data.entities.FavoriteModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by hp on 21-05-2018.
 */

@Dao
public interface FavoriteCombinationDao {
    @Query("select * from favoriteModel")
    List<FavoriteModel> getFavoriteWearModels();

    @Insert
    void insertAll(FavoriteModel... favoriteModels);

    @Insert(onConflict = REPLACE)
    long insertFavoriteWear(FavoriteModel favoriteModel);

    @Query("select * from favoriteModel where imgPathForTopWear LIKE :imgTopWearPath AND imgPathForBottomWear LIKE :imgBottomWearPath")
    FavoriteModel getFavoriteWearModel(String imgTopWearPath,String imgBottomWearPath);
}
