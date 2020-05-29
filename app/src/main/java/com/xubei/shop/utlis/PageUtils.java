package com.xubei.shop.utlis;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xubei.shop.AppConfig;
import com.xubei.shop.ui.views.NoDataView;

import java.util.List;

/**
 * 页面分页工具
 * date：2018/12/24 22:31
 * author：xiongj
 **/
public class PageUtils {

    //刷新模式
    public final static class RefreshMode {

        //全部
        public final static int LOAD_ALL = 0;

        //只有下拉刷新
        public final static int LOAD_REFRESH = 1;

        //只有加载更多
        public final static int LOAD_MORE = 2;
    }

    public interface OnNotifyItemListener {
        //刷新数据开始位置。刷新数据的数量
        void notifyItemRangeChanged(int start, int size);
    }


    //默认加载上拉和下拉,默认系统分页数量
    public static <T> Integer doSuccess(Integer requestNo, List<T> list, List<T> requestList, OnNotifyItemListener notifyItemListener, NoDataView noDataView, SmartRefreshLayout refreshLayout) {
        return doSuccess(requestNo, list, requestList, notifyItemListener, noDataView, refreshLayout, RefreshMode.LOAD_ALL, AppConfig.PAGE_SIZE);
    }

    //默认系统分页数量
    public static <T> Integer doSuccess(Integer requestNo, List<T> list, List<T> requestList, OnNotifyItemListener notifyItemListener, NoDataView noDataView, SmartRefreshLayout refreshLayout, int refreshMode) {
        return doSuccess(requestNo, list, requestList, notifyItemListener, noDataView, refreshLayout, refreshMode, AppConfig.PAGE_SIZE);
    }


    /**
     * 成功后的分页逻辑处理
     *
     * @param requestNo          请求的分页
     * @param list               数据源
     * @param requestList        请求返回的当前数据
     * @param notifyItemListener 刷新监听
     * @param noDataView         空白相关状态界面
     * @param refreshLayout      下拉上拉控件
     * @param refreshMode        刷新模式
     * @param pageSize           分页数量
     * @param <T>                泛型
     * @return 当前的分页
     */
    public static <T> Integer doSuccess(Integer requestNo, List<T> list, List<T> requestList, OnNotifyItemListener notifyItemListener, NoDataView noDataView, SmartRefreshLayout refreshLayout, int refreshMode, int pageSize) {
        int start = (requestNo - 1) * pageSize;
        //如果是返回的第一页
        if (start == 0) {
            list.clear();
        }
        //回来的数据条数
        int listSize = 0;
        if (requestList != null) {
            listSize = requestList.size();
            list.addAll(requestList);
        }
        //如果列表没有数据，则关闭下拉刷新。（说明为第一次加载第一页。）
        if (list.size() == 0) {
            if (refreshLayout != null) {
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setEnableLoadMore(false);
            }
            if (noDataView != null) {
                noDataView.setType(NoDataView.NO_DATA);
            }
         //   return 1;
        }
        //在有数据的情况下，请求的是第一页则打开下拉刷新
        if (start == 0 && refreshLayout != null && (refreshMode == RefreshMode.LOAD_ALL || refreshMode == RefreshMode.LOAD_REFRESH)) {
            refreshLayout.setEnableRefresh(true);
        }
        //是否还有更多数据
        if (refreshLayout != null && (refreshMode == RefreshMode.LOAD_ALL || refreshMode == RefreshMode.LOAD_MORE)) {
            refreshLayout.setEnableLoadMore(listSize == pageSize);
        }
        //如果不是第一页，并且没有返回的数据
        if (start != 0 && listSize == 0) {
            ToastUtils.showShort("没有更多数据");
        }
        //如果有返回的数据，则刷新
       // if (listSize > 0) {
            //刷新列表
            notifyItemListener.notifyItemRangeChanged(start, listSize);
      //  }
        //如果没有关闭刷新则关闭
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        return requestNo;
    }

    //请求失败处理
    public static void doError(NoDataView noDataView, SmartRefreshLayout refreshLayout) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        if (noDataView != null) {
            noDataView.setType(NoDataView.NET_ERR);
        }
    }
}
