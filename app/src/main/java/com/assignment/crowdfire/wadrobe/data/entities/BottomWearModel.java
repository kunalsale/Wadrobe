package com.assignment.crowdfire.wadrobe.data.entities;

import android.arch.persistence.room.Entity;

/**
 * Created by hp on 21-05-2018.
 */

@Entity
public class BottomWearModel extends ClothesModel{
    public BottomWearModel(String imgPath) {
        super(imgPath);
    }
}
