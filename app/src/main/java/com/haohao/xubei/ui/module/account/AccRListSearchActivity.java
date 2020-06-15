package com.haohao.xubei.ui.module.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.data.db.table.SearchHistoryTable;
import com.haohao.xubei.databinding.ActAccRListSearchBinding;
import com.haohao.xubei.ui.module.account.contract.AccRListSearchContract;
import com.haohao.xubei.ui.module.account.presenter.AccRListSearchPresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 我的账号搜索
 * date：2017/11/28 11:13
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_R_LISTSEARCH)
public class AccRListSearchActivity extends ABaseActivity<AccRListSearchContract.Presenter> implements AccRListSearchContract.View {

    private ActAccRListSearchBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_r_list_search);
    }

    @Inject
    AccRListSearchPresenter presenter;

    @Override
    protected AccRListSearchContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LayoutInflater inflater;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.etSearch.setHint("请输入搜索关键字");
        binding.appbar.ivBack.setOnClickListener(v -> onBackPressed());
        binding.appbar.tvSearch.setOnClickListener(v -> {
            startSearch();
            KeyboardUtils.hideSoftInput(this);
        });
        binding.ivDelete.setOnClickListener(v -> presenter.cleanSearchHistory());
        //点击输入法右下方搜索
        RxTextView.editorActionEvents(binding.appbar.etSearch).as(bindLifecycle()).subscribe(textViewEditorActionEvent -> {
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
        binding.appbar.etSearch.setText("");
        KeyboardUtils.hideSoftInput(this);
    }


    @Override
    public void startSearchResultActivity(String searchKey) {
        ARouter.getInstance().build(AppConstants.PagePath.ACC_R_LISTSRESULT).withString("key", searchKey).navigation();
    }

    @Override
    public void setSearchHistoryList(List<SearchHistoryTable> listSearchHistory) {
        //设置历史搜索
        binding.dlSearch.removeAllViews();
        for (int i = 0; i < listSearchHistory.size(); i++) {
            TextView textView = (TextView) inflater.inflate(R.layout.act_acc_search_item, binding.dlSearch, false);
            textView.setTag(i);
            textView.setText(listSearchHistory.get(i).getSearchKey());
            textView.setOnClickListener(v -> presenter.doSearchHistoryPosition((Integer) v.getTag()));
            binding.dlSearch.addView(textView);
        }
    }


}
