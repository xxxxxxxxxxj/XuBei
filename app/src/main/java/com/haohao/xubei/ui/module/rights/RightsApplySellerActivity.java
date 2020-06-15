package com.haohao.xubei.ui.module.rights;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActRightsApplySellerBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.rights.contract.RightsApplySellerContract;
import com.haohao.xubei.ui.module.rights.presenter.RightsApplySellerPresenter;
import com.haohao.xubei.utlis.AlertDialogUtils;
import com.haohao.xubei.utlis.TakePhoto;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 卖家申请维权
 * date：2017/12/13 09:54
 **/
@Route(path = AppConstants.PagePath.RIGHTS_APPLY)
public class RightsApplySellerActivity extends ABaseActivity<RightsApplySellerContract.Presenter> implements RightsApplySellerContract.View {

    private  ActRightsApplySellerBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_rights_apply_seller);
        binding.setActivity(this);
    }

    @Inject
    RightsApplySellerPresenter presenter;

    @Override
    protected RightsApplySellerContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AlertDialogUtils dialogUtils;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("维权申请");
        initLayout();
        presenter.start();
    }

    private void initLayout() {
        //查看图片监听
        binding.civgImages.setOnClickPicListener((v, position) -> {
            switch ((int) v.getTag()) {
                case 0://添加照片
                    dialogUtils.createHeadSelectedDialog(binding.civgImages, position2 -> {
                        switch (position2) {
                            case 1://相册
                                presenter.doSelectPhoto();
                                break;
                            case 2://拍照
                                presenter.doSelectCamera();
                                break;
                        }
                    });
                    break;
                default://点击照片
                    presenter.doImageItemClick(position);
                    break;
            }
        });
        //删除监听
        binding.civgImages.setOnContentChangeListener(path -> presenter.doImageItemDelete(path));
    }


    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_type:
                presenter.doShowType();
                break;
            case R.id.tv_commite:
                presenter.doSubmit(binding.etInputText.getText().toString().trim());
                break;
        }
    }

    //设置图片
    @Override
    public void setImageList(List<String> imageList) {
        binding.civgImages.setItemPaths(imageList);
        binding.civgImages.post(() -> binding.nsv.fullScroll(ScrollView.FOCUS_DOWN));
    }


    //显示选择的原因
    @Override
    public void setSelectType(String type) {
        binding.tvType.setText(type);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TakePhoto.CAMERA_WITH_DATA://相机拍照返回
                    presenter.onCameraComplete();
                    break;
                case AppConfig.CODE_IMAGE_LIST_REQUEST://相册选择
                    presenter.onLocalImageResult(data);
                    break;
            }

        }
    }

}
