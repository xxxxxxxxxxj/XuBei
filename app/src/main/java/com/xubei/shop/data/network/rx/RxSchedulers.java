package com.xubei.shop.data.network.rx;


import com.xubei.shop.data.network.exception.ServerErrorException;
import com.xubei.shop.ui.module.base.BaseDataResponse;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 进行线程切换（业务和程序逻辑的调度），
 * @see Flowable#compose(FlowableTransformer) 操作符进行使用
 * date：2017/7/24 14:21
 * author：Seraph
 **/
public class RxSchedulers {


    /**
     * 业务成功code
     */
    private static final int SUCCESS_STATUS = 1;



    /**
     * io线程转main线程，同时包含了业务逻辑的封装转换(返回对应的T对象)
     */
    public static <T> FlowableTransformer<BaseDataResponse<T>, T> io_main_business() {

        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).flatMap(tBaseDataResponse -> {
            switch (tBaseDataResponse.code) {
                case SUCCESS_STATUS: //成功
                    T t = tBaseDataResponse.result;
                    if (t == null) {
                        //约束后台，在有对象的时候不允许返回null，最好的办法是在 tBaseDataResponse.data 泛型T 如果没有返回值则返回空对象data{}
                        t = (T) "";
                    }
                    return Flowable.just(t);
                default://业务失败
                    return Flowable.error(new ServerErrorException(tBaseDataResponse.message, tBaseDataResponse.code));
            }
        });
    }


    /**
     * io线程转main线程
     */
    public static <T> FlowableTransformer<T, T> io_main() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * io线程转main线程
     */
    public static <T> ObservableTransformer<T, T> io_main2() {
        return upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


}
