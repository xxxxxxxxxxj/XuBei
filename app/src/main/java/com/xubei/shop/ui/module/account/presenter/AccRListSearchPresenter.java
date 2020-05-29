package com.xubei.shop.ui.module.account.presenter;

import android.app.AlertDialog;

import com.blankj.utilcode.util.ObjectUtils;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.db.help.SearchHistoryHelp;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.db.table.SearchHistoryTable;
import com.xubei.shop.data.db.table.UserTable;
import com.xubei.shop.ui.module.account.contract.AccRListSearchContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 搜索
 * date：2017/11/28 11:23
 * author：Seraph
 *
 **/
public class AccRListSearchPresenter extends AccRListSearchContract.Presenter {

    private SearchHistoryHelp historyHelp;

    private UserTable mUserTable;

    @Inject
    AccRListSearchPresenter(SearchHistoryHelp historyHelp, UserBeanHelp userBeanHelp) {
        this.historyHelp = historyHelp;
        this.mUserTable = userBeanHelp.getUserBean();
    }

    //历史搜索
    private List<SearchHistoryTable> listSearchHistory = new ArrayList<>();

    @Override
    public void start() {
        //查询搜索的历史记录
        selectSearchHistory();
    }

    //搜索
    public void doSearch(String searchStr) {
        if (ObjectUtils.isEmpty(searchStr)) {
            return;
        }
        //保存搜索到历史记录
        saveSearchToDB(searchStr);
        //刷新本地搜索结果
        selectSearchHistory();
        //跳转到搜索结果界面
        mView.startSearchResultActivity(searchStr);
    }


    /**
     * 查询本地搜索列表
     */
    private void selectSearchHistory() {
        List<SearchHistoryTable> listSearchHistory = historyHelp.querySearchDB(mUserTable == null ? "-1" : mUserTable.getUserid(), AppConstants.SPAction.SEARCH_MY_ACC);
        this.listSearchHistory.clear();
        this.listSearchHistory.addAll(listSearchHistory);
        mView.setSearchHistoryList(this.listSearchHistory);
    }


    /**
     * 保存到数据库
     */
    private void saveSearchToDB(String searchKeyWord) {
        //区分用户
        historyHelp.saveSearchToDB(mUserTable == null ? "-1" : mUserTable.getUserid(), AppConstants.SPAction.SEARCH_MY_ACC, searchKeyWord);
    }

    /**
     * 删除本地搜索数据列表
     */
    public void cleanSearchHistory() {
        new AlertDialog.Builder(mView.getContext()).setMessage("是否确认清除历史记录？").setNegativeButton("取消", null)
                .setPositiveButton("确认", (dialog, which) -> {
                    historyHelp.deleteAllSearchDB(mUserTable == null ? "-1" : mUserTable.getUserid(), AppConstants.SPAction.SEARCH_MY_ACC);
                    selectSearchHistory();
                }).show();
    }

    //点击历史位置
    public void doSearchHistoryPosition(int position) {
        SearchHistoryTable historyTable = listSearchHistory.get(position);
        doSearch(historyTable.getSearchKey());
    }


}
