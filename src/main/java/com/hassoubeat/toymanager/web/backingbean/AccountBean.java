/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;


import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.logic.AccountLogic;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hassoubeat
 */
@Named(value = "accountBean")
@RequestScoped
public class AccountBean {

    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountLogic accountLogic;
    
    @NotNull
    @Size(min=1,max=256)
    @Setter
    @Getter
    private String userId;
    
    @NotNull
    @Size(min=1,max=500)
    @Setter
    @Getter
    private String password;
    
    @Size(min=1,max=256)
    @Pattern(regexp="^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$")
    @NotNull
    @Getter
    @Setter
    private String remindUserId;
    
    /**
     * Creates a new instance of LoginBean
     */
    public AccountBean() {
    }
    
    /**
     * 入力されたUserIdとPasswordでログイン処理を行う
     * @return 
     */
    @LogInterceptor
    @ErrorInterceptor
    public String login() {
        
        // ログイン処理の実行
        MessageConst resultCode = accountLogic.login(this.getUserId(), this.getPassword());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        switch(resultCode) {
            case LOGICAL_DELETE_USER:
                // ログインしようとしたユーザが論理削除済であった場合
                facesContext.addMessage("login-form:user-id", new FacesMessage(MessageConst.LOGICAL_DELETE_USER.getMessage()));
                break;
            
            case INVALID_USERID: 
                // ID入力を誤った場合
                facesContext.addMessage("login-form:user-id", new FacesMessage(MessageConst.INVALID_USERID_OR_PASSWORD.getMessage()));
                facesContext.addMessage("login-form:password", new FacesMessage(MessageConst.INVALID_USERID_OR_PASSWORD.getMessage()));
                break;
                
            case INVALID_PASSWORD: 
                // パスワード入力を誤った場合
                facesContext.addMessage("login-form:user-id", new FacesMessage(MessageConst.INVALID_USERID_OR_PASSWORD.getMessage()));
                facesContext.addMessage("login-form:password", new FacesMessage(MessageConst.INVALID_USERID_OR_PASSWORD.getMessage()));
                break;
                
            case SUCCESS_LOGIN:
                // ログイン正常
                return "/auth/index?faces-redirect=true";

        }
        
        // 元の画面に戻る
        return "";
    }
    
    /**
     * 
     * @return 
     */
    @ErrorInterceptor
    @LogInterceptor
    public String logout() {
        
        // TODO ログアウト実行時に失敗した際の処理いるか？
        accountLogic.logout();
        
        return "/login?faces-redirect=true";
    }
    
    /**
     * パスワードのリマインド処理を行う
     * @return 
     * @throws com.hassoubeat.toymanager.service.exception.FailedSendMailException
     */
    @ErrorInterceptor
    @LogInterceptor
    public String remind() {
        
        MessageConst resultCode = accountLogic.remind(this.getRemindUserId());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        switch(resultCode) {
            case NOT_FOUND_USER:
                // リマインド対象のユーザがいなかった場合
                
                // 画面に戻ってエラーメッセージの出力        
                facesContext.addMessage("remind-failed", new FacesMessage(MessageConst.NOT_FOUND_USER.getMessage()));
                break;

            case SUCCESS_REMIND_PASSWORD:
                // リマインド完了

                // 画面に戻ってエラーメッセージの出力        
                facesContext.addMessage("remind-complete", new FacesMessage(MessageConst.SUCCESS_REMIND_PASSWORD.getMessage()));
                break;
        }
          
        // 元の画面に戻る
        return "";   
    }
    
    @ErrorInterceptor
    @LogInterceptor
    public void bookmarkable() throws IOException {
        // セッション情報でログイン済であった場合、トップ画面へリダイレクトする
        if (sessionBean.getIsAuth()) {
            // ログイン済の場合
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/auth/index.xhtml");
        }
    }
}
