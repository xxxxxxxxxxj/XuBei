package com.haohao.xubei.ui.module.order;

import android.os.Bundle;
import android.view.Menu;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActOrderAllBinding;
import com.haohao.xubei.di.QualifierType;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.order.adapter.OrderVPAdapter;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 全部订单
 * date：2017/12/4 15:09
 * author：Seraph
 **/
@Route(path = AppConstants.PagePath.ORDER_ALL,extras = AppConstants.ARAction.LOGIN)
public class OrderAllActivity extends ABaseActivity<IABaseContract.ABasePresenter> {


    private ActOrderAllBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_order_all);
    }

    @Override
    protected IABaseContract.ABasePresenter getMVPPresenter() {
        return null;
    }

    @Inject
    OrderVPAdapter vpAdapter;

//    @Inject
//    TimeSelectAdapter timeSelectAdapter;

    //选择时间列表
//    @Inject
//    List<TimeSelectBean> selectTimeList;

    @QualifierType("type")
    @Inject
    Integer type;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("我的租号订单");
        initLayout();
        RxToolbar.itemClicks(binding.appbar.toolbar)
                .as(bindLifecycle())
                .subscribe(menuItem -> {
            switch (menuItem.getItemId()) {
//                case R.id.action_time:
//                    binding.drawrlayout.openDrawer(GravityCompat.END);
//                    break;
                case R.id.action_search:
                    ARouter.getInstance().build(AppConstants.PagePath.ORDER_SEARCH).navigation();
                    break;
            }
        });
    }


    private void initLayout() {
//        binding.drawrlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            binding.timeSelect.appbar.setOutlineProvider(null);
//        }
//        binding.timeSelect.recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
//        binding.timeSelect.recyclerview.setAdapter(timeSelectAdapter);
//        timeSelectAdapter.replaceData(selectTimeList);
//        timeSelectAdapter.setOnItemClickListener((adapter, view, position) -> {
//            binding.drawrlayout.closeDrawer(GravityCompat.END);
//
//            for (int i = 0; i < selectTimeList.size(); i++) {
//                selectTimeList.get(i).isSelectd = false;
//            }
//            selectTimeList.get(position).isSelectd = true;
//            timeSelectAdapter.notifyDataSetChanged();
//            TimeSelectBean selectBean = selectTimeList.get(position);
//            //通知刷新
//            RxBus.get().post(AppConstants.RxBusAction.TAG_ORDER_TIME_SELECT, selectBean);
//        });

        binding.vpOrder.setAdapter(vpAdapter);
        binding.xtlOrder.setupWithViewPager(binding.vpOrder);
        binding.vpOrder.setCurrentItem(type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_act_time_search_icon, menu);
        return super.onCreateOptionsMenu(menu);
    }
}