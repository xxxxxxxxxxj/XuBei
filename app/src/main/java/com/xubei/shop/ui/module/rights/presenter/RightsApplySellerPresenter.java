package com.xubei.shop.ui.module.rights.presenter;

import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.FileUploadHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiGoodsService;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.common.photolist.LocalImageListActivity;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewActivity;
import com.xubei.shop.ui.module.rights.RightsApplySellerActivity;
import com.xubei.shop.ui.module.rights.contract.RightsApplySellerContract;
import com.xubei.shop.ui.module.rights.model.RightReasonBean;
import com.xubei.shop.utlis.TakePhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;

/**
 * 申请维权
 * date：2017/12/13 09:58
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class RightsApplySellerPresenter extends RightsApplySellerContract.Presenter {

    private TakePhoto takePhoto;
    private ApiGoodsService apiGoodsService;
    private UserBeanHelp userBeanHelp;
    private ApiUserNewService apiUserNewService;
    private String orderNo;

    @Inject
    RightsApplySellerPresenter(ApiGoodsService apiGoodsService, UserBeanHelp userBeanHelp, TakePhoto takePhoto, ApiUserNewService apiUserNewService, String orderNo) {
        this.takePhoto = takePhoto;
        this.apiGoodsService = apiGoodsService;
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.orderNo = orderNo;
    }


    //本地图片选择
    private ArrayList<String> imageList = new ArrayList<>();

    //维权类型列表
    private List<RightReasonBean> reasonBeanList = new ArrayList<>();

    //维权原因(默认选择第一个原因)
    private int type = 0;

    //上传的网络图片列表
    private ArrayList<String> netImages = new ArrayList<>();

    //维权说明
    private String inputStr = "";

    @Override
    public void start() {
        //获取维权原因列表
        orderLeaseRightReason();
    }


    //显示选项
    public void doShowType() {
        String[] tempItems = new String[reasonBeanList.size()];
        for (int i = 0; i < tempItems.length; i++) {
            tempItems[i] = reasonBeanList.get(i).reason;
        }
        new AlertDialog.Builder(mView.getContext())
                .setItems(tempItems, (dialog, which) -> {
                    type = which;
                    mView.setSelectType(reasonBeanList.get(type).reason);
                }).show();
    }


    //移除选择
    public void doImageItemDelete(String path) {
        imageList.remove(path);
    }

    //查看选择的图片
    public void doImageItemClick(int position) {
        PhotoPreviewActivity.startPhotoPreview(imageList, position);
    }

    //拍照
    public void doSelectCamera() {
        takePhoto.doTakePhoto();
    }

    //相册选择
    public void doSelectPhoto() {
        ARouter.getInstance().build(AppConstants.PagePath.COMM_LOCALIMAGELIST)
                .withStringArrayList(LocalImageListActivity.SELECTED_PATH, imageList)
                .navigation((RightsApplySellerActivity) mView.getContext(), AppConfig.CODE_IMAGE_LIST_REQUEST);
    }

    //拍照完毕，获取拍照文件
    public void onCameraComplete() {
        if (takePhoto.getCurrentPhotoFile() == null) {
            ToastUtils.showShort("拍照异常");
            return;
        }
        //  Uri photoUri = Uri.fromFile(takePhoto.getCurrentPhotoFile());
        imageList.add(takePhoto.getCurrentPhotoFile().getPath());
        mView.setImageList(imageList);
    }

    //选择的照片返回
    public void onLocalImageResult(Intent data) {
        imageList.clear();
        imageList.addAll(data.getStringArrayListExtra(LocalImageListActivity.SELECT_PATH));
        mView.setImageList(imageList);
    }

    //提交
    public void doSubmit(String inputStr) {
        this.inputStr = inputStr;
        //如果有图片，调用图片上传接口
        if (imageList.size() > 0) {
            netImages.clear();
            doUploadImage(0);
        } else {
            doLeaseeSubArbitration();
        }
    }

    //提交维权申请
    private void doLeaseeSubArbitration() {
        if (reasonBeanList.size() == 0) {
            ToastUtils.showShort("网络异常");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String url : netImages) {
            stringBuilder.append(url).append(",");
        }
        String tempImgPaths = stringBuilder.toString();
        if (ObjectUtils.isNotEmpty(tempImgPaths)) {
            tempImgPaths = tempImgPaths.substring(0, tempImgPaths.length() - 1);
        }
        HashMap<String, Object> map = new HashMap<>();
        //订单号
        map.put("orderId", orderNo);
        //维权类型 0:普通维权，1：极速维权(只支持买家)
        map.put("arbType", 0);
        //维权原因
        map.put("rightReason", reasonBeanList.get(type).reasonType);
        //维权说明
        if (ObjectUtils.isNotEmpty(inputStr)) {
            map.put("rightsProtection", inputStr);
        }
        //图片
        if (ObjectUtils.isNotEmpty(tempImgPaths)) {
            map.put("urls", tempImgPaths);
        }
        apiUserNewService.leaseeSubArb(userBeanHelp.getAuthorization(), map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("提交申请").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String string) {
                        ToastUtils.showShort("提交申请成功");
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_SELLER_LIST, true);
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_LIST, true);
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //上传图片接口 i为当前开始上传图片的位置
    private void doUploadImage(final int i) {
        String tempImage = imageList.get(i);
        Map<String, File> fileParams = new HashMap<>();
        fileParams.put("file", new File(tempImage));
        apiGoodsService.upTemp(FileUploadHelp.multipartRequestBody(null, fileParams))
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("上传图片").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<String>>() {
                    @Override
                    public void onSuccess(BaseData<String> stringBaseData) {
                        //添加网络地址到集合
                        netImages.add(stringBaseData.image_path);
                        if (i >= imageList.size() - 1) {
                            //如果上传的位置到最后一个，则调用提交接口
                            doLeaseeSubArbitration();
                        } else {
                            doUploadImage(i + 1);//如果没上传完，则递归调用
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.hideLoading();
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //获取维权原因
    private void orderLeaseRightReason() {
        apiUserNewService.orderLeaseRightReason(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .subscribe(new ABaseSubscriber<List<RightReasonBean>>(mView) {
                    @Override
                    public void onSuccess(List<RightReasonBean> rightReasonBeans) {
                        reasonBeanList.clear();
                        reasonBeanList.addAll(rightReasonBeans);
                        //设置第一个原因
                        mView.setSelectType(reasonBeanList.get(type).reason);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}
