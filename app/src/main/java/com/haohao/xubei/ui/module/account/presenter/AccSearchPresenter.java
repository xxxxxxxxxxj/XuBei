package com.haohao.xubei.ui.module.account.presenter;

import android.app.AlertDialog;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.SearchHistoryHelp;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.db.table.SearchHistoryTable;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiPHPService;
import com.haohao.xubei.ui.module.account.contract.AccSearchContract;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 搜索
 * date：2017/11/28 11:23
 * author：Seraph
 *
 **/
public class AccSearchPresenter extends AccSearchContract.Presenter {

    private ApiPHPService apiPHPService;

    private SearchHistoryHelp historyHelp;

    private UserTable mUserTable;

    @Inject
    AccSearchPresenter(ApiPHPService apiPHPService, SearchHistoryHelp historyHelp, UserBeanHelp userBeanHelp) {
        this.apiPHPService = apiPHPService;
        this.historyHelp = historyHelp;
        this.mUserTable = userBeanHelp.getUserBean();
    }

    //历史搜索
    private List<SearchHistoryTable> listSearchHistory = new ArrayList<>();

    //热门搜索
    private List<GameBean> hotSearchList = new ArrayList<>();

    @Override
    public void start() {
        //查询搜索的历史记录
        selectSearchHistory();
        //如果设计了推荐的搜索热门，则进行接口请求
        getTopSearch();
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
        List<SearchHistoryTable> listSearchHistory = historyHelp.querySearchDB(mUserTable == null ? "-1" : mUserTable.getUserid(), AppConstants.SPAction.SEARCH_RENT_NO);
        this.listSearchHistory.clear();
        this.listSearchHistory.addAll(listSearchHistory);
        mView.setSearchHistoryList(this.listSearchHistory);
    }


    /**
     * 保存到数据库
     */
    private void saveSearchToDB(String searchKeyWord) {
        //区分用户
        historyHelp.saveSearchToDB(mUserTable == null ? "-1" : mUserTable.getUserid(), AppConstants.SPAction.SEARCH_RENT_NO, searchKeyWord);
    }

    /**
     * 删除本地搜索数据列表
     */
    public void cleanSearchHistory() {
        new AlertDialog.Builder(mView.getContext()).setMessage("是否确认清除历史记录？").setNegativeButton("取消", null)
                .setPositiveButton("确认", (dialog, which) -> {
                    historyHelp.deleteAllSearchDB(mUserTable == null ? "-1" : mUserTable.getUserid(), AppConstants.SPAction.SEARCH_RENT_NO);
                    selectSearchHistory();
                }).show();
    }

    //点击历史位置
    public void doSearchHistoryPosition(int position) {
        SearchHistoryTable historyTable = listSearchHistory.get(position);
        doSearch(historyTable.getSearchKey());
    }

    //点击热门
    public void doSearchPosition(int position) {
        GameBean gameBean = hotSearchList.get(position);
        doSearch(gameBean.getGameName());
    }

    //获取热门搜索
    private void getTopSearch() {
        apiPHPService.appconfigTopSearch(AppConfig.APP_KEY_PHP)
                .compose(RxSchedulers.io_main_business())
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<GameBean>>() {

                    @Override
                    public void onSuccess(List<GameBean> gameBeans) {
                        hotSearchList.addAll(gameBeans);
                        //开始组装数据
                        mView.setSearchList(hotSearchList);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


}
