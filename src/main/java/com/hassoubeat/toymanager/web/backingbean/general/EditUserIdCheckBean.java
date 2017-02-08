/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean.general;

import com.hassoubeat.toymanager.annotation.AuthGeneralInterceptor;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import com.hassoubeat.toymanager.service.logic.AccountLogic;
import com.hassoubeat.toymanager.util.GMailLogic;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "editUserIdCheckBean")
@ViewScoped
public class EditUserIdCheckBean implements Serializable{

    
    @Inject
    Logger logger;
    
    @Inject
    GMailLogic mailLogic;
    
    @Inject
    SessionBean sessionBean;
           
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    AccountLogic accountLogic;
    
    @Getter
    private String editUserId = "";
    
    // UserId(メールアドレス)が実際に有効なものかをチェックするための認証コード
    @Getter
    private String authCode;
    
    @Getter
    @Setter
    private String inputAuthCode;
    
    
    /**
     * Creates a new instance of EditUserIdCheckBean
     */
    public EditUserIdCheckBean() {
    }
    
    /**
     * AccountEditBeanでセットされた変更するUserIdを受け取ってセットする
     */
    @LogInterceptor
    public void init(){
        FacesContext faceContext = FacesContext.getCurrentInstance();
        
        try {
            this.editUserId = Objects.requireNonNull((String)faceContext.getExternalContext().getFlash().get("editUserId"));
        } catch (NullPointerException ex) {
            // TODO 例外設計するときに表示するメッセージを検討する
            throw new InvalidScreenTransitionException("", ex);
        }
    }
    
    /**
     * 認証コードを生成する
     */
    @LogInterceptor
    public void genAuthCode() {
        // 認証コードの生成(8桁のランダムな英数字)
        this.authCode = RandomStringUtils.randomAlphanumeric(8);

        // ログ出力
        logger.info("{} : USER_ID:{}, EDIT_USER_ID:{}, AUTH_CODE:{}, {}.{}", MessageConst.GEN_USER_AUTH_CODE.getId() + ":" + MessageConst.GEN_USER_AUTH_CODE.getMessage(), sessionBean.getUserId(), this.getEditUserId(), this.getAuthCode(), this.getClass().getName(), this.getClass());
        
    }
    
    @LogInterceptor
    @ErrorInterceptor
    public String authEditUserId() {
        if (this.getInputAuthCode().equals(this.getAuthCode())) {
            // 認証が成功した場合
            logger.info("{} : USER_ID:{}, EDIT_USER_ID:{}  {}.{}", MessageConst.SUCCESS_USER_AUTH_CODE.getId() + ":" + MessageConst.SUCCESS_USER_AUTH_CODE.getMessage(), sessionBean.getUserId(),this.getEditUserId() ,this.getClass().getName(), this.getClass());
            
            // ユーザID変更処理の実行
            accountLogic.editUserId(accountFacade.findByUserId(sessionBean.getUserId()), this.getEditUserId());
            accountLogic.logout();
            
            return "/finishEditUserId?faces-redirect=true";
            
        } else {
            // 認証が失敗した場合
            logger.warn("{} : USER_ID:{}, EDIT_USER_ID:{}, INPUT_AUTH_CODE:{}, {}.{}", MessageConst.FAILED_USER_AUTH_CODE.getId() + ":" + MessageConst.FAILED_USER_AUTH_CODE.getMessage(), sessionBean.getUserId(), this.getEditUserId(), this.getInputAuthCode(), this.getClass().getName(), this.getClass());
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("auth-code-form:auth-code", new FacesMessage(MessageConst.FAILED_USER_AUTH_CODE.getMessage()));
            
            return "";
        }
        
        
    }
    
    /**
     * ブックマーカビリティ
     */
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable(){
        this.init();
        this.genAuthCode();
        // メールの送信
        mailLogic.send("hassoubeat0@gmail.com", "ユーザID変更：認証コードを送信しました", mailLogic.genAuthCodeLetterBody(this.authCode, false) , false);
    }
    
}
