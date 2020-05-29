package com.xubei.shop.ui.module.main.contract;

import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.base.BaseDataCms;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.module.main.model.BannerBean;
import com.xubei.shop.ui.module.main.model.HomeMultipleItem;
import com.xubei.shop.ui.module.main.model.WelfareBean;

import java.util.List;

/**
 * 首页
 * date：2017/2/20 17:06
 * author：Seraph
 **/
public interface MainHomeContract extends IABaseContract {

    interface View extends IBaseView {

        void setGameList(List<BaseDataCms<BannerBean>> bannerList,
                         List<HomeMultipleItem> list,
                         List<BaseDataCms<AccBean>> hotList,
                         List<BaseDataCms<WelfareBean>> welfareList);

        void setNoData(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }
}
