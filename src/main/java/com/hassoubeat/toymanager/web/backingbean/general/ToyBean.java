/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.logic.ToyLogic;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
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
@Named(value = "toyBean")
@ViewScoped
public class ToyBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    ToyLogic toyLogic;
    
    @NotNull
    @Getter
    @Setter
    private Integer toyRotNumber;
    
    @NotNull
    @Size(min=1, max=256)
    @Getter
    @Setter
    private String toyPassword;
    
    @NotNull
    @Size(min=1, max=50)
    @Getter
    @Setter
    private String toyName;
    
    /**
     * Creates a new instance of ToyMenuBean
     */
    
    public ToyBean() {
    }
    
    /**
     * 現在ログイン中のアカウントにToyを紐付けするメソッド
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void accountTying() {
        // ロットナンバー/パスワードの組み合わせのチェック
        if(toyFacade.countByRotNumberAndPassword(this.getToyRotNumber(), this.getToyPassword()) == 0) {
            // 対象のカラムが存在しない場合
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("toy-create-form:rot-number", new FacesMessage(MessageConst.INVALID_ROT_NUMBER_OR_PASSWORD.getMessage()));
            facesContext.addMessage("toy-create-form:toy-password", new FacesMessage(MessageConst.INVALID_ROT_NUMBER_OR_PASSWORD.getMessage()));
            
            logger.warn("{}:{}, USER_ID:{}, ROT_NUMBER:{}, PASSWORD,{} {}", MessageConst.INVALID_ROT_NUMBER_OR_PASSWORD.getId(), MessageConst.INVALID_ROT_NUMBER_OR_PASSWORD.getMessage(), sessionBean.getUserId(), this.getToyRotNumber(), this.getToyPassword(), this.getClass().getName() + "." + this.getClass());
            
            // 元の画面に戻る
            return;
        }
        
        Toy targetToy = toyFacade.findByRotNumber(this.getToyRotNumber());
        
        // 他のアカウントで既に紐付いていないかのチェック
        if(targetToy.getAccountId() != null) {
            // 既に他のアカウントに紐付いていた場合
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("toy-create-form:rot-number", new FacesMessage(MessageConst.ALREADY_TYING_ROT_NUMBER.getMessage()));
            
            
            
            logger.warn("{}:{}, USER_ID:{}, ROT_NUMBER:{}, {}", MessageConst.ALREADY_TYING_ROT_NUMBER.getId(), MessageConst.ALREADY_TYING_ROT_NUMBER.getMessage(), sessionBean.getUserId(), this.getToyRotNumber(), this.getClass().getName() + "." + this.getClass());
            
            // 元の画面に戻る
            return;
        }
        
        targetToy.setName(this.getToyName());
        targetToy.setAccountId(accountFacade.find(sessionBean.getId()));
        toyLogic.accountTying(targetToy);
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("toy-account-tying-success", new FacesMessage(MessageConst.SUCCESS_TOY_ACCOUNT_TYING.getMessage()));
        
    }
    
}
