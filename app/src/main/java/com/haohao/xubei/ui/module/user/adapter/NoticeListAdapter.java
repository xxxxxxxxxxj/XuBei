package com.haohao.xubei.ui.module.user.adapter;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.main.model.NoticeBean;
import com.haohao.xubei.utlis.Tools;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;

/**
 * 消息列表适配器
 * date：2018/5/29 18:09
 * author：Seraph
 **/
public class NoticeListAdapter extends BaseQuickAdapter<NoticeBean, BaseViewHolder> {


    @Inject
    public NoticeListAdapter(@Nullable List<NoticeBean> data) {
        super(R.layout.act_notice_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeBean item) {
        String tempTime = item.sendTime;
        if (!StringUtils.isEmpty(tempTime) && tempTime.length() > 10) {
            tempTime = tempTime.substring(0, 10);
        }
        if (item.status == null) {
            item.status = 1;
        }

        helper.setText(R.id.tv_title, item.title == null ? "" : item.title)
                .setText(R.id.tv_description, Tools.html2Txt2(item.secondTitle == null ? "" : item.secondTitle))
                .setText(R.id.tv_time, tempTime == null ? "" : tempTime)
                .setGone(R.id.v_msg_new, item.status == 0);
    }
}
