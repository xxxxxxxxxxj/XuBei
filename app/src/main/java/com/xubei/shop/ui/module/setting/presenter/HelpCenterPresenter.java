package com.xubei.shop.ui.module.setting.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;

import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.ui.module.setting.contract.HelpCenterContract;

import javax.inject.Inject;

/**
 * 帮助中心
 * date：2017/12/4 14:38
 * author：Seraph
 *
 **/
public class HelpCenterPresenter extends HelpCenterContract.Presenter {

    private ClipboardManager clipboardManager;

    @Inject
    HelpCenterPresenter(ClipboardManager clipboardManager) {
        this.clipboardManager = clipboardManager;
    }

    @Override
    public void start() {

    }


    //复制订单到剪切板
    public void doCopyAddress() {
        if (clipboardManager != null) {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, AppConfig.PC_DOWNLOAD));
            ToastUtils.showShort("下载链接复制成功");
        }
    }

}
