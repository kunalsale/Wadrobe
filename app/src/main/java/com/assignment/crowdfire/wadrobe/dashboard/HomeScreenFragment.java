package com.assignment.crowdfire.wadrobe.dashboard;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.crowdfire.wadrobe.HomeScreenPresenter;
import com.assignment.crowdfire.wadrobe.R;
import com.assignment.crowdfire.wadrobe.dashboard.adapters.HomeScreenPagerAdapter;
import com.assignment.crowdfire.wadrobe.dashboard.viewmodel.HomeScreenViewModel;
import com.assignment.crowdfire.wadrobe.data.entities.BottomWearModel;
import com.assignment.crowdfire.wadrobe.data.entities.TopWearModel;
import com.assignment.crowdfire.wadrobe.databinding.FragmentHomeScreenLayoutBinding;
import com.assignment.crowdfire.wadrobe.utils.AppConstant;
import com.assignment.crowdfire.wadrobe.utils.CustomPageScrollListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.assignment.crowdfire.wadrobe.utils.AppConstant.FILEPROVIDER_AUTHORITY;

/**
 * Created by hp on 19-05-2018.
 */

public class HomeScreenFragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO      = 1;
    static final int REQUEST_PICK_FROM_PHOTO = 2;

    private FragmentHomeScreenLayoutBinding mViewDataBinding;

    private HomeScreenPresenter mHomeScreenPresenter;
    private HomeScreenViewModel mHomeScreenViewModel;

    // Adapters
    private HomeScreenPagerAdapter<TopWearModel> mTopWearPagerAdapter;
    private HomeScreenPagerAdapter<BottomWearModel> mBottomWearPagerAdapter;
    private final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1;

    /*------ LifeCycle Methods-------*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomeScreenPresenter = new HomeScreenPresenter();
        if(savedInstanceState != null){
            mHomeScreenPresenter = savedInstanceState.getParcelable(AppConstant.HOME_SCREEN_PRESENTER);
        }
        // Get the ViewModel.
        mHomeScreenViewModel = ViewModelProviders.of(this).get(com.assignment.crowdfire.wadrobe.dashboard.viewmodel.HomeScreenViewModel.class);
        initializeAdapters();
        addTopWearObserver();
        addBottomWearObserver();
        addFavoriteObserver();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mViewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_screen_layout, container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable( AppConstant.HOME_SCREEN_PRESENTER, mHomeScreenPresenter);
    }

     /*------ End of LifeCycle Methods-------*/

    private void initializeAdapters() {
        mTopWearPagerAdapter = new HomeScreenPagerAdapter<>(new ArrayList<TopWearModel>());
        mBottomWearPagerAdapter = new HomeScreenPagerAdapter<>(new ArrayList<BottomWearModel>());
    }

    private void addFavoriteObserver() {
        Observer<Boolean> booleanObserver = new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if(aBoolean){
                    mViewDataBinding.imgFavorite.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_favorite));
                }
                else {
                    mViewDataBinding.imgFavorite.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_favorite_border));
                }

            }
        };
        mHomeScreenViewModel.getIsCombinationFavorite().observe(this, booleanObserver);
    }

    private void addTopWearObserver() {
            final Observer<List<TopWearModel>> topWearListObserver = new Observer<List<TopWearModel>>() {
            @Override
            public void onChanged(@Nullable final List<TopWearModel> clothesModelArrayList) {
                mTopWearPagerAdapter.updateList(clothesModelArrayList);
                mViewDataBinding.viewPagerShirtsCollections.setCurrentItem(mHomeScreenPresenter.getTopWearPagerPosition());
            }
        };
        mHomeScreenViewModel.getTopWearModelList().observe(this, topWearListObserver);
    }

    private void addBottomWearObserver() {
        final Observer<List<BottomWearModel>> bottomWearListObserver = new Observer<List<BottomWearModel>>() {
            @Override
            public void onChanged(@Nullable final List<BottomWearModel> clothesModelArrayList) {
                mBottomWearPagerAdapter.updateList(clothesModelArrayList);
                mViewDataBinding.viewPagerPantCollections.setCurrentItem(mHomeScreenPresenter.getBottomWearPagerPosition());
            }
        };
        mHomeScreenViewModel.getBottomWearModelList().observe(this, bottomWearListObserver);
    }

    private void initViews() {
        mViewDataBinding.imgAddPant.setOnClickListener(this);
        mViewDataBinding.imgAddShirt.setOnClickListener(this);
        mViewDataBinding.imgFavorite.setOnClickListener(this);
        mViewDataBinding.imgShuffle.setOnClickListener(this);

        mViewDataBinding.viewPagerShirtsCollections.setAdapter(mTopWearPagerAdapter);
        mViewDataBinding.viewPagerPantCollections.setAdapter(mBottomWearPagerAdapter);

        mViewDataBinding.viewPagerShirtsCollections.addOnPageChangeListener(new CustomPageScrollListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mHomeScreenPresenter.setTopWearPagerPosition(position);
                mHomeScreenViewModel.checkIfCombinationFavorite(position,mHomeScreenPresenter.getBottomWearPagerPosition());
            }
        });

        mViewDataBinding.viewPagerPantCollections.addOnPageChangeListener(new CustomPageScrollListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mHomeScreenPresenter.setBottomWearPagerPosition(position);
                mHomeScreenViewModel.checkIfCombinationFavorite(mHomeScreenPresenter.getTopWearPagerPosition(),position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgAddShirt:
                mHomeScreenPresenter.setSelectedOption(AppConstant.TOP_WEAR);
                requestPermission();
                break;
            case R.id.imgAddPant:
                mHomeScreenPresenter.setSelectedOption(AppConstant.BOTTOM_WEAR);
                requestPermission();
                break;
            case R.id.imgFavorite:
                mHomeScreenViewModel.saveFavoriteToDB(mHomeScreenPresenter.getTopWearPagerPosition(),mHomeScreenPresenter.getBottomWearPagerPosition());
                break;
            case R.id.imgShuffle:
                shuffleCombinations();
                break;
        }
    }

    private void shuffleCombinations(){
        List<TopWearModel> topWearModelArrayList = mHomeScreenViewModel.getTopWearModelList().getValue();
        List<BottomWearModel> bottomWearModelArrayList = mHomeScreenViewModel.getBottomWearModelList().getValue();
        if(topWearModelArrayList != null && bottomWearModelArrayList != null){
            int topWearArraySize = topWearModelArrayList.size();
            int bottomWearArraySize = bottomWearModelArrayList.size();
            if(bottomWearArraySize > 0 && topWearArraySize > 0){
                Random random = new Random();
                mHomeScreenPresenter.setTopWearPagerPosition(random.nextInt(topWearArraySize));
                mHomeScreenPresenter.setBottomWearPagerPosition(random.nextInt(bottomWearArraySize));
                mViewDataBinding.viewPagerShirtsCollections.setCurrentItem(mHomeScreenPresenter.getTopWearPagerPosition());
                mViewDataBinding.viewPagerPantCollections.setCurrentItem((mHomeScreenPresenter.getBottomWearPagerPosition()));
            }
        }
    }

    // Open option dialog
    private void openMenuDialog() {
        final CharSequence options[] = new CharSequence[]{getString(R.string.take_picture), getString(R.string.open_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.pick_an_image);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        dispatchTakePictureIntent();
                        break;
                    case 1:
                        pickImageFromGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    // Method to take picture from camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if(getActivity() != null){
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = mHomeScreenPresenter.createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                            FILEPROVIDER_AUTHORITY,
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    // method to pick image from gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), REQUEST_PICK_FROM_PHOTO);
    }

    // Result of picking image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            addImagePathToList();
        }
        else if(requestCode == REQUEST_PICK_FROM_PHOTO && resultCode == RESULT_OK) {
            if(data != null && data.getData() != null){
                mHomeScreenPresenter.setCurrentImagePath(data.getData().toString());
                addImagePathToList();
            }
        }
    }

    // Add image to db and render it
    public void addImagePathToList(){
       switch (mHomeScreenPresenter.getSelectedOption()){
            case AppConstant.TOP_WEAR:
                saveTopWearToDB();
                mHomeScreenViewModel.getTopWearModelList().getValue().add(new TopWearModel(mHomeScreenPresenter.getCurrentImagePath()));
                mHomeScreenPresenter.setTopWearPagerPosition(mHomeScreenViewModel.getTopWearModelList().getValue().size() - 1);
                mHomeScreenViewModel.getTopWearModelList().setValue(mHomeScreenViewModel.getTopWearModelList().getValue());
                break;
            case AppConstant.BOTTOM_WEAR:
                saveBottomWearToDB();
                mHomeScreenViewModel.getBottomWearModelList().getValue().add(new BottomWearModel(mHomeScreenPresenter.getCurrentImagePath()));
                mHomeScreenPresenter.setBottomWearPagerPosition(mHomeScreenViewModel.getBottomWearModelList().getValue().size() - 1);
                mHomeScreenViewModel.getBottomWearModelList().setValue(mHomeScreenViewModel.getBottomWearModelList().getValue());
                break;
        }
    }

    private void saveTopWearToDB() {
        TopWearModel topWearModel = new TopWearModel(mHomeScreenPresenter.getCurrentImagePath());
        mHomeScreenViewModel.insertTopWearToDB(topWearModel);
    }

    private void saveBottomWearToDB() {
        BottomWearModel bottomWearModel = new BottomWearModel(mHomeScreenPresenter.getCurrentImagePath());
        mHomeScreenViewModel.insertBottomWearToDB(bottomWearModel);
    }


    /*----------  Permission -------------*/
    private boolean checkIfGranted(){
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        if(checkIfGranted()){
            if(getActivity() != null)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_PERMISSION);
        }
        else {
            openMenuDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openMenuDialog();
                }
            }
        }
    }
}
