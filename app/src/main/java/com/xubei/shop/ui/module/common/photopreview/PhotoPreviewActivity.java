package com.xubei.shop.ui.module.common.photopreview;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.CommonActivityPhotoPreviewBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 图片查看器
 */
@Route(path = AppConstants.PagePath.COMM_PHOTOPREVIEW)
public class PhotoPreviewActivity extends ABaseActivity<PhotoPreviewContract.Presenter> implements PhotoPreviewContract.View {


    private CommonActivityPhotoPreviewBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.common_activity_photo_preview);
    }

    public static final String IMAGE_TYPE = "image_type";

    public static final String IMAGE_TYPE_LOCAL = "image_type_local";

    public static final String IMAGE_TYPE_NETWORK = "image_type_network";

    /**
     * 图片列表数据
     */
    public final static String PHOTO_LIST = "photoList";
    /**
     * 当前选中的图片
     */
    public final static String CURRENT_POSITION = "currentPosition";

    @Inject
    PhotoPreviewPresenter mPresenter;

    @Inject
    PhotoPreviewAdapter mPhotoPreviewAdapter;

    @Inject
    RxPermissions rxPermissions;

    @Override
    protected PhotoPreviewContract.Presenter getMVPPresenter() {
        return mPresenter;
    }


    /**
     * 单图片预览(本地图片)
     */
    public static void startPhotoPreview(String imagePrat) {
        ArrayList<String> imageList = new ArrayList<>();
        imageList.add(imagePrat);
        startPhotoPreview(imageList, 0);
    }

    /**
     * 单图片预览（网络图片/网络图片）
     */
    public static void startPhotoPreview(PhotoPreviewBean bean) {
        ArrayList<PhotoPreviewBean> imageList = new ArrayList<>();
        imageList.add(bean);
        startPhotoPreview(imageList, 0);
    }

    /**
     * 多图片预览
     *
     * @param currentPosition 当前第几个（从0开始)
     */
    public static <T> void startPhotoPreview(ArrayList<T> imageList, int currentPosition) {
        ArrayList<PhotoPreviewBean> beanList = new ArrayList<>();
        for (T t : imageList) {
            if (t instanceof String) {
                PhotoPreviewBean previewBean = new PhotoPreviewBean();
                previewBean.objURL = (String) t;
                previewBean.fromType = IMAGE_TYPE_LOCAL;
                beanList.add(previewBean);
            } else {
                beanList.add((PhotoPreviewBean) t);
            }
        }
        ARouter.getInstance().build(AppConstants.PagePath.COMM_PHOTOPREVIEW)
                .withSerializable(PHOTO_LIST, beanList)
                .withInt(CURRENT_POSITION, currentPosition)
                .navigation();
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        initViewPager();
        initRxBinding();
        mPresenter.start();
    }

    private void initRxBinding() {
        //点击保存按钮先检查权限
        RxView.clicks(binding.llSave)
                .compose(rxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
                .as(bindLifecycle())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //获取权限成功
                        mPresenter.saveImage();
                    } else {
                        //获取权限失败
                        ToastUtils.showShort("缺少SD卡权限，保存图片失败");
                    }
                });

    }


    private void initViewPager() {
        binding.vpPhotoPreview.setOnPageSelectedListener(position -> mPresenter.upDataCurrentPosition(position));
        binding.vpPhotoPreview.setOffscreenPageLimit(5);
        //关闭当前界面
        mPhotoPreviewAdapter.setOnImageClickListener(position -> finish());
        binding.vpPhotoPreview.setAdapter(mPhotoPreviewAdapter);
    }


    @Override
    public void setPhotoList(ArrayList<PhotoPreviewBean> mPhotoList) {
        mPhotoPreviewAdapter.setListData(mPhotoList);
    }

    /**
     * 跳转到指定页
     */
    @Override
    public void showPageSelected(int position, int size) {
        binding.tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", (position + 1), size));
        binding.vpPhotoPreview.setCurrentItem(position);
    }

    @Override
    public void hideSaveBtn() {
        binding.llSave.setVisibility(View.GONE);
    }


}
