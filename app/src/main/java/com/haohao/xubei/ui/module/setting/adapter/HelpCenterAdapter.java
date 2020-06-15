package com.haohao.xubei.ui.module.setting.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.setting.model.ProblemBean;

import java.util.List;

import javax.inject.Inject;

/**
 * 帮助中心适配器
 * date：2018/8/8 09:29
 * author：Seraph
 *
 **/
public class HelpCenterAdapter extends BaseQuickAdapter<ProblemBean, BaseViewHolder> {

    @Inject
    public HelpCenterAdapter(@Nullable List<ProblemBean> data) {
        super(R.layout.act_setting_help_center_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProblemBean item) {
        helper.setText(R.id.tv_title, item.menuName)
                .setImageResource(R.id.iv_logo, item.logo);
    }


}
