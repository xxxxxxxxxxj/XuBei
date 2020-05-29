package com.xubei.shop.ui.module.account;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.KeyboardUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.databinding.ActAccListBinding;
import com.xubei.shop.databinding.ActAccListSelectLayoutBinding;
import com.xubei.shop.databinding.PopupListLayoutBinding;
import com.xubei.shop.databinding.PopupServerListLayoutBinding;
import com.xubei.shop.ui.module.account.adapter.AccAdapter;
import com.xubei.shop.ui.module.account.adapter.PopupAreaAdapter;
import com.xubei.shop.ui.module.account.adapter.PopupListAdapter;
import com.xubei.shop.ui.module.account.adapter.PopupServerAdapter;
import com.xubei.shop.ui.module.account.contract.AccListContract;
import com.xubei.shop.ui.module.account.model.AccBean;
import com.xubei.shop.ui.module.account.model.GameAllAreaBean;
import com.xubei.shop.ui.module.account.model.GameSearchRelationBean;
import com.xubei.shop.ui.module.account.presenter.AccListPresenter;
import com.xubei.shop.ui.module.base.ABaseActivity;
import com.xubei.shop.ui.views.DynamicLayout;
import com.xubei.shop.ui.views.FixNPopWindow;
import com.xubei.shop.ui.views.NoDataView;
import com.xubei.shop.utlis.LinearLayoutManager2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * 账号列表
 * date：2017/11/30 11:47
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.ACC_LIST)
public class AccListActivity extends ABaseActivity<AccListContract.Presenter> implements AccListContract.View {

    private ActAccListBinding binding;

