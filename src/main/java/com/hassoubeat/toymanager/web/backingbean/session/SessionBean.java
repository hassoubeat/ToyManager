/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.session;

import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.entity.Account;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;

/**
 *
 * @author hassoubeat
 */
@Named(value = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {
    
    @Getter
    Integer id = null;
    
    @Getter
    String userId = null;
    
    @Getter
    String role = null;
    
    @Getter
    Boolean isAuth = false;
    
    /**
     * Creates a new instance of SessionBean
     */
    public SessionBean() {
    }
    
    /**
     * アカウント情報を保持する
     * 
     * @param account ログインアカウント情報
     */
    @LogInterceptor
    public void login(Account account) {
        // Session Fixation対策にセッションIDを変更
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        request.changeSessionId();
        
        id = account.getId();
        userId = account.getUserId();
        role = account.getRoleId().getName();
        isAuth = true;
    }
    
    /**
     * アカウント情報を破棄する
     * 
     */
    @LogInterceptor
    public void logout() {
        id = null;
        userId = null;
        role = null;
        isAuth = false;
    }
    
}
