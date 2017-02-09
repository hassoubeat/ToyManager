/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "toyMenuBean")
@RequestScoped
public class ToyMenuBean {
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @Getter
    @Setter
    private List<Toy> toyList;

    /**
     * Creates a new instance of ToyMenuBean
     */
    public ToyMenuBean() {
    }
    
    @PostConstruct
    public void init() {
        // ログイン中のユーザに紐づくToyを取得する
        Account targetAccount = accountFacade.findByUserId(sessionBean.getUserId());
        setToyList(targetAccount.getToyList());
    }
    
    /**
     * Toyを選択する際に実行されるメソッド
     * @param selectedToyId 選択したToyのID
     * @return 
     */
    public String selectToy(int selectedToyId){
        // セッションに選択したToyIDを格納する
        sessionBean.setSelectedToyId(selectedToyId);
        
        logger.info("{}:{} USER_ID:{}, SELECT_TOY_ID:{}, {}.{}", MessageConst.SELECT_TOY.getId(), MessageConst.SELECT_TOY.getMessage(), sessionBean.getUserId(), selectedToyId, this.getClass().getName(), this.getClass());
        
        // 元の画面にリダイレクトして戻る(各ページのbookmarkableを動作させるためにリダイレクトする)
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getPathInfo() + "?faces-redirect=true";
    }
    
}