    private ActAccListSelectLayoutBinding selectLayoutBinding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_acc_list);
        binding.listTitle.setActivity(this);
    }

    @Inject
    AccListPresenter presenter;

    @Override
    protected AccListPresenter getMVPPresenter() {
        return presenter;
    }


    private AccAdapter adapter;


    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        presenter.start();
    }

    public void initLayout(List<AccBean> list, NoDataView noDataView, boolean isFree) {
        adapter = new AccAdapter(list, isFree);
        adapter.setEmptyView(noDataView);
        //关闭手势滑动
        binding.drawrlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        selectLayoutBinding = binding.otherSelect;
        selectLayoutBinding.setActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            selectLayoutBinding.appbar.setOutlineProvider(null);
        }
        binding.drawrlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //移除软键盘
                KeyboardUtils.hideSoftInput(AccListActivity.this);
            }
        });
        binding.itemList.rv.setLayoutManager(new LinearLayoutManager2(this));
        binding.itemList.rv.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> presenter.onItemClick(position));
        binding.itemList.srl.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                presenter.nextPage();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                presenter.doRefresh();
            }
        });
    }

    private LayoutInflater inflater;

    private List<GameSearchRelationBean> tempRelationList;

    @Override
    public void doUpdataMoreView(List<GameSearchRelationBean> relationList) {
        tempRelationList = relationList;
        if (inflater == null) {
            inflater = LayoutInflater.from(this);
        }
        selectLayoutBinding.llRoot.removeAllViews();
        //动态显示布局
        for (int i = 0; i < relationList.size(); i++) {
            GameSearchRelationBean bean = relationList.get(i);
            //去掉多选的的筛选
            if (bean.isCheckbox == 0) {
                //标题
                View titleView = inflater.inflate(R.layout.act_acc_list_select_title, selectLayoutBinding.llRoot, false);
                TextView tvTitle = titleView.findViewById(R.id.tv_title);
                tvTitle.setText(bean.showName);
                selectLayoutBinding.llRoot.addView(titleView);
                //设置对应的选项
                View valuesView = inflater.inflate(R.layout.act_acc_list_select_values, selectLayoutBinding.llRoot, false);
                for (GameSearchRelationBean.OgssBean ogssBean : bean.ogss) {
                    //如果是范围值并且需要输入（范围布局）
                    if (ogssBean.valType == 2 && ogssBean.isEnter == 1) {
                        View rangeView = inflater.inflate(R.layout.act_acc_list_select_range, selectLayoutBinding.llRoot, false);
                        EditText etLow = rangeView.findViewById(R.id.et_low);
                        EditText etHigh = rangeView.findViewById(R.id.et_high);
                        etLow.setTag(bean.paramName);
                        etHigh.setTag(bean.paramName);
                        if (bean.getSelectValue() != null) {
                            GameSearchRelationBean.OgssBean selectOgssBean = bean.getSelectValue();
                            String[] tempSelectStr = selectOgssBean.val.split("-");
                            etLow.setText(tempSelectStr.length > 0 ? tempSelectStr[0] : "");
                            etHigh.setText(tempSelectStr.length > 1 ? tempSelectStr[1] : "");
                        }
                        selectLayoutBinding.llRoot.addView(rangeView);
                    } else {
                        //选项值
                        DynamicLayout dlValues = valuesView.findViewById(R.id.dl_values);
                        View valuesItemView = inflater.inflate(R.layout.act_acc_list_select_values_item, dlValues, false);
                        TextView valueItem = valuesItemView.findViewById(R.id.tv_value_item);
                        valueItem.setText(ogssBean.keyName);
                        //判断是否选中(字符串类型的单选)
                        if (bean.getSelectValue() != null && ogssBean.valType == 1) {
                            GameSearchRelationBean.OgssBean selectOgssBean = bean.getSelectValue();
                            if (selectOgssBean.keyName.equals(ogssBean.keyName)) {
                                valueItem.setTextColor(0xffffffff);
                                valueItem.setBackgroundResource(R.drawable.act_acc_sx_select_item_bg);
                            } else {
                                valueItem.setTextColor(0xff666666);
                                valueItem.setBackgroundResource(R.drawable.act_acc_sx_item_bg);
                            }
                        }

                        int finalI = i;
                        valueItem.setOnClickListener(v -> {
                            //设置当前选择的数据和选项
                            switch (ogssBean.valType) {
                                case 1://选中效果
                                    //如果之前是同一个选项，则反选，清空保存的
                                    if (bean.getSelectValue() != null && bean.getSelectValue().keyName.equals(ogssBean.keyName)) {
                                        relationList.get(finalI).setSelectValue(null);
                                    } else {
                                        relationList.get(finalI).setSelectValue(ogssBean);
                                    }
                                    break;
                                case 2://回填范围值
                                    relationList.get(finalI).setSelectValue(ogssBean);
                                    break;
                            }
                            //刷新界面
                            presenter.onUpdataSelectView();
                        });
                        dlValues.addView(valueItem);
                    }
                }
                selectLayoutBinding.llRoot.addView(valuesView);
            }
        }
        binding.drawrlayout.openDrawer(GravityCompat.END);
    }

    @Override
    public SmartRefreshLayout getSrl() {
        return binding.itemList.srl;
    }


    @Override
    public void setTitle(String name) {
        binding.appbar.toolbar.setTitle(name);
    }

    @Override
    public void onAutoRefresh() {
        //设置为第一页
        binding.itemList.rv.scrollToPosition(0);
        binding.itemList.srl.autoRefresh();
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        if (positionStart == 0 && itemCount == 0) {
            adapter.notifyDataSetChanged();
        } else {
            adapter.notifyItemRangeChanged(positionStart, itemCount);
        }
    }

    //设置筛选平台
    @Override
    public void setFilterPlatform(String platform) {
        binding.listTitle.tvFilterPlatform.setText(platform);
    }

    //平台和排序用适配器
    @Inject
    PopupListAdapter popAdapter;

    //根据数据源和默认的选项显示选择框(系统选择，和排序选择)
    @Override
    public void onShowPopFilter(List<String> list, String selectItem, final int type) {
        //设置标题栏
        PopupListLayoutBinding popBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_list_layout, null, false);
        final PopupWindow popupWindow = new PopupWindow(popBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(() -> binding.vLayout.setVisibility(View.GONE));
        popBinding.recyclerview.setLayoutManager(new LinearLayoutManager2(this));
        popBinding.recyclerview.setAdapter(popAdapter);
        popAdapter.setOnItemClickListener((adapter, view, position) -> {
            //筛选点击。
            switch (type) {
                case 1://平台选择
                    presenter.doPlatformSelect(position);
                    break;
                case 2://排序选择
                    presenter.doSortSelect(position);
                    break;
            }
            popupWindow.dismiss();
        });
        popAdapter.replaceData(list);
        popAdapter.setSelectStr(selectItem);
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(binding.viewLine);
            binding.vLayout.setVisibility(View.VISIBLE);
        }
    }


    //平台和排序用适配器
    @Inject
    PopupAreaAdapter popAreaAdapter;
    @Inject
    PopupServerAdapter popServerAdapter;

    //服务器筛选
    private FixNPopWindow popupWindow;

    @Override
    public void showAreaPop(List<GameAllAreaBean> children, final int p) {
        //显示服务器筛选pop
        PopupServerListLayoutBinding serverBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_server_list_layout, null, false);
        if (popupWindow == null) {
            popupWindow = new FixNPopWindow(serverBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        popupWindow.setOnDismissListener(() -> binding.vLayout.setVisibility(View.GONE));
        serverBinding.rvArea.setLayoutManager(new LinearLayoutManager2(this));
        serverBinding.rvServer.setLayoutManager(new GridLayoutManager(this, 2));
        serverBinding.rvArea.setAdapter(popAreaAdapter);
        serverBinding.rvServer.setAdapter(popServerAdapter);
        popAreaAdapter.setOnItemClickListener((adapter, view, position) ->
                //大区选择
                presenter.doFilterArea(position));
        popServerAdapter.setOnItemClickListener((adapter, view, position) ->
                presenter.doFilterServer((GameAllAreaBean) adapter.getData().get(position)));
        //1级数据
        popAreaAdapter.replaceData(children);
        popAreaAdapter.onSelectPosition(p);
        //2级数据
        if (children.get(p).children == null || children.get(p).children.size() == 0) {
            if (children.get(p).children == null) {
                children.get(p).children = new ArrayList<>();
            }
            GameAllAreaBean gameAllAreaBean = new GameAllAreaBean();
            gameAllAreaBean.gameName = "全部";
            children.get(p).children.add(gameAllAreaBean);
        }
        popServerAdapter.replaceData(children.get(p).children);
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(binding.viewLine);
            binding.vLayout.setVisibility(View.VISIBLE);
        }
    }

    //关闭区域pop
    @Override
    public void onCloseAreaPop() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    //关闭其它筛选
    @Override
    public void onCloseOtherFilter() {
        binding.drawrlayout.closeDrawer(GravityCompat.END);
        KeyboardUtils.hideSoftInput(this);
    }


    //标题点击
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_filter_platform://平台筛选
                presenter.doFilterPlatform();
                break;
            case R.id.ll_filter_area://服务器筛选
                presenter.doFilterArea(0);
                break;
            case R.id.ll_sort://选择排序
                presenter.doSelectedSort();
                break;
            case R.id.ll_filter://筛选
                presenter.onMoreFilter();
                break;
        }
    }

    //筛选点击
    public void onOtherClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cz://其它筛选重置
                presenter.onResetSelect();
                break;
            case R.id.tv_ok://其它筛选确认
                //获取填写的范围值
                values.clear();
                getConfigValues(selectLayoutBinding.llRoot);
                presenter.doFilterOther(values);
                break;
        }
    }

    //获取的输入的值
    private HashMap<String, String> values = new HashMap<>();

    //获取所有tag输入框的值
    private void getConfigValues(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ViewGroup) {
                getConfigValues((ViewGroup) view);
            } else if ((view instanceof EditText) && view.getTag() != null) {
                String key = (String) view.getTag();
                String value = values.get(key);
                if (value == null) {
                    value = ((EditText) view).getText().toString();
                } else {
                    //如果有值则
                    switch (view.getId()) {
                        case R.id.et_low:
                            //获取当前key的值
                            value = ((EditText) view).getText().toString() + "-" + value;
                            break;
                        case R.id.et_high:
                            //获取当前key的值
                            value = value + "-" + ((EditText) view).getText().toString();
                            break;
                    }
                }
                values.put(key, value);
            }
        }
    }


    @Override
    protected void onDestroy() {
        onCloseAreaPop();
        super.onDestroy();
    }
}
