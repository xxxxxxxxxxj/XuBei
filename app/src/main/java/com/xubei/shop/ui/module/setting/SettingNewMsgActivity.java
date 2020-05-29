package com.xubei.shop.ui.module.setting;

import android.os.Bundle;
import android.widget.CompoundButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActSettingNewMsgBinding;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.module.setting.contract.SettingNewMsgContract;
import com.xubei.shop.ui.module.setting.presenter.SettingNewMsgPresenter;
import com.xubei.shop.ui.module.user.model.MessageConfigBean;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;


/**
 * 设置新消息界面
 * date：2017/12/4 14:25
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.SETTING_NEWMSG)
public class SettingNewMsgActivity extends ABaseActivity<SettingNewMsgContract.Presenter> implements SettingNewMsgContract.View {

    private ActSettingNewMsgBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_setting_new_msg);
    }

    @Inject
    SettingNewMsgPresenter presenter;

    @Override
    protected SettingNewMsgContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("新消息通知");
        binding.switchMsgObtained.setOnCheckedChangeListener(this::onCheckedChanged);
        binding.switchMsgRightsProtection.setOnCheckedChangeListener(this::onCheckedChanged);
        binding.switchMsgTopping.setOnCheckedChangeListener(this::onCheckedChanged);
        binding.switchMsgZl.setOnCheckedChangeListener(this::onCheckedChanged);
        binding.switchMsgToppingEnd.setOnCheckedChangeListener(this::onCheckedChanged);
        binding.switchMsgSys.setOnCheckedChangeListener(this::onCheckedChanged);
        presenter.start();
    }


    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_msg_obtained://下架通知
                presenter.onMsgObained(isChecked);
                break;
            case R.id.switch_msg_rights_protection://维权通知
                presenter.onMsgRightsProtection(isChecked);
                break;
            case R.id.switch_msg_topping://置顶通知
                presenter.onMsgTopping(isChecked);
                break;
            case R.id.switch_msg_topping_end://置顶结束通知
                presenter.onMsgToppingEnd(isChecked);
                break;
            case R.id.switch_msg_zl://租赁通知
                presenter.onMsgZl(isChecked);
                break;
            case R.id.switch_msg_sys://系统消息
                presenter.onMsgSys(isChecked);
                break;
        }
    }

    @Override
    public void showMessageConfig(List<MessageConfigBean> list) {
        for (MessageConfigBean bean : list) {
            switch (bean.type) {
                case "1"://商品下架
                    binding.switchMsgObtained.setChecked("1".equals(bean.status));
                    break;
                case "2"://维权
                    binding.switchMsgRightsProtection.setChecked("1".equals(bean.status));
                    break;
                case "3"://置顶
                    binding.switchMsgTopping.setChecked("1".equals(bean.status));
                    break;
                case "4"://置顶结束
                    binding.switchMsgToppingEnd.setChecked("1".equals(bean.status));
                    break;
                case "5"://系统消息
                    binding.switchMsgSys.setChecked("1".equals(bean.status));
                    break;
                case "6"://系统消息
                    binding.switchMsgZl.setChecked("1".equals(bean.status));
                    break;
            }
        }
    }
}
