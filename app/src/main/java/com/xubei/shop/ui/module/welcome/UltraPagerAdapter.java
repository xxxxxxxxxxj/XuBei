package com.xubei.shop.ui.module.welcome;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xubei.shop.R;

import javax.inject.Inject;

/**
 * 引导页切换
 * date：2017/5/3 10:53
 * author：Seraph
 *
 **/
class UltraPagerAdapter extends PagerAdapter {

    public interface PagerItemClickListener {

        void onItemClick(int position);

    }

    private PagerItemClickListener pagerItemClickListener;


    private Integer[] listImage;


    @Inject
    UltraPagerAdapter() {
    }

    public void setListImage(Integer[] listImage) {
        this.listImage = listImage;
    }

    public void setOnClickListener(PagerItemClickListener pagerItemClickListener) {
        this.pagerItemClickListener = pagerItemClickListener;
    }

    @Override
    public int getCount() {
        return listImage == null ? 0 : listImage.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.act_welcome_guide_pages_item, container, false);
        ImageView imageView = view.findViewById(R.id.iv_guide_page);
        imageView.setImageResource(listImage[position]);
        imageView.setOnClickListener(v -> {
            if (pagerItemClickListener != null) {
                pagerItemClickListener.onItemClick(position);
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
