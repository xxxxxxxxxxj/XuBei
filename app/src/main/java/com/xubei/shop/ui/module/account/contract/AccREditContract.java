package com.xubei.shop.ui.module.account.contract;

import com.xubei.shop.ui.module.account.model.OutGoodsDetailValuesBean;
import com.xubei.shop.ui.module.account.model.GameReleaseAllBean;
import com.xubei.shop.ui.module.base.IABaseContract;

import java.util.List;

/**
 * 账号发布
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccREditContract extends IABaseContract {

    interface View extends IBaseView {

        void setLayoutModel(GameReleaseAllBean gameReleaseAllBean,OutGoodsDetailValuesBean outGoodsDetailValuesBean);

        void setImageList(List<String> imageList);

        void setSelectActivity(String strRule);

        void setNoDataView(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {


    }

}
