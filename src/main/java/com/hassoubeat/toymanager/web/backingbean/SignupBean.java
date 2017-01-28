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
import com.hassoubeat.toymanager.util.MessageConst;
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
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author hassoubeat
 */
@Named(value = "signupBean")
@ViewScoped
@Data
public class SignupBean implements Serializable{
    
    @Inject
    GMailLogic mailLogic;
    
    @EJB
    AccountFacade accountFacade;
    
    @Size(min=1,max=256)
    @Pattern(regexp="^[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+(\\.[a-zA-Z0-9!#$%&'_`/=~\\*\\+\\-\\?\\^\\{\\|\\}]+)*+(.*)@[a-zA-Z0-9][a-zA-Z0-9\\-]*(\\.[a-zA-Z0-9\\-]+)+$")
    @NotNull
    private String userId = "";
    
    @Size(min=1,max=500)
    @NotNull
    private String password = "";
    
    @Size(min=1,max=500)
    @NotNull
    private String checkPassword = "";
    
    private String salt;
    private String userInputSalt;
    
    /**
     * Creates a new instance of SingupBean
     */
    public SignupBean() {
    }
    
    @LogInterceptor
    @ErrorInterceptor
    public String userIdCheck() throws MessagingException, UnsupportedEncodingException{
        
        System.out.println("自クラス: SignupBean : メソッド userIdChech");
        
        // userId(メールアドレスの重複登録チェック)
        if(accountFacade.countByUserId(this.getUserId()) >= 1) {
            // 1件以上登録されていれば重複とみなし、エラーメッセージを表示する
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("user-id", new FacesMessage(MessageConst.ALREADY_REGISTED_USER_ID_MESSAGE_ID + MessageConst.ALREADY_REGISTED_USER_ID_MESSAGE));
            // TODO メッセージ内容のチェック
//            logger.log(Level.WARNING, "[WARNING]:" + this.getClass().getName() + "." + this.getClass(). "" ":{0}",);
            
            
            // 元の画面に戻る
            return "";
        }
        
        // ワンタイムパスワードの生成
        this.setSalt(BCrypt.gensalt(12));
        
        mailLogic.send(this.getUserId());
        
        return "";
    }
    
    public String userIdAuth() {
        if (this.getUserInputSalt().equals(this.getSalt())) {
            System.out.println("認証完了");
        }
        return "";
    }
    
//    public void signup() {
//        // ソルトの生成
//        String salt = BCrypt.gensalt(12);
//        
//        final String hashedPasswordWithSalt = BCrypt.hashpw(this.getPassword(), salt);
//        
//        Account account = new Account();
//        account.setUser(this.getUser());
//        account.setPassword(hashedPasswordWithSalt);
//        account.setRole("admin");
//        
//        accountFacade.create(account);
//    }
    
    
    
    
    
}
