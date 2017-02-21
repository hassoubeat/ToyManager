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
import com.hassoubeat.toymanager.service.exception.ToyManagerException;
import com.hassoubeat.toymanager.service.logic.AccountLogic;
import com.hassoubeat.toymanager.constant.MessageConst;
import static com.hassoubeat.toymanager.constant.MessageConst.INVALID_PASSWORD;
import static com.hassoubeat.toymanager.constant.MessageConst.INVALID_USERID;
import static com.hassoubeat.toymanager.constant.MessageConst.LOGICAL_DELETE_USER;
import static com.hassoubeat.toymanager.constant.MessageConst.SUCCESS_LOGIN;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
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
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Named(value = "accountEditBean")
@RequestScoped
public class AccountEditBean {
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @EJB
    AccountLogic accountLogic;
    
    @EJB
    AccountFacade accountFacade;
    
    @NotNull
    @Size(min=1, max=256)
    @Pattern(regexp="^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$")
    @Setter
    @Getter
    private String editUserId;
    
    @NotNull
    @Size(min=1, max=500)
    @Setter
    @Getter
    private String userCheckPassword;
    
    @Setter
    @Getter
    private String password;
    
    @NotNull
    @Size(min=1, max=500)
    @Getter
    @Setter
    private String editPassword;
    
    @NotNull
    @Size(min=1, max=500)
    @Getter
    @Setter
    private String editCheckPassword;
    
    /**
     * Creates a new instance of UserBean
     */
    public AccountEditBean() {
    }
    
    /**
     * ユーザIDの変更処理を実施する
     * @return 
     * @throws java.lang.Exception 
     */
    @LogInterceptor
    @ErrorInterceptor
    public String userIdEdit() throws Exception {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // 変更後メールアドレスの重複登録チェック
        if(accountFacade.countByUserId(this.getEditUserId()) >= 1) {
            // 1件以上登録されていれば重複とみなし、エラーメッセージを表示する
            facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("user-id-edit-form:edit-user-id", new FacesMessage(MessageConst.ALREADY_REGISTED_USER.getMessage()));

            logger.warn("{}:{}", MessageConst.ALREADY_REGISTED_USER.getId() + ":" + MessageConst.ALREADY_REGISTED_USER.getMessage(), this.getClass().getName() + "." + this.getClass());

            // 元の画面に戻る
            return "";
        }
        
        // sessionBeanからのユーザIDと入力したパスワードでログイン出来ることを確認
        MessageConst resultCode = accountLogic.authAccount(sessionBean.getUserId(), this.getPassword());
        
        switch(resultCode) {
            case LOGICAL_DELETE_USER:
                // 現在ログインしているユーザIDが論理削除済であった場合(正常に動作していればありえない動作のため)
                throw new ToyManagerException(MessageConst.LOGICAL_DELETE_USER.getMessage());
            
            case INVALID_USERID: 
                // 現在ログインしているユーザIDが存在しなかった場合(正常に動作していればありえない動作のため)
                throw new ToyManagerException(MessageConst.NOT_FOUND_USER.getMessage());
                
            case INVALID_PASSWORD: 
                // パスワード入力を誤った場合
                facesContext.addMessage("invalid-password", new FacesMessage(MessageConst.INVALID_PASSWORD.getMessage()));
                break;
                
            case SUCCESS_LOGIN:
                // 正しく認証が行えた場合
                
                // 認証コード確認画面へユーザIDとパスワードを渡すためにFaceContextにセットする
                facesContext.getExternalContext().getFlash().put("editUserId", this.getEditUserId());
                
                // 認証コード確認画面へ遷移
                return "/auth/user/editUserIdCheck?faces-redirect=true";

        }
        // 元の画面に戻る
        return "";
    }
    
    /**
     * パスワードの変更処理を実施する
     * @return 
     */
    @ErrorInterceptor
    @LogInterceptor
    public String passwordEdit() {
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        
        // 確認用パスワードが正常に入力されているかチェック
        if(!this.getEditPassword().equals(this.getEditCheckPassword())) {
            // 確認用パスワードが誤っていた場合
            facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("password-edit-form:edit-check-password", new FacesMessage(MessageConst.INVALID_CHECK_PASSWORD.getMessage()));
            
            logger.warn("{}:{} EDIT_PASSWORD:{}, EDIT_CHECK_PASSWORD:{} {}.{}", MessageConst.INVALID_CHECK_PASSWORD.getId(), MessageConst.INVALID_CHECK_PASSWORD.getMessage(), this.getEditPassword(), this.getEditCheckPassword(), this.getClass().getName(), this.getClass());
            
            // 元の画面に戻る
            return "";
        }
        
        // sessionBeanからのユーザIDと入力したパスワードでログイン出来ることを確認
        MessageConst resultCode = accountLogic.authAccount(sessionBean.getUserId(), this.getPassword());
        
        
        switch(resultCode) {
            case LOGICAL_DELETE_USER:
                // 現在ログインしているユーザIDが論理削除済であった場合(正常に動作していればありえない動作のため)
                throw new ToyManagerException(MessageConst.LOGICAL_DELETE_USER.getMessage());
            
            case INVALID_USERID: 
                // 現在ログインしているユーザIDが存在しなかった場合(正常に動作していればありえない動作のため)
                throw new ToyManagerException(MessageConst.NOT_FOUND_USER.getMessage());
                
            case INVALID_PASSWORD: 
                // パスワード入力を誤った場合
                facesContext.addMessage("invalid-password", new FacesMessage(MessageConst.INVALID_PASSWORD.getMessage()));
                break;
                
            case SUCCESS_LOGIN:
                // 正しく認証が行えた場合
                
                // パスワードを変更
                accountLogic.editPassword(accountFacade.findByUserId(sessionBean.getUserId()), this.getEditPassword());
                facesContext.addMessage("edit-password-complete", new FacesMessage(MessageConst.SUCCESS_EDIT_PASSWORD.getMessage()));
                break;
        }
        // 元の画面に戻る
        return "";
    }
    
    @ErrorInterceptor
    @AuthGeneralInterceptor
    @LogInterceptor
    public void bookmarkable() {
    }
    
}
