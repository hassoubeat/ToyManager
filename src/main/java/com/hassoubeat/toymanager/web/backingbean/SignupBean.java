/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;


import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.util.GMailLogic;
import com.hassoubeat.toymanager.constant.MessageConst;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "signupBean")
@ViewScoped
public class SignupBean implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    GMailLogic mailLogic;
    
    @EJB
    AccountFacade accountFacade;
    
    @Size(min=1,max=256)
    @Pattern(regexp="^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$")
    @NotNull
    @Getter
    @Setter
    private String userId = "";
    
    @Size(min=1,max=500)
    @NotNull
    @Getter
    @Setter
    private String password = "";
    
    @Size(min=1,max=500)
    @NotNull
    @Getter
    @Setter
    private String checkPassword = "";
    
    /**
     * Creates a new instance of SingupBean
     */
    public SignupBean() {
    }
    
    @LogInterceptor
    @ErrorInterceptor
    public String userIdCheck() throws MessagingException, UnsupportedEncodingException{
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // 確認用パスワードが正常に入力されているかチェック
        if(!this.getPassword().equals(this.getCheckPassword())) {
            // 確認用パスワードが誤っていた場合
            facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("signup-form:check-input-password", new FacesMessage(MessageConst.INVALID_CHECK_PASSWORD.getMessage()));
            
            logger.warn("{}:{} PASSWORD:{}, CHECK_PASSWORD:{} {}.{}", MessageConst.INVALID_CHECK_PASSWORD.getId(), MessageConst.INVALID_CHECK_PASSWORD.getMessage(), this.getPassword(), this.getCheckPassword(), this.getClass().getName(), this.getClass());
            
            return "";
        }
        
        // userId(メールアドレスの重複登録チェック)
        if(accountFacade.countByUserId(this.getUserId()) >= 1) {
            // 1件以上登録されていれば重複とみなし、エラーメッセージを表示する
            facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("signup-form:user-id", new FacesMessage(MessageConst.ALREADY_REGISTED_USER.getMessage()));
            
            logger.warn("{}:{} USER_ID:{}, {}.{}", MessageConst.ALREADY_REGISTED_USER.getId(), MessageConst.ALREADY_REGISTED_USER.getMessage(), this.getUserId(), this.getClass().getName(), this.getClass());
            
            // 元の画面に戻る
            return "";
        }
        
        // 認証コード確認画面へユーザIDとパスワードを渡すためにFaceContextにセットする
        facesContext.getExternalContext().getFlash().put("userId", this.getUserId());
        facesContext.getExternalContext().getFlash().put("password", this.getPassword());
        
        
        // 認証コード確認画面へ遷移
        return "/signupCheck?faces-redirect=true";
    }

    
    
    
    
    
}
