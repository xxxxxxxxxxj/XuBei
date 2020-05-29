package com.xubei.shop.ui.module.common.scan;


import com.xubei.shop.ui.module.base.IABaseContract;

/**
 * 扫描
 * date：2017/5/16 09:31
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface CaptureContract extends IABaseContract{

    interface View extends IBaseView {

        void startScanQECode();

        void closedWin();

    }

    abstract class Presenter extends ABasePresenter<View> {

    }
}
