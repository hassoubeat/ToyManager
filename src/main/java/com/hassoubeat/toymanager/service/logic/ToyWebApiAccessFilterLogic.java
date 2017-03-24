/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.ToyWebapiAccessFilterFacade;
import com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class ToyWebApiAccessFilterLogic{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    ToyWebapiAccessFilterFacade toyWebapiAccessFilterFacade;

    public ToyWebApiAccessFilterLogic() {
    }
    
    /**
     * アクセスフィルターを承認するメソッド
     * @param toyWebApiAccessFilter 承認するアクセスフィルター
     * @return 承認されたアクセスフィルターエンティティ
     */
    public ToyWebapiAccessFilter approval(ToyWebapiAccessFilter toyWebApiAccessFilter) {
        toyWebApiAccessFilter.setIsAuthenticated(true);
        ToyWebapiAccessFilter approvalAccessFilter = toyWebapiAccessFilterFacade.edit(toyWebApiAccessFilter);
        logger.warn("{}:{}, USER_ID:{}, TOY_ID:{}, ACCESS_FILTER_ID:{} {}.{}", MessageConst.SUCCESS_ACCESS_FILTER_APPROVAL.getId(), MessageConst.SUCCESS_ACCESS_FILTER_APPROVAL.getMessage(), sessionBean.getId(), sessionBean.getSelectedToyId(), toyWebApiAccessFilter.getId(), this.getClass().getName(), this.getClass());
        return approvalAccessFilter;
    }
    
    /**
     * アクセスフィルターを拒否するメソッド
     * @param toyWebApiAccessFilter 拒否するアクセスフィルター
     * @return 拒否されたアクセスフィルターエンティティ
     */
    public ToyWebapiAccessFilter reject(ToyWebapiAccessFilter toyWebApiAccessFilter) {
        toyWebApiAccessFilter.setIsAuthenticated(true);
        ToyWebapiAccessFilter rejectAccessFilter = toyWebapiAccessFilterFacade.remove(toyWebApiAccessFilter);
        logger.warn("{}:{}, USER_ID:{}, TOY_ID:{}, ACCESS_FILTER_ID:{} {}.{}", MessageConst.SUCCESS_ACCESS_FILTER_REJECT.getId(), MessageConst.SUCCESS_ACCESS_FILTER_REJECT.getMessage(), sessionBean.getId(), sessionBean.getSelectedToyId(), toyWebApiAccessFilter.getId(), this.getClass().getName(), this.getClass());
        return rejectAccessFilter;
    }
}
