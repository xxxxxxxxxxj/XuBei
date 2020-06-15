package com.haohao.xubei.ui.module.account.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.account.model.GameAreaBean;

import javax.inject.Inject;

/**
 * 账号区服选择
 * date：2017/12/18 15:27
 * author：Seraph
 *
 **/
public class AccASAdapter extends BaseQuickAdapter<GameAreaBean, BaseViewHolder> {


    @Inject
    AccASAdapter() {
        super(R.layout.act_acc_as_item);
    }

    private GameAreaBean areaBean;

    @Override
    protected void convert(BaseViewHolder helper, GameAreaBean item) {
        helper.setText(R.id.tv_area, item.gameName)
                .setVisible(R.id.imageView, areaBean != null
                        && areaBean.id == item.id);
    }

    public void setSelectItem(GameAreaBean selectBean) {
        this.areaBean = selectBean;
        notifyDataSetChanged();
    }
}
