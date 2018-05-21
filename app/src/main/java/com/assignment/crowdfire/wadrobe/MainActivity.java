package com.assignment.crowdfire.wadrobe;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.assignment.crowdfire.wadrobe.dashboard.HomeScreenFragment;
import com.assignment.crowdfire.wadrobe.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView( this, R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add( R.id.container, new HomeScreenFragment()).commit()   ;
    }
}
