/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;


import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.exception.FailedSendMailException;
import com.hassoubeat.toymanager.service.exception.LogicalDeletedException;
import com.hassoubeat.toymanager.service.exception.LoginException;
import com.hassoubeat.toymanager.service.exception.RemindPasswordException;
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
        try {
            accountLogic.login(this.getUserId(), this.getPassword());
        } catch (LoginException ex) {
            // ID/パスワード入力を誤った場合
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("login-form:user-id", new FacesMessage(MessageConst.INVALID_ID_OR_PASSWORD));
            facesContext.addMessage("login-form:password", new FacesMessage(MessageConst.INVALID_ID_OR_PASSWORD));
            
            // 元の画面に戻る
            return "";
            
        } catch (LogicalDeletedException ex) {
            // ログインしようとしたユーザが論理削除済であった場合
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("login-form:user-id", new FacesMessage(MessageConst.LOGICAL_DELETE_USER));
            
            // 元の画面に戻る
            return "";
        }
        
        return "/auth/index?faces-redirect=true";
    }
    
    /**
     * 
     * @return 
     */
    @ErrorInterceptor
    @LogInterceptor
    public String logout() {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
//        
//        try {
//            request.logout();
//        } catch(Exception ex) {
//            // エラーでもログイン画面に遷移する
//        }
        
        return "/login?faces-redirect=true";
    }
    
    /**
     * パスワードのリマインド処理を行う
     * @return 
     * @throws com.hassoubeat.toymanager.service.exception.FailedSendMailException
     */
    @ErrorInterceptor
    @LogInterceptor
    public String remind() throws FailedSendMailException {
        try {
            accountLogic.remind(this.getRemindUserId());
        } catch (RemindPasswordException ex) {
            // 画面に戻ってエラーメッセージの出力
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("remind-failed", new FacesMessage(MessageConst.FAILED_SEND_REMIND_PASSWORD_MAIL));
            
            // 元の画面に戻る
            return "";
        }
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("remind-complete", new FacesMessage(MessageConst.SUCCESS_RIMIND_PASSWORD));
        
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
