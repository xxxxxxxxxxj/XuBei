package com.haohao.xubei.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.Api8Service;
import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.ui.module.account.contract.AccListContract;
import com.haohao.xubei.ui.module.account.model.AccBean;
import com.haohao.xubei.ui.module.account.model.GameAllAreaBean;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.account.model.GameSearchRelationBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.views.NoDataView;
import com.haohao.xubei.utlis.PageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * 分类到->账号列表
 * date：2017/11/28 11:23
 * author：Seraph
 **/
public class AccListPresenter extends AccListContract.Presenter {

    private Api8Service api8Service;

    private GameBean gameBean;

    private boolean isFree;

    //排序数据源
    private List<String> sortList;
    //选择排序
    private String selectSort;

    private NoDataView noDataView;

    @Inject
    AccListPresenter(Api8Service api8Service, GameBean gameBean, @QualifierType("sort") List<String> sortList, NoDataView noDataView) {
        this.api8Service = api8Service;
        this.gameBean = gameBean;
        this.sortList = sortList;
        this.selectSort = sortList.get(0);
        this.noDataView = noDataView;
        this.isFree = "mianfei".equals(gameBean.goto_link);
    }


    private List<AccBean> list = new ArrayList<>();

    private int pageNo = 0;

    //平台数据源
    private List<String> platformList = new ArrayList<>();
    //选择平台
    private String selectPlatform;

    //高级筛选配置
    private List<GameSearchRelationBean> relationList = new ArrayList<>();

    @Override
    public void start() {
        mView.initLayout(list, noDataView, isFree);
        mView.setTitle(gameBean.getGameName());
        initFilters();
        noDataView.setOnClickListener(v -> doRefresh());
    }

    //初始化一些筛选条件
    private void initFilters() {
        //根据游戏类型，确定平台
        if ("1".equals(gameBean.game_type)) {
            platformList.add("PC");
        } else {
            platformList.add("安卓");
            platformList.add("苹果");
        }
        //服务器
        //排序选择列表
        //其它的筛选数据
        /*设置初始的筛选条件*/
        //平台默认给第一个，服务器，排序，其它的都默认
        selectPlatform = platformList.get(0);
        mView.setFilterPlatform(selectPlatform);
        doRefresh();
    }

