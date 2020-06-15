package com.haohao.xubei.ui.module.account.presenter;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.Api8Service;
import com.haohao.xubei.ui.module.account.contract.AccRSearchContract;
import com.haohao.xubei.ui.module.account.model.AccRSResultBean;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * 发布搜索
 * date：2017/11/28 11:23
 * author：Seraph
 **/
public class AccRSearchPresenter extends AccRSearchContract.Presenter {


    private Api8Service api8Service;

    @Inject
    AccRSearchPresenter(Api8Service api8Service) {
        this.api8Service = api8Service;
    }


    //热门搜索
    private List<AccRSResultBean> searchList = new ArrayList<>();


    @Override
    public void start() {

    }


    //搜索
    public void doSearch(String searchStr) {
        if (ObjectUtils.isEmpty(searchStr)) {
            ToastUtils.showShort("请输入您要发布的游戏名称");
            return;
        }
        indexGameList(searchStr);
    }


    //点击的位置
    public void doSearchPosition(int position) {
        AccRSResultBean accRSesultBean = searchList.get(position);
        GameBean gameBean = new GameBean();
        gameBean.game_id = accRSesultBean.game_id;
        gameBean.setGameName(accRSesultBean.game);
        ARouter.getInstance().build(AppConstants.PagePath.ACC_RELEASE).withSerializable("bean",gameBean).navigation();
    }


    private void indexGameList(String searchStr) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("game", searchStr);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort("提交数据错误");
            return;
        }
        RequestBody jsonBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        api8Service.indexGameList(jsonBody)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading("正在搜索").setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<List<AccRSResultBean>>(mView) {
                    @Override
                    public void onSuccess(List<AccRSResultBean> accountList) {
                        searchList.clear();
                        searchList.addAll(accountList);
                        mView.setSearchList(searchList);
                        if (searchList.size() == 0) {
                            ToastUtils.showShort("暂无数据");
                        }
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

}
