package com.xubei.shop.ui.module.account.contract;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xubei.shop.ui.module.account.model.GameBean;
import com.xubei.shop.ui.module.account.model.OutGoodsBean;
import com.xubei.shop.ui.module.base.IABaseContract;
import com.xubei.shop.ui.views.NoDataView;

import java.util.List;

/**
 * 账号置顶
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccTopContract extends IABaseContract {

    interface View extends IBaseView {

        void initView(List<OutGoodsBean> list, NoDataView noDataView);

        void notifyItemRangeChanged(int startP, int size, Object payloads);

        void setAllSelectType(Boolean allSelectType);

        void setAllSelectPrice(double allPrice);

        void onAutoRefresh();

        void setGameList(List<String> letterList,List<GameBean> gameList);

        void updateSelectView(int selectLetterPosition);

        void showSelectGamePop();

        void setGameSelect(GameBean selectGameBean);

        SmartRefreshLayout getSrl();
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}
