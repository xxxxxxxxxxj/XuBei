package com.haohao.xubei.ui.module.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwangjr.rxbus.RxBus;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.haohao.xubei.R;
import com.haohao.xubei.ui.views.CustomLoadingDialog;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import dagger.android.support.AndroidSupportInjection;

/**
 * 基础的Fragment继承父类
 * 说明参见{@link ABaseActivity}
 * date：2017/2/20 16:40
 * author：Seraph
 *
 **/
public abstract class ABaseFragment<P extends IABaseContract.ABasePresenter> extends Fragment implements IABaseContract.IBaseView {

    protected abstract View initDataBinding(LayoutInflater inflater, ViewGroup container);

    protected abstract P getMVPPresenter();

    protected abstract void initCreate(@Nullable Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Inject
    CustomLoadingDialog mLoadingDialog;

    private P mPresenter;

    //自动解绑rxJava（在指定的生命周期）
    public <T> AutoDisposeConverter<T> bindLifecycle(Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, untilEvent));
    }
    //自动解绑rxJava（在结束的时候）
    public <T> AutoDisposeConverter<T> bindLifecycle() {
        return bindLifecycle(Lifecycle.Event.ON_DESTROY);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = initDataBinding(inflater, container);
        View appbar = rootView.findViewById(R.id.appbar);
        if (appbar != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appbar.setOutlineProvider(null);
        }
        //数据绑定
        RxBus.get().register(this);
        initMVP();
        return rootView;
    }

    @SuppressWarnings("unchecked")
    private void initMVP() {
        try {
            if (getMVPPresenter() == null) {
                return;
            }
            mPresenter = getMVPPresenter();
            mPresenter.setView(this);
        } catch (ClassCastException e) {
            throw new RuntimeException("子类必须实现IABaseContract.IBaseView接口");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCreate(savedInstanceState);
    }

    @Override
    public CustomLoadingDialog showLoading() {
        return showLoading("");
    }

    @Override
    public CustomLoadingDialog showLoading(String str) {
        mLoadingDialog.setDialogMessage(str);
        return mLoadingDialog;
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        RxBus.get().unregister(this);
        super.onDestroyView();
        //回收之后在快速的切换会发生空指针异常
        if (mPresenter != null) {
            mPresenter.onDestroy();
        }
    }

}
