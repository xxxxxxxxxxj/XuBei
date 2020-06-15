package com.haohao.xubei.ui.module.common.photopreview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.StringUtils;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.ui.views.zoom.ImageViewTouch;
import com.haohao.xubei.ui.views.zoom.ImageViewTouchBase;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * 图片预览适配器
 */
class PhotoPreviewAdapter extends PagerAdapter {


    interface OnImageClickListener {
        void onImageClick(int position);
    }

    private Activity mContext;

    private List<PhotoPreviewBean> mListData;


    private OnImageClickListener onImageClickListener;

    @Inject
    PhotoPreviewAdapter(Activity context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageViewTouch imageView = new ImageViewTouch(mContext, null);
        //imageView.setTag(ImageViewTouchViewPager.VIEW_PAGER_OBJECT_TAG + position);
        imageView.setMaxScale(3.0f);
        imageView.setMinScale(1.0f);
        imageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        if (onImageClickListener != null) {
            imageView.setSingleTapListener(() -> onImageClickListener.onImageClick(position));
        }
        PhotoPreviewBean previewBean = mListData.get(position);
        if (previewBean.objURL == null) {
            previewBean.objURL = "";
        }
        if (StringUtils.equals(previewBean.fromType, PhotoPreviewActivity.IMAGE_TYPE_LOCAL) && !previewBean.objURL.contains("http://")) {
            GlideApp.with(mContext).load(new File(previewBean.objURL)).override(1080).into(imageView);
        } else {
            GlideApp.with(mContext).load(previewBean.objURL).override(1080).into(imageView);
        }
        container.addView(imageView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        return imageView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    /**
     * 设置数据
     */
    void setListData(List<PhotoPreviewBean> listData) {
        if (mListData == null) {
            mListData = listData;
        }
        notifyDataSetChanged();

    }


    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        try {
            super.finishUpdate(container);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Catch the NullPointerException in FragmentPagerAdapter.finishUpdate");
        }
    }
}
