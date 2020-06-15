package com.haohao.xubei.ui.module.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.glide.GlideApp;
import com.haohao.xubei.databinding.ActUserInfoBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.user.contract.UserInfoContract;
import com.haohao.xubei.ui.module.user.presenter.UserInfoPresenter;
import com.haohao.xubei.utlis.AlertDialogUtils;
import com.haohao.xubei.utlis.TakePhoto;
import com.yalantis.ucrop.UCrop;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 个人资料
 * date：2017/12/4 14:56
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.USER_INFO, extras = AppConstants.ARAction.LOGIN)
public class UserInfoActivity extends ABaseActivity<UserInfoContract.Presenter> implements UserInfoContract.View {

    private ActUserInfoBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_user_info);
        binding.setActivity(this);
    }

    @Inject
    UserInfoPresenter presenter;

    @Override
    protected UserInfoContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    AlertDialogUtils dialogUtils;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("个人资料");
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.start();
    }

    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_avatar://更新头像
                dialogUtils.createHeadSelectedDialog(binding.ivAvatar, position -> {
                    switch (position) {
                        case 1://请求画廊
                            presenter.doPickPhotoFromGallery();
                            break;
                        case 2://拍照
                            presenter.doTakePhoto();
                            break;
                    }
                });
                break;
            case R.id.ll_nick_name://修改昵称
                ARouter.getInstance().build(AppConstants.PagePath.USER_UPDATENICKNAME).navigation();
                break;
            case R.id.ll_id_card://身份验证
                //先判断是实名认证了
                presenter.doCheckAuth();
                break;
            case R.id.ll_phone://更改绑定手机号
              //  ARouter.getInstance().build(AppConstants.PagePath.USER_UPDATEBINDPHONE).navigation();
                break;
        }
    }

    @Override
    public void setUserInfo(UserTable userBean) {
        try {
            //设置头像
            GlideApp.with(this).load(userBean.getAvatar() == null ? "" : userBean.getAvatar()).placeholder(R.mipmap.common_avatar_default).error(R.mipmap.common_avatar_default).into(binding.ivAvatar);
            //设置昵称
            binding.tvNickName.setText(ObjectUtils.isNotEmpty(userBean.getUsernick()) ? userBean.getUsernick() : "未设置");
            //设置手机号
            if (ObjectUtils.isNotEmpty(userBean.getMobile()) && userBean.getMobile().length() == 11) {
                String tempMobile = userBean.getMobile().substring(0, 3) + "****" + userBean.getMobile().substring(7, 11);
                binding.tvPhone.setText(tempMobile);
            }
        } catch (NullPointerException e) {
            ToastUtils.showShort("加载数据失败，请重试");
        }

    }

    @Override
    public void setIdCartAuth() {
        //身份验证状态
        ARouter.getInstance().build(AppConstants.PagePath.USER_VERIFIED).navigation();
    }

    //调用裁剪
    @Override
    public void onUCropImage(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        //裁剪质量
        options.setCompressionQuality(100);
        //是否隐藏底部容器，默认显示
        options.setHideBottomControls(true);
        options.setActiveWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1080, 1080)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TakePhoto.CAMERA_WITH_DATA://拍照
                    presenter.onCameraComplete();
                    break;
                case TakePhoto.PHOTO_WITH_DATA://画廊
                    presenter.onPhotoComplete(data.getData());
                    break;
                case UCrop.REQUEST_CROP://剪切返回
                    presenter.onUCropResult(UCrop.getOutput(data));
                    break;
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            ToastUtils.showShort("图片剪切失败");
        }
    }

}
