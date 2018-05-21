package com.assignment.crowdfire.wadrobe.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.assignment.crowdfire.wadrobe.data.entities.BottomWearModel;
import com.assignment.crowdfire.wadrobe.data.entities.ClothesModel;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by hp on 21-05-2018.
 */

@Dao
public interface BottomWearDao {
    @Query("select * from bottomWearModel")
    List<BottomWearModel> getBottomWearModels();

    @Insert
    void insertAll(BottomWearModel... bottomWearModels);

    @Insert(onConflict = REPLACE)
    void insertBottomWear(BottomWearModel bottomWearModel);
}
