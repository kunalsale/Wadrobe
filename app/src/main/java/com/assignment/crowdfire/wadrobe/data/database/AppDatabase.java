package com.assignment.crowdfire.wadrobe.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.assignment.crowdfire.wadrobe.data.dao.BottomWearDao;
import com.assignment.crowdfire.wadrobe.data.dao.FavoriteCombinationDao;
import com.assignment.crowdfire.wadrobe.data.dao.TopWearDao;
import com.assignment.crowdfire.wadrobe.data.entities.BottomWearModel;
import com.assignment.crowdfire.wadrobe.data.entities.FavoriteModel;
import com.assignment.crowdfire.wadrobe.data.entities.TopWearModel;

/**
 * Created by hp on 21-05-2018.
 */

@Database(entities = {TopWearModel.class, BottomWearModel.class, FavoriteModel.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String DATABASE_NAME = "wadrobe_db";
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .build();
        }
        return INSTANCE;
    }
    public abstract TopWearDao    getTopWearDao();
    public abstract BottomWearDao getBottomWearDao();
    public abstract FavoriteCombinationDao getFavoriteWearDao();
}
