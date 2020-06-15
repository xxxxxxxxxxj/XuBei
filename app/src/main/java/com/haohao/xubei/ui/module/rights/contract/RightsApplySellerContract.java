package com.haohao.xubei.ui.module.rights.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;

import java.util.List;

/**
 * 申请维权契约
 * date：2017/12/13 09:55
 * author：Seraph
 * mail：417753393@qq.com
 **/
public interface RightsApplySellerContract extends IABaseContract{

    interface View extends IBaseView{

        void setImageList(List<String> imageList);

        void setSelectType(String type);
    }

    abstract class Presenter extends ABasePresenter<View>{


    }

}
