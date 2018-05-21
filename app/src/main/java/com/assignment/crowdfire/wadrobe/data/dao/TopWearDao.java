package com.assignment.crowdfire.wadrobe.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.assignment.crowdfire.wadrobe.data.entities.ClothesModel;
import com.assignment.crowdfire.wadrobe.data.entities.TopWearModel;

import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by hp on 21-05-2018.
 */

@Dao
public interface TopWearDao {

    @Query("select * from topWearModel")
    List<TopWearModel> getTopWearModels();

    @Insert
    void insertAll(TopWearModel... topWearModels);

    @Insert(onConflict = REPLACE)
    void insertTopWear(TopWearModel topWearModel);
}
