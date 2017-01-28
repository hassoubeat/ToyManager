/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;


import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author hassoubeat
 */
@Named(value = "accountBean")
@RequestScoped
@Data
public class AccountBean {

    @EJB
    AccountFacade accountFacade;
    
    @NotNull
    @Size(min=1,max=256)
    private String userId;
    
    @NotNull
    @Size(min=1,max=500)
    private String password;
            
    private Account account; 
    
    /**
     * Creates a new instance of LoginBean
     */
    public AccountBean() {
    }
    
    /**
     * 
     */
    @LogInterceptor
    @ErrorInterceptor
    public String login() {
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
//        
//        try {
//            
//            final String hashedPasswordWithSalt = BCrypt.hashpw(this.getPassword(), this.getSalt());
//            System.out.println(hashedPasswordWithSalt);
//            
//            // ログイン処理
//            request.login(this.getUser(), hashedPasswordWithSalt);
//            
//            // ユーザIDからアカウント情報を取得
//            Account account = accountFacade.find(this.getUser());
//            
//            System.out.println("ログイン対象パスワード：" + account.getPassword());
//            
//            if (account.getPassword().equals(hashedPasswordWithSalt)){
//                System.out.println("認証OK");
//                // セッションスコープにログイン情報を格納
//                loginSessionBean.setAccount(account);
//            }
//            
//            // ログイン後トップ画面にログイン画面に遷移
//            return "/auth/index?faces-redirect=true";
//            
//        } catch (Exception ex) {
//            return "error";
//        }
        return "error";
    }
    
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
    
    public void bookmarkable() {
        // ユーザIDからアカウント情報を取得
//        this.setAccount(accountFacade.find("admin"));
    }
}
