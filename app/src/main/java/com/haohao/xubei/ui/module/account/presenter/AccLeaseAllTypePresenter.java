package com.haohao.xubei.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConfig;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiPHPService;
import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.ui.module.account.contract.AccLeaseAllTypeContract;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.main.model.GameTypeBean;
import com.haohao.xubei.ui.views.NoDataView;
import com.haohao.xubei.utlis.PinyinUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * 账号租赁所有类型逻辑
 * date：2017/2/21 17:10
 * author：Seraph
 **/
public class AccLeaseAllTypePresenter extends AccLeaseAllTypeContract.Presenter {


    private ApiPHPService apiPHPService;

    //记录选中的位置(默认第一个)
    private int selectTypePosition;

    @Inject
    AccLeaseAllTypePresenter(ApiPHPService apiPHPService, @QualifierType("type") Integer type) {
        this.apiPHPService = apiPHPService;
        this.selectTypePosition = type;
    }

    //总数据源
    private List<GameTypeBean> gameTypes = new ArrayList<>();

    //字母赛选数据源
    private List<String> letterList = new ArrayList<>();

    //当前赛选后的列表数据源
    private List<GameBean> gameList = new ArrayList<>();

    //当前选择的字母（默认第一个）
    private int selectLetterPosition = 0;

    @Override
    public void start() {
        //初始化
        mView.initLayout(letterList, gameList);
        //如果没有数据 则进行请求
        if (gameTypes.size() == 0) {
            doRentPage();
        }
    }

    //转换数据
    private void processingData() {
        if (gameTypes.size() == 0) {
            return;
        }
        //获取当前选择的类型下的数据
        int tempSize = gameTypes.get(selectTypePosition).content.size();
        letterList.clear();
        //转换首字母
        for (int i = 0; i < tempSize; i++) {
            String tempFirstLetter = PinyinUtils.getPinYinFirstLetter(gameTypes.get(selectTypePosition).content.get(i).getGameName()).toUpperCase();
            gameTypes.get(selectTypePosition).content.get(i).firstLetter = tempFirstLetter;
            //添加字母数据源(如果不包含)
            if (!letterList.contains(tempFirstLetter)) {
                letterList.add(tempFirstLetter);
            }
        }
        //排序字母升序
        Collections.sort(letterList);
        //添加第一个为热
        letterList.add(0, "热");
        //根据当前字母选择筛选对应的数据
        onLetterClick(selectLetterPosition);
    }

    //类型选择
    public void onTypeClick(int position) {
        if (position == selectTypePosition) {
            return;
        }
        this.selectTypePosition = position;
        //重置游戏字母为第一个
        this.selectLetterPosition = 0;
        if (gameTypes.size() == 0) {
            doRentPage();
        } else {
            processingData();
        }

    }

    //字母筛选点击
    public void onLetterClick(int position) {
        this.selectLetterPosition = position;
        //根据当前字母选择筛选对应的数据
        try {
            String tempLetter = letterList.get(selectLetterPosition);
            gameList.clear();
            if ("热".equals(tempLetter)) {
                gameList.addAll(gameTypes.get(selectTypePosition).content);
            } else {
                for (GameBean gameBean : gameTypes.get(selectTypePosition).content) {
                    if (tempLetter.equalsIgnoreCase(gameBean.firstLetter)) {
                        gameList.add(gameBean);
                    }
                }
            }
            //刷新列表
            mView.updateView(selectLetterPosition);
        } catch (IndexOutOfBoundsException e) {
            ToastUtils.showShort("获取数据异常，请重新加载页面数据");
        }

    }


    //跳转对应租赁游戏账号的列表
    public void onItemClick(int position) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_LIST).withSerializable("bean", gameList.get(position)).navigation();
    }


    //新的请求租号列表接口
    public void doRentPage() {
        apiPHPService.appconfigRentPage(AppConfig.APP_KEY_PHP)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoData(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<GameTypeBean>>() {
                    @Override
                    public void onSuccess(List<GameTypeBean> typeBeans) {
                        gameTypes.clear();
                        gameTypes.addAll(typeBeans);
                        if (mView == null) {
                            return;
                        }
                        //刷新type
                        mView.setUITypeDate(gameTypes, selectTypePosition);
                        //处理数据
                        processingData();
                        mView.setNoData(NoDataView.LOADING_OK);
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoData(NoDataView.NET_ERR);
                    }
                });
    }


}
