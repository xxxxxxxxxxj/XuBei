package com.haohao.xubei.ui.module.base;

import com.haohao.xubei.data.network.exception.ServerErrorCode;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * 网络订阅者父类通用操作
 * date：2017/3/15 14:34
 * author：Seraph
 **/
public abstract class ABaseSubscriber<T> implements Subscriber<T> {

    private IBaseContract.IBaseView mView;

    private ServerErrorCode errorCode;

    protected ABaseSubscriber(IBaseContract.IBaseView view) {
        this.mView = view;
        errorCode = new ServerErrorCode();
    }

    public ABaseSubscriber() {
        this(null);
    }

    @Override
    public void onSubscribe(Subscription s) {
        s.request(1);
    }

    @Override
    public void onError(Throwable t) {
        onError(errorCode.errorCodeToMessageShow(t));
        if (mView != null)
            mView.hideLoading();
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    protected abstract void onSuccess(T t);

    protected abstract void onError(String errStr);



    @Override
    public void onComplete() {
        if (mView != null)
            mView.hideLoading();
    }

}
