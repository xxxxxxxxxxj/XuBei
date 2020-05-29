package com.xubei.shop.ui.module.account.adapter;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xubei.shop.R;
import com.xubei.shop.data.network.glide.GlideApp;
import com.xubei.shop.ui.module.account.model.OutGoodsDetailBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 账号详情banner图适配器
 * date：2017/5/4 15:47
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class AccRDetailBannerAdapter extends PagerAdapter {

    private List<OutGoodsDetailBean.GoodsPicLocationBean> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Inject
    AccRDetailBannerAdapter() {
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    public void repData(List<OutGoodsDetailBean.GoodsPicLocationBean> listData) {
        if (list != listData && listData != null) {
            list.clear();
            list.addAll(listData);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.act_acc_detail_banner_item, container, false);
        ImageView itemBanner = view.findViewById(R.id.iv_banner_item);
        GlideApp.with(container.getContext()).load(list.get(position).location).into(itemBanner);
        itemBanner.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(position);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
