package com.haohao.xubei.ui.module.account.contract;

import com.haohao.xubei.ui.module.account.model.GameBean;
import com.haohao.xubei.ui.module.base.IABaseContract;
import com.haohao.xubei.ui.module.main.model.GameTypeBean;

import java.util.List;

/**
 * 游戏分类
 * date：2017/2/21 17:08
 * author：Seraph
 **/
public interface AccLeaseAllTypeContract extends IABaseContract {

    interface View extends IBaseView {

        void setUITypeDate(List<GameTypeBean> typeList,int type);

        void setNoData(int type);

        void initLayout(List<String> letterList, List<GameBean> gameList);

        void updateView(int selectLetterPosition);

    }

    abstract class Presenter extends ABasePresenter<View> {

    }


}
