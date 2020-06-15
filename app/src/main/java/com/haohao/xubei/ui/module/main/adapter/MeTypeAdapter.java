package com.haohao.xubei.ui.module.main.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.haohao.xubei.di.QualifierType;

import java.util.List;

import javax.inject.Inject;

/**
 * 我的Adapter
 * date：2017/11/14 14:25
 * author：Seraph
 *
 **/
public class MeTypeAdapter extends FragmentPagerAdapter {


    private List<Fragment> fragments;

    @Inject
    public MeTypeAdapter(FragmentManager fm, @QualifierType("me") List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "我是租客";
        } else {
            return "我是号主";
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
    }

}
