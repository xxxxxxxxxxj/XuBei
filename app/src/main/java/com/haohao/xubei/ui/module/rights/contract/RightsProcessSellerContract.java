package com.haohao.xubei.ui.module.rights.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.rights.model.LeaseeArbBean;

import java.util.List;

/**
 * 维权处理
 * date：2017/12/13 09:55
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface RightsProcessSellerContract extends IABaseContract{

    interface View extends IBaseView{

        void setLeaseeArbBean(LeaseeArbBean mLeaseeArbBean);

        void setImageList(List<String> imageList);

    }

    abstract class Presenter extends ABasePresenter<View>{


    }

}
