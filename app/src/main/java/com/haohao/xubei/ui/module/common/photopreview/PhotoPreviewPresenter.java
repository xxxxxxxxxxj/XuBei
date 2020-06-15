package com.haohao.xubei.ui.module.common.photopreview;

import android.graphics.Bitmap;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.utlis.Tools;

import java.io.File;
import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposable;


/**
 * 图片预览逻辑处理
 * date：2017/2/27 14:25
 * author：Seraph
 * mail：417753393@qq.com
 **/
class PhotoPreviewPresenter extends PhotoPreviewContract.Presenter {


    private ArrayList<PhotoPreviewBean> mPhotoList;

    /**
     * 当前第几张照片
     */
    private int currentPosition;

    @Inject
    PhotoPreviewPresenter(ArrayList<PhotoPreviewBean> mPhotoList, @QualifierType("p")Integer currentPosition) {
        this.mPhotoList = mPhotoList;
        this.currentPosition = currentPosition;
    }

    private Disposable mDisposable;

    /**
     * 保存的图片
     */
    private PhotoPreviewBean mSavePhoto;
    /**
     * 保存的图片名称
     */
    private String saveImageName;


    private String imageType;


    @Override
    public void start() {
        //初始化处理
        if (mPhotoList == null || mPhotoList.size() == 0) {
            ToastUtils.showShort("没有可预览的图片");
            mView.finish();
            return;
        }
        mView.setPhotoList(mPhotoList);
        //显示指定位置图片
        upDataCurrentPosition(currentPosition);
    }


    public void upDataCurrentPosition(int position) {
        this.currentPosition = position;
        mView.showPageSelected(position, mPhotoList.size());
    }


    /**
     * 保存当前图片
     */
    public void saveImage() {
        if (!StringUtils.equals(imageType, PhotoPreviewActivity.IMAGE_TYPE_NETWORK)) {
            return;
        }
        mSavePhoto = mPhotoList.get(currentPosition);
        mView.showLoading("保存中").setOnDismissListener(dialog -> {
            if (mDisposable != null) {
                mDisposable.dispose();
            }
        });
        GlideApp.with(mView.getContext()).asBitmap()
                .load(mSavePhoto.objURL)
                .into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> transition) {
                //保存bitmap
                mDisposable = saveImageToDisk(resource);
            }
        });
    }

    /**
     * 保存到磁盘
     */
    private Disposable saveImageToDisk(final Bitmap bitmap) {
        //使用子线程进行保存
        return Flowable.create((FlowableEmitter<String> e) -> {
            String saveImageName = EncryptUtils.encryptMD5ToString(mSavePhoto.objURL) + "." + (StringUtils.isEmpty(mSavePhoto.type) ? "jpg" : mSavePhoto.type);
            File dcimFile = Tools.getDCIMFile(saveImageName);
            if (dcimFile != null && dcimFile.exists() && dcimFile.length() > 0) {
                e.onNext("图片已保存");
                e.onComplete();
            }
            try {
                Tools.bitmapToFile(bitmap, dcimFile);
                // 最后通知图库更新此图片
                Tools.scanAppImageFile(mView.getContext(), saveImageName);
                e.onNext("保存成功");
                e.onComplete();
            } catch (Exception e1) {
                e.onError(e1);
            }
        }, BackpressureStrategy.BUFFER)
                .compose(RxSchedulers.io_main())
                .as(mView.bindLifecycle())
                .subscribe(s -> {
                    ToastUtils.showShort(s);
                    mView.hideLoading();
                }, throwable -> {
                    throwable.printStackTrace();
                    ToastUtils.showShort("保存失败");
                    mView.hideLoading();
                });
    }
}
