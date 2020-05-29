package com.xubei.shop.ui.module.account.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xubei.shop.R;

import javax.inject.Inject;

/**
 * poplist选择适配器（平台和排序选择用）
 * date：2018/2/7 09:55
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class PopupListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private String selectStr;

    @Inject
    PopupListAdapter() {
        super(R.layout.popup_list_layout_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_type, item)
                .setVisible(R.id.iv_selected, selectStr.equals(item))
                .setTextColor(R.id.tv_type, selectStr.equals(item) ? 0xff0ea8f0 : 0xff666666);
    }

    //设置选中项目
    public void setSelectStr(String selectStr) {
        this.selectStr = selectStr;
        notifyDataSetChanged();
    }
}
