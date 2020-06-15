package com.haohao.xubei.ui.module.user.presenter;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.data.db.help.UserBeanHelp;
import com.haohao.xubei.data.db.table.UserTable;
import com.haohao.xubei.data.network.rx.RxSchedulers;
import com.haohao.xubei.data.network.service.ApiUserNewService;
import com.haohao.xubei.ui.module.base.ABaseSubscriber;
import com.haohao.xubei.ui.module.user.contract.UpdateNickNameContract;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import okhttp3.RequestBody;

/**
 * 修改昵称逻辑类
 * date：2018/9/26 14:09
 * author：xiongj
 **/
public class UpdateNickNamePresenter extends UpdateNickNameContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    @Inject
    public UpdateNickNamePresenter(ApiUserNewService apiUserNewService, UserBeanHelp userBeanHelp) {
        this.apiUserNewService = apiUserNewService;
        this.userBeanHelp = userBeanHelp;
    }

    @Override
    public void start() {
        //设置用户昵称
        mView.setUserNickName(userBeanHelp.getUserBean().getUsernick());
    }

    //保存用户设置昵称
    public void doSave(String inputStr) {
        if (ObjectUtils.isEmpty(inputStr)) {
            ToastUtils.showShort("请输入昵称");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userNick", inputStr);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtils.showShort("提交数据错误");
            return;
        }
        RequestBody jsonBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
        apiUserNewService.saveUserInfoNew(userBeanHelp.getAuthorization(), jsonBody)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String str) {
                        ToastUtils.showShort("保存成功");
                        UserTable userTable = userBeanHelp.getUserBean();
                        userTable.setUsernick(inputStr);
                        userBeanHelp.save(userTable);
                        mView.finish();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }
}
