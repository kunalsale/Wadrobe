package com.assignment.crowdfire.wadrobe.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by hp on 21-05-2018.
 */

@Entity
public class FavoriteModel {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "imgPathForTopWear")
    private String imgPathForTopWear;

    @ColumnInfo(name = "imgPathForBottomWear")
    private String imgPathForBottomWear;

    public FavoriteModel(String imgPathForTopWear, String imgPathForBottomWear) {
        this.imgPathForTopWear = imgPathForTopWear;
        this.imgPathForBottomWear = imgPathForBottomWear;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getImgPathForTopWear() {
        return imgPathForTopWear;
    }

    public void setImgPathForTopWear(String imgPathForTopWear) {
        this.imgPathForTopWear = imgPathForTopWear;
    }

    public String getImgPathForBottomWear() {
        return imgPathForBottomWear;
    }

    public void setImgPathForBottomWear(String imgPathForBottomWear) {
        this.imgPathForBottomWear = imgPathForBottomWear;
    }
}
