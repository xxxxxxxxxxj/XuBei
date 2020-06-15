package com.haohao.xubei.ui.module.user.presenter;

import android.net.Uri;

import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.FileUploadHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiGoodsService;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.module.user.contract.UserInfoContract;
import com.haohao.xubei.ui.module.user.model.AcctManageBean;
import com.haohao.xubei.utlis.TakePhoto;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * 个人中心
 * date：2017/12/4 15:04
 * author：Seraph
 **/
public class UserInfoPresenter extends UserInfoContract.Presenter {


    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    private ApiGoodsService apiGoodsService;

    private TakePhoto mTakePhoto;

    @Inject
    UserInfoPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService, ApiGoodsService apiGoodsService, TakePhoto takePhoto) {
        this.userBeanHelp = userBeanHelp;
        this.apiGoodsService = apiGoodsService;
        this.mTakePhoto = takePhoto;
        this.apiUserNewService = apiUserNewService;
    }

    @Override
    public void start() {
        updateUserBean();
    }

    //拍照
    public void doTakePhoto() {
        mTakePhoto.doTakePhoto();
    }

    //选择相册
    public void doPickPhotoFromGallery() {
        mTakePhoto.doPickPhotoFromGallery();
    }

    //拍照返回
    public void onCameraComplete() {
        Uri uri = Uri.fromFile(mTakePhoto.getCurrentPhotoFile());
        mView.onUCropImage(uri, uri);
    }

    //照片返回
    public void onPhotoComplete(Uri data) {
        mView.onUCropImage(data, Uri.fromFile(mTakePhoto.getNewPhotoFile()));
    }

    //图片剪切返回
    public void onUCropResult(Uri output) {
        //上传图片
        try {
            upTempImage(new File(new URI(output.toString())));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    //上传图片到服务器
    private void upTempImage(File file) {
        Map<String, File> fileParams = new HashMap<>();
        fileParams.put("file", file);
        apiGoodsService.upTemp(FileUploadHelp.multipartRequestBody(null, fileParams))
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("上传图片").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<String>>() {
                    @Override
                    public void onSuccess(BaseData<String> stringBaseData) {
                        //得到上传后的网络地址，再更新到头像
                        updataAvatar(stringBaseData.image_path);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //更新用户资料
    private void updateUserBean() {
        mView.setUserInfo(userBeanHelp.getUserBean());
    }


    //检查是否实名认证
    public void doCheckAuth() {
        apiUserNewService.acctManageIndex(userBeanHelp.getAuthorization())
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<AcctManageBean>(mView) {
                    @Override
                    public void onSuccess(AcctManageBean acctManageBean) {
                        if (acctManageBean.isCertificate) {
                            //已经验证
                            ToastUtils.showShort("您已验证");
                        } else {
                            //跳转简单认证界面
                            mView.setIdCartAuth();
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


    //更新头像
    private void updataAvatar(final String image_path) {
        apiUserNewService.updateHeadPortrait(userBeanHelp.getAuthorization(), image_path)
                .compose(RxSchedulers.io_main_business())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        //更新头像成功
                        ToastUtils.showShort("保存头像成功");
                        UserTable userTable = userBeanHelp.getUserBean();
                        userTable.setAvatar(image_path);
                        userBeanHelp.save(userTable);
                        //加载头像
                        updateUserBean();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


}
