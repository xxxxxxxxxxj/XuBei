package com.xubei.shop.ui.module.rights.presenter;

import android.content.Intent;
import android.view.LayoutInflater;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.FileUploadHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiGoodsService;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.databinding.DialogModifyPwLayoutBinding;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.module.common.photolist.LocalImageListActivity;
import com.xubei.shop.ui.module.common.photopreview.PhotoPreviewActivity;
import com.xubei.shop.ui.module.rights.RightsProcessSellerActivity;
import com.xubei.shop.ui.module.rights.contract.RightsProcessSellerContract;
import com.xubei.shop.ui.module.rights.model.LeaseeArbBean;
import com.xubei.shop.utlis.TakePhoto;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

/**
 * 申请维权
 * date：2017/12/13 09:58
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class RightsProcessSellerPresenter extends RightsProcessSellerContract.Presenter {

    private String orderNo;

    private ApiUserNewService apiUserNewService;

    private ApiGoodsService apiGoodsService;

    private UserBeanHelp userBeanHelp;

    private TakePhoto takePhoto;

    @Inject
    RightsProcessSellerPresenter(String orderNo, ApiUserNewService apiUserNewService, ApiGoodsService apiGoodsService, TakePhoto takePhoto, UserBeanHelp userBeanHelp) {
        this.orderNo = orderNo;
        this.apiUserNewService = apiUserNewService;
        this.userBeanHelp = userBeanHelp;
        this.takePhoto = takePhoto;
        this.apiGoodsService = apiGoodsService;
    }

    private LeaseeArbBean mLeaseeArbBean;

    //本地图片选择
    private ArrayList<String> imageList = new ArrayList<>();

    //上传的网络图片列表
    private ArrayList<String> netImages = new ArrayList<>();

    @Override
    public void start() {
        //请求维权信息
        toLeaseeArbList();
    }


    //显示修改密码框
    public void changePassword() {
        DialogModifyPwLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mView.getContext()), R.layout.dialog_modify_pw_layout, null, false);
        AlertDialog dialog = new AlertDialog.Builder(mView.getContext(), R.style.custom_dialog_style)
                .setView(binding.getRoot()).show();
        binding.btnOk.setOnClickListener(v -> {
            String inputText = binding.etInputPw.getText().toString().trim();
            if (ObjectUtils.isEmpty(inputText)) {
                ToastUtils.showShort("密码不能为空");
                return;
            }
            if (!inputText.equals(binding.etInputPw2.getText().toString().trim())) {
                ToastUtils.showShort("2次密码输入不一致");
                return;
            }
            dialog.dismiss();
            changeGoodsPwd(inputText);
        });
        binding.ivClose.setOnClickListener(v -> dialog.dismiss());
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
                .navigation((RightsProcessSellerActivity) mView.getContext(), AppConfig.CODE_IMAGE_LIST_REQUEST);
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


    //提交维权图片
    public void doSubmit() {
        //根据是否显示不同意维权按钮判断是否处理过维权，如果处理过则不调用处理维权的接口
        if ("1".equals(mLeaseeArbBean.outOrderLeaseRightList.get(0).isNoAgreeArb)) {
            //如果有图片，调用图片上传接口
            if (imageList.size() > 0) {
                netImages.clear();
                doUploadImage(0);
            } else {
                //没有图片直接调用不同意接口
                addLeaseRightDetail();
            }
        } else {
            //只有添加凭证，则需要至少一张图片
            if (imageList.size() == 0) {
                ToastUtils.showShort("请至少提交一张图片");
                return;
            }
            netImages.clear();
            doUploadImage(0);
        }

    }

    //同意维权
    public void doAggreOk() {
        new AlertDialog.Builder(mView.getContext())
                .setTitle("提示")
                .setMessage("是否确认同意维权？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", (dialog, which) -> handleLeaseRight(1))
                .show();
    }

    //获取维权记录
    private void toLeaseeArbList() {
        apiUserNewService.toLeaseeArbList(userBeanHelp.getAuthorization(), orderNo)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<LeaseeArbBean>(mView) {
                    @Override
                    public void onSuccess(LeaseeArbBean leaseeArbBean) {
                        mLeaseeArbBean = leaseeArbBean;
                        mView.setLeaseeArbBean(mLeaseeArbBean);
                    }

                    @Override
                    public void onError(String errStr) {
                        mView.finish();
                        ToastUtils.showShort(errStr);
                    }
                });


    }


    //修改商品密码
    private void changeGoodsPwd(String password) {
        apiUserNewService.changeGoodsPwd(userBeanHelp.getAuthorization(), mLeaseeArbBean.outOrder.goodId, password)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("密码修改成功");
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
                            addLeaseRightDetail();
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


    //添加凭证
    private void addLeaseRightDetail() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String url : netImages) {
            stringBuilder.append(url).append(",");
        }
        String tempImgPaths = stringBuilder.toString();
        if (ObjectUtils.isNotEmpty(tempImgPaths)) {
            tempImgPaths = tempImgPaths.substring(0, tempImgPaths.length() - 1);
        }
        apiUserNewService.addLeaseRightDetail(userBeanHelp.getAuthorization(), mLeaseeArbBean.outOrderLeaseRightList.get(0).leaseRightNo, 0, tempImgPaths)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("提交成功");
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_SELLER_LIST, true);
                        mView.finish();

                    }


                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


    //处理维权1:同意
    private void handleLeaseRight(int isAggre) {
        apiUserNewService.handleLeaseRight(userBeanHelp.getAuthorization(),
                mLeaseeArbBean.outOrder.gameNo,
                mLeaseeArbBean.outOrderLeaseRightList.get(0).leaseRightNo,
                isAggre)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


}
