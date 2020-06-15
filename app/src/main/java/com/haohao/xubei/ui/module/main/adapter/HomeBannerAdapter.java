package com.haohao.xubei.ui.module.main.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ObjectUtils;
import com.haohao.xubei.R;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.module.base.BaseDataCms;
import com.haohao.xubei.ui.module.main.model.BannerBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 推荐banner图适配器
 * date：2017/5/4 15:47
 * author：Seraph
 *
 **/
public class HomeBannerAdapter extends PagerAdapter {

    private List<BaseDataCms<BannerBean>> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    private LayoutInflater inflater;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Inject
    HomeBannerAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    public void repData(List<BaseDataCms<BannerBean>> listData) {
        if (list != listData) {
            list.clear();
            list.addAll(listData);
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.act_main_home_head_banner_item, container, false);
        ImageView itemBanner = view.findViewById(R.id.iv_banner_item);
        String tempLocation = list.get(position).properties.location;
        if (ObjectUtils.isEmpty(tempLocation) || !tempLocation.contains("http")) {
            tempLocation = "http:" + tempLocation;
        }
        GlideApp.with(container.getContext()).load(tempLocation).into(itemBanner);
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
