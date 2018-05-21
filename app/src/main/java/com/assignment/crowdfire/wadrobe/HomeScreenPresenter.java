package com.assignment.crowdfire.wadrobe;

import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import com.assignment.crowdfire.wadrobe.utils.AppConstant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hp on 21-05-2018.
 */

public class HomeScreenPresenter implements Parcelable{

    private int topWearPagerPosition;
    private int bottomWearPagerPosition;
    private String selectedOption;
    private String currentImagePath;

    public HomeScreenPresenter() {
    }

    protected HomeScreenPresenter(Parcel in) {
        topWearPagerPosition = in.readInt();
        bottomWearPagerPosition = in.readInt();
        selectedOption = in.readString();
        currentImagePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(topWearPagerPosition);
        dest.writeInt(bottomWearPagerPosition);
        dest.writeString(selectedOption);
        dest.writeString(currentImagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HomeScreenPresenter> CREATOR = new Creator<HomeScreenPresenter>() {
        @Override
        public HomeScreenPresenter createFromParcel(Parcel in) {
            return new HomeScreenPresenter(in);
        }

        @Override
        public HomeScreenPresenter[] newArray(int size) {
            return new HomeScreenPresenter[size];
        }
    };

    public int getTopWearPagerPosition() {
        return topWearPagerPosition;
    }

    public void setTopWearPagerPosition(int topWearPagerPosition) {
        this.topWearPagerPosition = topWearPagerPosition;
    }

    public int getBottomWearPagerPosition() {
        return bottomWearPagerPosition;
    }

    public void setBottomWearPagerPosition(int bottomWearPagerPosition) {
        this.bottomWearPagerPosition = bottomWearPagerPosition;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getCurrentImagePath() {
        return currentImagePath;
    }

    public void setCurrentImagePath(String currentImagePath) {
        this.currentImagePath = currentImagePath;
    }

    public File createImageFile() throws IOException {
        String jpg = ".jpg";
        // Create an image file name
        String timeStamp = new SimpleDateFormat(AppConstant.FILE_DATE_PATTERN, Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                jpg,         /* suffix */
                storageDir      /* directory */
        );
        setCurrentImagePath(image.getAbsolutePath());
        return image;
    }

}
