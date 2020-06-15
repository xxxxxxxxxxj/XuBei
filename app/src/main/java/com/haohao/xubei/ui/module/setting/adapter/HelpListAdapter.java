package com.haohao.xubei.ui.module.setting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.module.setting.model.QuestionBean;
import com.haohao.xubei.utlis.Tools;

import javax.inject.Inject;

/**
 * 帮助类型列表
 * date：2018/8/8 10:18
 * author：Seraph
 *
 **/
public class HelpListAdapter extends BaseQuickAdapter<QuestionBean, BaseViewHolder> {


    @Inject
    public HelpListAdapter() {
        super(R.layout.act_setting_help_list_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, QuestionBean item) {
        helper.setText(R.id.tv_question, item.articleTitle)
                .setText(R.id.tv_answer, Tools.html2Txt2(item.articleContent))
                .setText(R.id.tv_time, item.createTime.split(" ")[0]);
    }
}
