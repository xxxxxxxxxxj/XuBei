package com.haohao.xubei.ui.module.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActAccRSearchBinding;
import com.haohao.xubei.ui.module.account.contract.AccRSearchContract;
import com.haohao.xubei.ui.module.account.model.AccRSResultBean;
import com.haohao.xubei.ui.module.account.presenter.AccRSearchPresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号发布搜索
 * date：2017/11/28 11:13
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.ACC_R_SEARCH)
public class AccRSearchActivity extends ABaseActivity<AccRSearchContract.Presenter> implements AccRSearchContract.View {

    private ActAccRSearchBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_search);
    }

    @Inject
    AccRSearchPresenter presenter;

    @Override
    protected AccRSearchContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LayoutInflater inflater;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.etSearch.setHint("请输入游戏名称");
        binding.appbar.ivBack.setOnClickListener(v -> onBackPressed());
        binding.appbar.tvSearch.setOnClickListener(v -> {
            startSearch();
            KeyboardUtils.hideSoftInput(this);
        });
        //点击输入法右下方搜索
        RxTextView.editorActionEvents(binding.appbar.etSearch)
                .as(bindLifecycle()).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                startSearch();
            }
        });
        presenter.start();
    }

    /**
     * 开始搜索
     */
    private void startSearch() {
        presenter.doSearch(binding.appbar.etSearch.getText().toString().trim());
        KeyboardUtils.hideSoftInput(this);
    }


    @Override
    public void setSearchList(List<AccRSResultBean> gameBeans) {
        if (gameBeans != null && gameBeans.size() > 0) {
            binding.dlSearch.removeAllViews();
            for (int i = 0; i < gameBeans.size(); i++) {
                TextView textView = (TextView) inflater.inflate(R.layout.act_acc_search_item, binding.dlSearch, false);
                textView.setTag(i);
                textView.setText(gameBeans.get(i).game);
                textView.setOnClickListener(v -> presenter.doSearchPosition((Integer) v.getTag()));
                binding.dlSearch.addView(textView);
            }
        } else {
            ToastUtils.showShort("暂无相关游戏");
        }
    }

}
