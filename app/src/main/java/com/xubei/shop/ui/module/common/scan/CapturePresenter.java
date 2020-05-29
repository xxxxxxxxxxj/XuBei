package com.xubei.shop.ui.module.common.scan;

import android.Manifest;

import com.blankj.utilcode.util.ToastUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

/**
 * 扫描
 * date：2017/5/16 09:31
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class CapturePresenter extends CaptureContract.Presenter {


    private RxPermissions mRxPermissions;

    @Inject
    CapturePresenter(RxPermissions rxPermissions) {
        mRxPermissions = rxPermissions;
    }

    @Override
    public void start() {
        mRxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .as(mView.bindLifecycle())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        mView.startScanQECode();
                    } else {
                        ToastUtils.showShort("缺少相机，SD卡读写权限，请打开对应的权限");
                        mView.closedWin();
                    }
                });
    }

}
