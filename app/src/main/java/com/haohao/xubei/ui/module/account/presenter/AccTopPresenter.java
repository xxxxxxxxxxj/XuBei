package com.haohao.xubei.ui.module.account.presenter;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.account.contract.AccTopContract;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.account.model.GoodsStickBean;
import com.haohao.xubei.ui.module.account.model.OutGoodsBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.base.BaseData;
import com.haohao.xubei.ui.views.NoDataView;
import com.haohao.xubei.utlis.PageUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * 商品置顶
 * date：2018/12/5 15:24
 * author：xiongj
 **/
public class AccTopPresenter extends AccTopContract.Presenter {

    private UserBeanHelp userBeanHelp;

    private ApiUserNewService apiUserNewService;

    private NoDataView noDataView;

    @Inject
    AccTopPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService, NoDataView noDataView) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.noDataView = noDataView.setNoDataMsg("暂无可置顶商品");
    }

    //商品列表
    private List<OutGoodsBean> list = new ArrayList<>();

    private int pageNo = 0;

    //游戏类目
    private List<String> letterList = new ArrayList<>();

    //当前字母的选择位置
    private int selectLetterPosition = 0;
    //游戏类目
    private List<GameBean> gameAllList = new ArrayList<>();
    //筛选后的游戏类目
    private List<GameBean> gameList = new ArrayList<>();

    //选择的筛选游戏
    private GameBean selectGameBean;

    //选择的游戏筛选位置
    private int selectItem = -1;

    @Override
    public void start() {
        mView.initView(list, noDataView);
        //设置筛选的数据
        mView.setGameList(letterList, gameList);
        onRefresh();
        noDataView.setOnClickListener(v -> onRefresh());
    }

    //显示游戏选择
    public void onShowGameSelect() {
        //获取游戏筛选类目
        if (gameList.size() == 0) {
            getGoodsManageOfGame();
        } else {
            //显示筛选pop
            mView.showSelectGamePop();
        }
    }


    //转换数据
    private void processingData() {
        if (gameAllList.size() == 0) {
            ToastUtils.showShort("暂无商品筛选");
            return;
        }
        //显示筛选pop
        mView.showSelectGamePop();
        //获取当前选择的类型下的数据
        letterList.clear();
        //转换首字母
        for (int i = 0; i < gameAllList.size(); i++) {
            //添加字母数据源(如果不包含)
            if (!letterList.contains(gameAllList.get(i).firstWord)) {
                letterList.add(gameAllList.get(i).firstWord);
            }
        }
        //排序字母升序
        Collections.sort(letterList);
        //添加第一个为热
        letterList.add(0, "全");
        //通过字母筛选
        onLetterClick(selectLetterPosition);
    }


    //点击字母筛选
    public void onLetterClick(int position) {
        this.selectLetterPosition = position;
        //根据当前字母选择筛选对应的数据
        gameList.clear();
        //-1为全部游戏
        if (selectLetterPosition == 0) {
            gameList.addAll(gameAllList);
        } else {
            //通过字母筛选
            String tempLetter = letterList.get(selectLetterPosition);
            for (GameBean gameBean : gameAllList) {
                if (tempLetter.equalsIgnoreCase(gameBean.firstWord)) {
                    gameList.add(gameBean);
                }
            }
        }
        //刷新选择的游戏列表
        mView.updateSelectView(selectLetterPosition);

    }

    //点击游戏筛选
    public void onGameSelect(int position) {
        //获取点击的bean
        selectGameBean = gameList.get(position);
        //设置选中游戏的颜色
        mView.setGameSelect(selectGameBean);
        mView.onAutoRefresh();
    }


    //反选点击
    public void onItemClick(int position) {
        //反选
        list.get(position).isSelect = !list.get(position).isSelect;
        //刷新
        mView.notifyItemRangeChanged(position, 1, "select");
        //判断是否全部选中（设置全选按钮状态）
        mView.setAllSelectType(isAllSelect());
        //设置所以选中商品的价格
        mView.setAllSelectPrice(allPayPrice());

    }


    //是否全部选中
    private Boolean isAllSelect() {
        if (list.size() == 0) {
            return null;
        }
        for (OutGoodsBean outGoodsBean : list) {
            if (!outGoodsBean.isSelect) {
                return false;
            }
        }
        return true;
    }

    //计算选中的价格
    private double allPayPrice() {
        BigDecimal allPriceBigDecimal = BigDecimal.ZERO;
        for (OutGoodsBean outGoodsBean : list) {
            if (outGoodsBean.isSelect) {
                allPriceBigDecimal = allPriceBigDecimal.add(BigDecimal.valueOf(outGoodsBean.stickPrice));
            }
        }
        return allPriceBigDecimal.doubleValue();
    }

    //计算选中的价格
    private String allSelectIdsStr() {
        StringBuilder sbAllSelect = new StringBuilder();
        for (OutGoodsBean outGoodsBean : list) {
            if (outGoodsBean.isSelect) {
                sbAllSelect.append(outGoodsBean.goodsId).append(",");
            }
        }
        String idsStr = sbAllSelect.toString();
        if (!StringUtils.isEmpty(idsStr)) {
            idsStr = idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr;
    }

    //反选
    public void allSelectInverse() {
        //如果全部选中，则设置全部不选中
        Boolean isAllSelect = isAllSelect();
        if (isAllSelect != null) {
            for (int i = 0; i < list.size(); i++) {
                list.get(i).isSelect = !isAllSelect;
            }
            //列表点击
            mView.notifyItemRangeChanged(0, list.size(), "select");
            //全选按钮
            mView.setAllSelectType(!isAllSelect);
            //设置所以选中商品的价格
            mView.setAllSelectPrice(allPayPrice());
        } else {
            ToastUtils.showShort("没有可选商品");
        }
    }

    //开始支付
    public void startPay() {
        //获取勾选的商品id
        String selectIds = allSelectIdsStr();
        if (StringUtils.isEmpty(selectIds)) {
            ToastUtils.showShort("请选择需要置顶的商品");
            return;
        }
        goodsStick(selectIds);
    }


    public void onRefresh() {
        getGoodsManage(1);
    }

    public void onLoadMore() {
        getGoodsManage(pageNo + 1);
    }

    //获取我的商品列表信息
    private void getGoodsManage(final int tempPageNo) {
        HashMap<String, Object> map = new HashMap<>();
        //类型: 0 出售 1 出租
        map.put("tradingWay", 1);
        //展示中
        map.put("goodsStatus", "3");
        //未置顶
        map.put("stickStatus", "1");
        //游戏筛选
        if (selectGameBean != null) {
            map.put("gameId", selectGameBean.game_id);
        }
        map.put("pageIndex", tempPageNo);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        apiUserNewService.getGoodsManage(userBeanHelp.getAuthorization(), map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> noDataView.setType(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<OutGoodsBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<OutGoodsBean> baseData) {
                        pageNo = PageUtils.doSuccess(tempPageNo, list, baseData.data,
                                (start, size) -> mView.notifyItemRangeChanged(start, size, null),
                                noDataView,
                                mView.getSrl());
                        //进行重新刷新全选按钮状态
                        mView.setAllSelectType(isAllSelect());
                        //设置所以选中商品的价格
                        mView.setAllSelectPrice(allPayPrice());
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        PageUtils.doError(noDataView, mView.getSrl());
                    }
                });
    }


    //商品置顶
    private void goodsStick(String selectIds) {
        apiUserNewService.goodsStick(userBeanHelp.getAuthorization(), selectIds, false)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<GoodsStickBean>(mView) {
                    @Override
                    protected void onSuccess(GoodsStickBean stickBean) {
                        ToastUtils.showShort("成功置顶" + stickBean.succNum + "件商品");
                        //通知刷新我的账号管理列表
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ACCOUNT_SELLER_LIST, true);
                        //通知跳转已置顶列表
                        RxBus.get().post(AppConstants.RxBusAction.TAG_ACCOUNT_SELLER_LIST_TYPE, 5);
                        //刷新列表
                        mView.finish();
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


    //获取筛选游戏类目
    private void getGoodsManageOfGame() {
        apiUserNewService.getGoodsManageOfGame(userBeanHelp.getAuthorization(), "1")
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<GameBean>>(mView) {
                    @Override
                    protected void onSuccess(List<GameBean> gameBeanList) {
                        gameAllList.clear();
                        gameAllList.addAll(gameBeanList);
                        processingData();
                    }

                    @Override
                    protected void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }


}