    //跳转详情
    public void onItemClick(int position) {
        if (position >= list.size()) {
            ToastUtils.showShort("查看详情失败，请刷新列表后重试");
            return;
        }
        if (isFree) {
            ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL_FREE).withString("id", list.get(position).id).navigation();
        } else {
            ARouter.getInstance().build(AppConstants.PagePath.ACC_DETAIL).withString("id", list.get(position).id).navigation();
        }
    }

    //平台筛选
    public void doFilterPlatform() {
        mView.onShowPopFilter(platformList, selectPlatform, 1);
    }

    private GameAllAreaBean gameAreaBean;

    //服务器筛选
    public void doFilterArea(final int p) {
        if (gameAreaBean == null) {
            api8Service.findGameAreaById(gameBean.game_id)
                    .compose(RxSchedulers.io_main_business())
                    .as(mView.bindLifecycle())
                    .subscribe(new ABaseSubscriber<GameAllAreaBean>(mView) {
                        @Override
                        public void onSuccess(GameAllAreaBean gameAllAreaBean) {
                            gameAreaBean = gameAllAreaBean;
                            //开始显示弹框
                            showAreaFilter(p);
                        }

                        @Override
                        public void onError(String errStr) {
                            ToastUtils.showShort(errStr);
                        }
                    });
        } else {
            //显示弹框
            showAreaFilter(p);
        }
    }

    //大区选择位置
    private int areaSelect = -1;

    //显示服务器弹框
    private void showAreaFilter(int p) {
        if (gameAreaBean.children != null && gameAreaBean.children.size() > 0) {
            areaSelect = p;
            //显示弹框 默认显示第0个
            mView.showAreaPop(gameAreaBean.children, p);
        } else {
            ToastUtils.showShort("暂无服务器筛选");
        }
    }

    //服务器选择
    private GameAllAreaBean serverSelect = null;

    //服务器筛选
    public void doFilterServer(GameAllAreaBean areaBean) {
        serverSelect = areaBean;
        //关闭pop
        mView.onCloseAreaPop();
        mView.onAutoRefresh();
    }

    //选择排序
    public void doSelectedSort() {
        mView.onShowPopFilter(sortList, selectSort, 2);
    }


    //显示更多筛选
    public void onMoreFilter() {
        //判断是否有数据源，没有则请求数据
        if (relationList.size() == 0) {
            getGameSearchRelation();
        } else {
            mView.doUpdataMoreView(relationList);
        }
    }

    //刷新筛选界面
    public void onUpdataSelectView() {
        mView.doUpdataMoreView(relationList);
    }


    //重置选项
    public void onResetSelect() {
        for (int i = 0; i < relationList.size(); i++) {
            relationList.get(i).setSelectValue(null);
        }
        onUpdataSelectView();
    }

    //其它筛选
    public void doFilterOther(HashMap<String, String> hashMap) {
        //获取区间筛选的值
        for (String key : hashMap.keySet()) {
            for (int i = 0; i < relationList.size(); i++) {
                if (relationList.get(i).paramName.equals(key) && !"-".equals(hashMap.get(key))) {
                    GameSearchRelationBean.OgssBean ogssBean = new GameSearchRelationBean.OgssBean();
                    ogssBean.val = hashMap.get(key);
                    relationList.get(i).setSelectValue(ogssBean);
                }
            }
        }
        //关闭筛选弹出
        mView.onCloseOtherFilter();
        mView.onAutoRefresh();
    }

    //平台选择
    public void doPlatformSelect(int position) {
        selectPlatform = platformList.get(position);
        mView.setFilterPlatform(selectPlatform);
        //开始请求刷新接口
        mView.onAutoRefresh();
    }

    //排序选择
    public void doSortSelect(int position) {
        selectSort = sortList.get(position);
        //开始请求刷新接口
        mView.onAutoRefresh();
    }

    public void doRefresh() {
        //刷新第一页
        findGoodsList(1);
    }

    public void nextPage() {
        //下一页
        findGoodsList(pageNo + 1);
    }


    //查询游戏账号列表
    private void findGoodsList(final int tempPageNo) {
        HashMap<String, Object> map = new HashMap<>();
        //游戏id
        map.put("gameId", gameBean.game_id);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        map.put("pageIndex", tempPageNo);
        map.put("businessNo", AppConfig.getChannelValue());
        //筛选条件(大区)
        if (areaSelect != -1 && gameAreaBean != null) {
            map.put("area", gameAreaBean.children.get(areaSelect).gameName);
        }
        //服务器
        if (serverSelect != null && ObjectUtils.isNotEmpty(serverSelect.id)) {
            map.put("server", serverSelect.gameName);
        }
        //系统筛选
        if (selectPlatform != null) {
            switch (selectPlatform) {
                case "安卓":
                    map.put("system", "0");
                    break;
                case "苹果":
                    map.put("system", "1");
                    break;
            }
        }
        if (selectSort != null) {
            //排序
            switch (selectSort) {
                case "价格由高到低":
                    map.put("priceOrderBy", "1");
                    break;
                case "价格由低到高":
                    map.put("priceOrderBy", "0");
                    break;
                case "发布时间有远到近":
                    map.put("timeOrderBy", "0");
                    break;
                case "发布时间由近到远":
                    map.put("timeOrderBy", "1");
                    break;
            }
        }

        for (GameSearchRelationBean bean : relationList) {
            if (bean.getSelectValue() != null && !StringUtils.isEmpty(bean.getSelectValue().val)) {
                map.put(bean.paramName, bean.getSelectValue().val);
            }
        }

        //免费试玩
        if (isFree) {
            map.put("freeArea", 1);
        }
        api8Service.findGoodsList(map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> noDataView.setType(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<AccBean>>() {
                    @Override
                    public void onSuccess(BaseData<AccBean> accountList) {
                        pageNo = PageUtils.doSuccess(tempPageNo, list, accountList.list,
                                (start, size) -> mView.notifyItemRangeChanged(start, size), noDataView, mView.getSrl());
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        PageUtils.doError(noDataView, mView.getSrl());
                    }
                });
    }

    //获取高级筛选
    private void getGameSearchRelation() {
        api8Service.getGameSearchRelation(gameBean.game_id)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<GameSearchRelationBean>>(mView) {
                    @Override
                    protected void onSuccess(List<GameSearchRelationBean> gameSearchRelationList) {
                        relationList.clear();
                        relationList.addAll(gameSearchRelationList);
                        //刷新界面
                        mView.doUpdataMoreView(relationList);
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}
