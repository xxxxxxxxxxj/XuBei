package com.haohao.xubei.ui.module.rights;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.haohao.xubei.AppConstants;
import com.haohao.xubei.R;
import com.haohao.xubei.databinding.ActRightsDetailSellerBinding;
import com.haohao.xubei.ui.module.base.ABaseActivity;
import com.haohao.xubei.ui.module.rights.adapter.RightsAdapter;
import com.haohao.xubei.ui.module.rights.contract.RightsDetailSellerContract;
import com.haohao.xubei.ui.module.rights.model.LeaseeArbBean;
import com.haohao.xubei.ui.module.rights.presenter.RightsDetailSellerPresenter;
import com.haohao.xubei.utlis.LinearLayoutManager2;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

/**
 * 卖家维权详情(查看维权记录)
 * date：2017/12/13 09:54
 * author：Seraph
 * mail：417753393@qq.com
 **/
@Route(path = AppConstants.PagePath.RIGHTS_DETAIL)
public class RightsDetailSellerActivity extends ABaseActivity<RightsDetailSellerContract.Presenter> implements RightsDetailSellerContract.View {

    private ActRightsDetailSellerBinding binding;

    @Override
    protected void initContextView() {
        binding = DataBindingUtil.setContentView(this, R.layout.act_rights_detail_seller);
    }

    @Inject
    RightsDetailSellerPresenter presenter;

    @Override
    protected RightsDetailSellerContract.Presenter getMVPPresenter() {
        return presenter;
    }

    @Inject
    LinearLayoutManager2 manager;

    @Inject
    RightsAdapter adapter;

    @Override
    public void initCreate(@Nullable Bundle savedInstanceState) {
        binding.appbar.toolbar.setTitle("查看维权记录");
        binding.recyclerview.setLayoutManager(manager);
        binding.ndv.setOnClickListener(v -> presenter.start());
        presenter.start();
    }


    //设置维权详情信息
    @Override
    public void setLeaseeArbBean(LeaseeArbBean leaseeArbBean) {
        if (leaseeArbBean.outOrderLeaseRightList == null || leaseeArbBean.outOrderLeaseRightList.size() == 0) {
            ToastUtils.showShort("暂无查询到维权信息");
            finish();
            return;
        }
        //最近一比维权的状态进度
        if (leaseeArbBean.outOrderLeaseRightList.get(0).rightStatus == 0) {
            binding.rapv.setProgress(2);
        } else {
            binding.rapv.setProgress(3);
        }
        //设置维权列表
        binding.recyclerview.setAdapter(adapter);
        adapter.replaceData(leaseeArbBean.outOrderLeaseRightList);

    }

    @Override
    public void setNoDataView(int type) {
        binding.ndv.setType(type);
    }

}