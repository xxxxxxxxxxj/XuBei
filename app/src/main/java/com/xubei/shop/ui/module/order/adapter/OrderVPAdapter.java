package com.xubei.shop.ui.module.order.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.xubei.shop.ui.module.order.OrderAllFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * 订单 ViewPage Adapter
 * date：2017/11/14 14:25
 * author：Seraph
 *
 **/
public class OrderVPAdapter extends FragmentPagerAdapter {

    private String[] orderType;

    private List<OrderAllFragment> fragments;

    @Inject
    public OrderVPAdapter(FragmentManager fm, String[] orderType, List<OrderAllFragment> fragments) {
        super(fm);
        this.orderType = orderType;
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return orderType.length;
    }

    @Override
    public OrderAllFragment getItem(int position) {
        return fragments.get(position);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return orderType[position];
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position,@NonNull Object object) {
    }

}
