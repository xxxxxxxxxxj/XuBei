package com.xubei.shop.ui.module.account.presenter;

import android.util.SparseArray;

import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.xubei.shop.AppConstants;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.Api8Service;
import com.xubei.shop.ui.module.account.contract.AccASContract;
import com.xubei.shop.ui.module.account.model.GameAreaBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;

import java.util.List;

import javax.inject.Inject;

/**
 * 大区选择逻辑
 * date：2017/12/18 15:12
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class AccASPresenter extends AccASContract.Presenter {


    private Api8Service api8Service;

    //当前最高级的父类ID
    private String gameId;

    @Inject
    AccASPresenter(Api8Service api8Service, String id) {
        this.api8Service = api8Service;
        this.gameId = id;
    }


    //保存的历史请求数据
    private SparseArray<List<GameAreaBean>> sparseArray = new SparseArray<>();

    //当前列表item
    private GameAreaBean gameBean;

    //最终选择的bean
    private GameAreaBean selectBean;

    @Override
    public void start() {
        try {
            //请求第一次的数据
            doFindChdByGameParent(Long.valueOf(gameId));
        } catch (NumberFormatException e) {
            ToastUtils.showShort("获取游戏id失败");
            mView.finish();
        }

    }

    //点击
    public void onItemClick(GameAreaBean gameAreaBean) {
        //判断是否有缓存数据
        List<GameAreaBean> saveList = sparseArray.get(gameAreaBean.id);
        if (saveList == null) {//没有请求过
            doFindChdByGameParent(gameAreaBean.id);
        } else if (saveList.size() != 0) {//有缓存下级数据列表
            List<GameAreaBean> list = sparseArray.get(gameAreaBean.id);
            mView.setListDate(list);
            gameBean = list.get(0);//设置当前的列表对应的数据list
        } else {//已经请求过，没有下级数据
            selectBean = gameAreaBean;
            //设置选中
            mView.setSelectItem(selectBean);
        }
    }


    //保存
    public void doSave() {
        if (selectBean == null) {
            ToastUtils.showShort("请选择区服");
            return;
        }
        RxBus.get().post(AppConstants.RxBusAction.TAG_SELECT_AREA, selectBean);
        mView.finish();
    }

    //返回
    public void doBackPressed() {
        //每次返回则清空选择
        selectBean = null;
        mView.setSelectItem(null);
        //获取当前列表数据的父类ID ,如果第一次进来，或者当前为最高级列表数据，则关闭
        if (gameBean == null || gameBean.parentId == -1) {
            mView.finish();
        } else {
            GameAreaBean topBean = getValueOfID(gameBean.parentId);
            if (topBean == null) {
                mView.finish();
            } else {
                List<GameAreaBean> saveList = sparseArray.get(topBean.parentId);
                //设置当前列表对应的item
                gameBean = topBean;
                mView.setListDate(saveList);
            }
        }

    }

    //通过当前的parent_id。查找对应父类的item
    private GameAreaBean getValueOfID(int parent_id) {
        GameAreaBean topBean = null;
        //获取当前数据列表上一级数据列表的id(进行数据对比，当前选择数据的parent_id == 父类的 id)
        startId:
        for (int i = 0; i < sparseArray.size(); i++) {
            List<GameAreaBean> list = sparseArray.valueAt(i);
            for (GameAreaBean gameAreaBean : list) {
                if (gameAreaBean.id == parent_id) {
                    topBean = gameAreaBean;
                    break startId;
                }
            }
        }
        return topBean;
    }

    //查询游戏区服  type  0 1级数据  1 2级数据
    private void doFindChdByGameParent(final long parentId) {
        api8Service.findChdByGameParent(parentId)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle()).subscribe(new ABaseSubscriber<List<GameAreaBean>>(mView) {
            @Override
            public void onSuccess(List<GameAreaBean> gameBeans) {
                //判断是否有保存，没有则保存
                if (sparseArray.indexOfKey((int) parentId) < 0) {
                    sparseArray.append((int) parentId, gameBeans);
                }
                if (gameBeans.size() == 0) {
                    //找到对应的点击的item
                    selectBean = getValueOfID((int) parentId);
                    //查找对应的位置
                    mView.setSelectItem(selectBean);
                    return;
                }
                //判断返回数据的类型
                mView.setListDate(gameBeans);
                gameBean = gameBeans.get(0);
            }

            @Override
            public void onError(String errStr) {
                ToastUtils.showShort(errStr);
            }
        });
    }

}
