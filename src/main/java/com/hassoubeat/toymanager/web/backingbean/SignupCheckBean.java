/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;


import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.exception.FailedSendMailException;
import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import com.hassoubeat.toymanager.service.logic.AccountLogic;
import com.hassoubeat.toymanager.util.GMailLogic;
import com.hassoubeat.toymanager.util.MessageConst;
import java.io.Serializable;
import java.util.Objects;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "signupCheckBean")
@ViewScoped
public class SignupCheckBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    GMailLogic mailLogic;
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    AccountLogic accountLogic;
    
    @Getter
    private String userId = "";
    
    @Getter
    private String password = "";
    
    // UserId(メールアドレス)が実際に有効なものかをチェックするための認証コード
    @Getter
    private String authCode;
    
    @Getter
    @Setter
    private String inputAuthCode;
    
    
    /**
     * Creates a new instance of SingupBean
     */
    public SignupCheckBean() {
    }
    
    /**
     * SignupBeanでセットされたUserIdとパスワードを受け取ってセットする
     * @throws com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException
     */
    @LogInterceptor
    public void init() throws InvalidScreenTransitionException {
        FacesContext faceContext = FacesContext.getCurrentInstance();
        
        try {
            this.userId = Objects.requireNonNull((String)faceContext.getExternalContext().getFlash().get("userId"));
            this.password = Objects.requireNonNull((String)faceContext.getExternalContext().getFlash().get("password"));
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
        logger.info("{} : USER_ID:{}, AUTH_CODE:{}, {}", MessageConst.GEN_USER_AUTH_CODE_ID + ":" + MessageConst.GEN_USER_AUTH_CODE, this.getUserId(), this.getAuthCode(), this.getClass().getName() + "." + this.getClass());
        
    }
    
    @LogInterceptor
    @ErrorInterceptor
    public String authSignup() {
        if (this.getInputAuthCode().equals(this.getAuthCode())) {
            // 認証が成功した場合
            logger.info("{} : USER_ID:{}", MessageConst.SUCCESS_USER_AUTH_CODE_ID + ":" + MessageConst.SUCCESS_USER_AUTH_CODE, this.getUserId());
            
            // ユーザ登録処理の実行
            accountLogic.signup(this.getUserId(), this.getPassword());
            
            return "/finishSignup?faces-redirect=true";
            
        } else {
            // 認証が失敗した場合
            logger.warn("{} : USER_ID:{}, INPUT_AUTH_CODE:{}, {}", MessageConst.FAILED_USER_AUTH_CODE_ID + ":" + MessageConst.FAILED_USER_AUTH_CODE, this.getUserId(), this.getInputAuthCode(), this.getClass().getName() + "." + this.getClass());
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("auth-code-form:auth-code", new FacesMessage(MessageConst.FAILED_USER_AUTH_CODE));
            
            return "";
        }
        
        
    }
    
    /**
     * ブックマーカビリティ
     * @throws com.hassoubeat.toymanager.service.exception.FailedSendMailException
     * @throws com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException
     */
    @ErrorInterceptor
    @LogInterceptor
    public void bookmarkable() throws FailedSendMailException, InvalidScreenTransitionException{
        this.init();
        this.genAuthCode();
        // メールの送信
        
        try {
            mailLogic.send("hassoubeat0@gmail.com", "認証コードを送信しました", mailLogic.genAuthCodeLetterBody(this.authCode, false) , false);
        } catch (FailedSendMailException ex) {
            // 認証コードメール送信失敗時
            
            logger.error("{}:{}, USER_ID:{}, {}", MessageConst.FAILED_SEND_AUTH_CODE_MAIL_ID, MessageConst.FAILED_SEND_AUTH_CODE_MAIL, this.getUserId(), this.getClass().getName() + "." + this.getClass());
            throw new FailedSendMailException("", ex);
        }
        

    }
    
}
