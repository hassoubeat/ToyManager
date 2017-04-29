/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.session;

import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.util.RoleLogic;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hassoubeat
 */
@Named(value = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {
    
    @EJB
    RoleLogic roleLogic;
    @EJB
    ToyFacade toyFacade;
    
    @Getter
    Integer id = null;
    
    @Getter
    String userId = null;
    
    @Getter
    String role = null;
    
    @Getter
    int roleAuthority = 0;
    
    @Getter
    Boolean isAuth = false;
    
    @Getter
    @Setter
    Integer selectedToyId = 0;
    
    @Getter
    @Setter
    Integer selectedToyRotNum = 0;
    
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
        roleAuthority = account.getRoleId().getAuthority();
        isAuth = true;
        selectedToyId = account.getLastSelectedToyId();
        if (isToySelected()) {
            // Toyが存在する場合
            selectedToyRotNum = toyFacade.find(selectedToyId).getRotNum();
        }
        
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
        roleAuthority = 0;
        isAuth = false;
        selectedToyId = 0;
    }
    
    /**
     * Toyが選択済であるかをチェックする
     * @return 
     */
    public boolean isToySelected() {
        if (selectedToyId != null) {
            return selectedToyId > 0;
        }
        return false;
    }
    
    /**
     * ログイン中のアカウントがManager権限以上であるかをチェックする
     * @return 
     */
    public boolean isManagerRole() {
        return roleLogic.isManagerRole(roleAuthority);
    }
    
    /**
     * ログイン中のアカウントがAdmin権限以上であるかをチェックする
     * @return 
     */
    public boolean isAdminRole() {
        return roleLogic.isManagerRole(roleAuthority);
    }
    
    @Override
    public String toString() {
        return "id=" + id + ",userId=" + userId + ",role=" + role + ",roleAuthority=" + roleAuthority + ",isAuth=" + isAuth + ",selectedToyId=" + selectedToyId;
    }
}
