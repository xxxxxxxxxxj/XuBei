package com.haohao.xubei.ui.module.main.contract;

import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.main.model.GameTypeBean;

import java.util.List;

/**
 * 游戏分类
 * date：2017/2/21 17:08
 * author：Seraph
 **/
public interface MainGameTypeContract extends IABaseContract {

    interface View extends IBaseView {

        void setUIDate(List<GameTypeBean> typeList, int selectPosition);

        void setNoData(int type);
    }

    abstract class Presenter extends ABasePresenter<View> {

    }


}
