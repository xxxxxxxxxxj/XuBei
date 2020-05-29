package com.xubei.shop.ui.module.account.contract;

import com.xubei.shop.data.db.table.SearchHistoryTable;
import com.xubei.shop.ui.module.account.model.GameBean;
import com.xubei.shop.ui.module.base.IABaseContract;

import java.util.List;

/**
 * 搜索
 * date：2017/11/28 11:15
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface AccSearchContract extends IABaseContract {

    interface View extends IBaseView {

        void startSearchResultActivity(String searchKey);

        void setSearchHistoryList(List<SearchHistoryTable> listSearchHistory);

        void setSearchList(List<GameBean> gameBeans);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }

}