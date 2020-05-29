package com.xubei.shop.ui.module.account.presenter;

import android.view.LayoutInflater;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xubei.shop.AppConfig;
import com.xubei.shop.AppConstants;
import com.xubei.shop.R;
import com.xubei.shop.data.db.help.UserBeanHelp;
import com.xubei.shop.data.network.rx.RxSchedulers;
import com.xubei.shop.data.network.service.ApiUserNewService;
import com.xubei.shop.databinding.DialogModifyPwLayoutBinding;
import com.xubei.shop.ui.module.account.contract.AccRListSResultContract;
import com.xubei.shop.ui.module.account.model.OutGoodsBean;
import com.xubei.shop.ui.module.base.ABaseSubscriber;
import com.xubei.shop.ui.module.base.BaseData;
import com.xubei.shop.ui.views.NoDataView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

/**
 * 账号搜索结果列表
 * date：2017/11/28 11:23
 * author：Seraph
 * mail：417753393@qq.com
 **/
public class AccRListSResultPresenter extends AccRListSResultContract.Presenter {

    private ApiUserNewService apiUserNewService;

    private UserBeanHelp userBeanHelp;

    private String searchStr;

    @Inject
    AccRListSResultPresenter(UserBeanHelp userBeanHelp, ApiUserNewService apiUserNewService, String searchStr) {
        this.userBeanHelp = userBeanHelp;
        this.apiUserNewService = apiUserNewService;
        this.searchStr = searchStr;
    }

    private List<OutGoodsBean> list = new ArrayList<>();

    private int pageNo = 0;

    @Override
    public void start() {
        mView.setTitle(searchStr);
        mView.initLayout(list);
        doRefresh();
    }

    //下拉刷新
    public void doRefresh() {
        doFindGoodsOrderList(1);
    }

    //加载更多
    public void doNextPage() {
        doFindGoodsOrderList(pageNo + 1);
    }

    //商品详情
    public void doItemDetailClick(int position) {
        if (list.size() <= position) {
            ToastUtils.showShort("查看商品失败，请刷新列表后重试");
            return;
        }
        ARouter.getInstance().build(AppConstants.PagePath.ACC_R_DETAIL).withString("id",list.get(position).goodsId).navigation();
    }

    //删除
    public void doItemDeleteClick(int position) {
        if (list.size() <= position) {
            ToastUtils.showShort("修改商品状态失败，请刷新列表后重试");
            return;
        }
        OutGoodsBean outGoodsBean = list.get(position);
        new AlertDialog.Builder(mView.getContext())
                .setMessage("确认删除该账号吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", ((dialog, which) -> deleteGoods(outGoodsBean.goodsId)))
                .show();
    }

    //编辑
    public void doItemEditClick(int position) {
        if (list.size() <= position) {
            ToastUtils.showShort("修改商品状态失败，请刷新列表后重试");
            return;
        }
        OutGoodsBean outGoodsBean = list.get(position);
        ARouter.getInstance().build(AppConstants.PagePath.ACC_R_EDIT)
                .withString("id",outGoodsBean.goodsId)
                .navigation();
    }

