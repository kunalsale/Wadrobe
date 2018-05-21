package com.assignment.crowdfire.wadrobe.dashboard.adapters;

import android.databinding.DataBindingUtil;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.assignment.crowdfire.wadrobe.R;
import com.assignment.crowdfire.wadrobe.data.entities.ClothesModel;
import com.assignment.crowdfire.wadrobe.databinding.HomeScreenPagerItemLayoutBinding;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by hp on 20-05-2018.
 */

public class HomeScreenPagerAdapter<T extends ClothesModel> extends PagerAdapter {

    private List<T> clothesModelArrayList;

    public HomeScreenPagerAdapter(List<T> clothesModelArrayList) {
        this.clothesModelArrayList = clothesModelArrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HomeScreenPagerItemLayoutBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.home_screen_pager_item_layout, container, false);
        Glide.with(container.getContext()).load(clothesModelArrayList.get(position).getImgPath()).into(viewDataBinding.imgPagerItem);
        container.addView(viewDataBinding.getRoot());
        return viewDataBinding.getRoot();
    }

    @Override
    public int getCount() {
        return clothesModelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }

    public void updateList(List<T> clothesModelArrayList){
        this.clothesModelArrayList.clear();
        this.clothesModelArrayList.addAll(clothesModelArrayList);
        notifyDataSetChanged();
    }
}
