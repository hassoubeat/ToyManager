/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.ColorTypeFacade;
import com.hassoubeat.toymanager.service.dao.EventFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.ColorType;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "eventEditBean")
@ViewScoped
public class EventEditBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @Inject
    @Getter
    EventRoopParamConst erpConst;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    EventFacade eventFacade;
    
    @EJB
    ColorTypeFacade colorTypeFacade;
    
    @NotNull
    @Size(min=1, max=50)
    @Getter
    @Setter
    private String eventName;
    
    @Size(min=0, max=200)
    @Getter
    @Setter
    private String eventContent;
    
    @NotNull
    @Getter
    @Setter
    private Date eventStartDate;
    
    @Getter
    @Setter
    private Date eventEndDate;
    
    @Getter
    @Setter
    private String eventColorCode;
    
    @Getter
    @Setter
    private int roop = 0;
    
    @Getter
    @Setter
    private Date roopEndDate;
    
    @NotNull
    @Getter
    @Setter
    private boolean isTalking = false;
    
    @NotNull
    @Getter
    @Setter
    private boolean isAccountShare = false;
    
    // カラーコードの一覧
    @Getter
    private List<ColorType> colorTypeList;
    
    /**
     * Creates a new instance of EventEditBean
     */
    public EventEditBean() {
    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable() {
        if(sessionBean.getSelectedToyId() == 0) {
            // TODO Toyが未選択であった場合(Toyが未選択エラー)
        }
        System.out.println("eventStartDate" + this.eventStartDate);
        System.out.println("eventEndDate" + this.eventEndDate);
    }
    
    
}
