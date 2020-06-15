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
import com.haohao.xubei.databinding.ActAccSearchBinding;
import com.haohao.xubei.ui.module.account.contract.AccSearchContract;
import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.account.presenter.AccSearchPresenter;
import com.haohao.xubei.ui.module.base.ABaseActivity;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 账号搜索
 * date：2017/11/28 11:13
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ACC_SEARCH)
public class AccSearchActivity extends ABaseActivity<AccSearchContract.Presenter> implements AccSearchContract.View {

    private ActAccSearchBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_search);
    }

    @Inject
    AccSearchPresenter presenter;

    @Override
    protected AccSearchContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LayoutInflater inflater;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.ivDelete.setOnClickListener(v -> presenter.cleanSearchHistory());
        //点击输入法右下方搜索
        RxTextView.editorActionEvents(binding.appbar.etSearch).as(bindLifecycle()).subscribe(textViewEditorActionEvent -> {
            if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEARCH) {
                startSearch();
            }
        });
        binding.appbar.ivBack.setOnClickListener(v -> onBackPressed());
        binding.appbar.tvSearch.setOnClickListener(v ->
        {
            presenter.doSearch(binding.appbar.etSearch.getText().toString().trim());
            KeyboardUtils.hideSoftInput(this);
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
        ARouter.getInstance().build(AppConstants.PagePath.ACC_SRESULT).withString("key", searchKey).navigation();
    }

    @Override
    public void setSearchList(List<GameBean> gameBeans) {
        //热门搜索
        binding.dlHotSearch.removeAllViews();
        for (int i = 0; i < gameBeans.size(); i++) {
            TextView textView = (TextView) inflater.inflate(R.layout.act_acc_search_item, null, false);
            textView.setTag(i);
            textView.setText(gameBeans.get(i).getGameName());
            textView.setBackgroundResource(R.drawable.act_acc_search_item_hot_bg);
            textView.setTextColor(0xffff9900);
            textView.setOnClickListener(v -> presenter.doSearchPosition((Integer) v.getTag()));
            binding.dlHotSearch.addView(textView);
        }
    }

    @Override
    public void setSearchHistoryList(List<SearchHistoryTable> listSearchHistory) {
        //设置历史搜索
        binding.dlSearch.removeAllViews();
        for (int i = 0; i < listSearchHistory.size(); i++) {
            TextView textView = (TextView) inflater.inflate(R.layout.act_acc_search_item, null, false);
            textView.setTag(i);
            textView.setText(listSearchHistory.get(i).getSearchKey());
            textView.setOnClickListener(v -> presenter.doSearchHistoryPosition((Integer) v.getTag()));
            binding.dlSearch.addView(textView);
        }
    }


}