    //修改密码
    public void doItemModifyClick(int position) {
        if (list.size() <= position) {
            ToastUtils.showShort("修改商品状态失败，请刷新列表后重试");
            return;
        }
        OutGoodsBean outGoodsBean = list.get(position);
        DialogModifyPwLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mView.getContext()), R.layout.dialog_modify_pw_layout, null, false);
        AlertDialog dialog = new AlertDialog.Builder(mView.getContext(), R.style.custom_dialog_style)
                .setView(binding.getRoot()).show();
        binding.btnOk.setOnClickListener(v -> {
            String inputText = binding.etInputPw.getText().toString().trim();
            if (ObjectUtils.isEmpty(inputText)) {
                ToastUtils.showShort("密码不能为空");
                return;
            }
            if (!inputText.equals(binding.etInputPw2.getText().toString().trim())) {
                ToastUtils.showShort("2次密码输入不一致");
                return;
            }
            dialog.dismiss();
            doChangeGoodsPw(outGoodsBean.goodsId, inputText);
        });
        binding.ivClose.setOnClickListener(v -> dialog.dismiss());
    }


    //上架
    public void doItemShelfClick(int position) {
        new AlertDialog.Builder(mView.getContext())
                .setMessage("确认上架该账号吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", ((dialog, which) -> upOrDownGoods(position, "0")))
                .show();
    }

    //下架
    public void doItemObtainedClick(int position) {
        new AlertDialog.Builder(mView.getContext())
                .setMessage("确认下架该账号吗？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确认", ((dialog, which) -> upOrDownGoods(position, "1")))
                .show();
    }


    //获取我的商品列表信息
    private void doFindGoodsOrderList(final int tempPageNo) {
        HashMap<String, Object> map = new HashMap<>();
        //类型: 0 出售 1 出租
        map.put("tradingWay", 1);
        map.put("keyWords", searchStr);
        map.put("pageIndex", tempPageNo);
        map.put("pageSize", AppConfig.PAGE_SIZE);
        apiUserNewService.getGoodsManage(userBeanHelp.getAuthorization(), map)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.setNoDataView(NoDataView.LOADING))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<BaseData<OutGoodsBean>>(mView) {
                    @Override
                    public void onSuccess(BaseData<OutGoodsBean> baseData) {
                        if (tempPageNo == 1) {
                            list.clear();
                        }
                        if (mView == null) {
                            return;
                        }
                        if (baseData.currentPage == tempPageNo || baseData.currentPage == 0) {
                            list.addAll(baseData.data);
                            mView.notifyItemRangeChanged((tempPageNo - 1) * AppConfig.PAGE_SIZE, baseData.data.size());
                        } else {
                            ToastUtils.showShort("没有更多数据");
                            mView.notifyItemRangeChanged(-1, 0);
                        }
                        if (list.size() == 0) {
                            mView.setNoDataView(NoDataView.NO_DATA);
                        } else {
                            mView.setNoDataView(NoDataView.LOADING_OK);
                        }
                        pageNo = baseData.currentPage;
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                        mView.setNoDataView(NoDataView.NET_ERR);
                    }
                });
    }

    //进行商品上下架切换
    private void upOrDownGoods(int position, String type) {
        if (list.size() <= position) {
            ToastUtils.showShort("修改商品状态失败，请刷新列表后重试");
            return;
        }
        OutGoodsBean outGoodsBean = list.get(position);
        apiUserNewService.upOrDownGoods(userBeanHelp.getAuthorization(), outGoodsBean.goodsId, type)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String re) {
                        mView.onAutoRefreshList();
                        ToastUtils.showShort("商品" + ("0".equals(type) ? "上架" : "下架") + "成功");
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //修改密码
    private void doChangeGoodsPw(String goodsId, String pwd) {
        apiUserNewService.changeGoodsPwd(userBeanHelp.getAuthorization(), goodsId, pwd)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("密码修改成功");
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });
    }

    //删除
    private void deleteGoods(String goodsId) {
        apiUserNewService.deleteGoods(userBeanHelp.getAuthorization(), goodsId)
                .compose(RxSchedulers.io_main_business())
                .doOnSubscribe(subscription -> mView.showLoading().setOnDismissListener(dialog -> subscription.cancel()))
                .as(mView.bindLifecycle())
                .subscribe(new ABaseSubscriber<String>(mView) {
                    @Override
                    public void onSuccess(String s) {
                        ToastUtils.showShort("商品删除成功");
                        //刷新界面
                        mView.onAutoRefreshList();
                    }

                    @Override
                    public void onError(String errStr) {
                        ToastUtils.showShort(errStr);
                    }
                });

    }


}
