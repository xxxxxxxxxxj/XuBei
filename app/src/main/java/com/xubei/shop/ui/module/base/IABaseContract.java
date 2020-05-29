package com.xubei.shop.ui.module.base;

/**
 * p层实现类的父类
 * date：2017/12/8 09:18
 * author：Seraph
 *
 **/
public interface IABaseContract extends IBaseContract {


    abstract class ABasePresenter<V extends IBaseView> implements IBasePresenter<V> {

        protected V mView;

        @Override
        public void setView(V v) {
            mView = v;
        }

        @Override
        public void onDestroy() {
            //解除绑定
            mView = null;
        }







    }

}
