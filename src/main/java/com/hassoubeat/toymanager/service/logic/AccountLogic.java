/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.RoleFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.util.GMailLogic;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Stateless
public class AccountLogic{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    @Inject
    GMailLogic mailLogic; 
    
    @EJB
    AccountFacade accountFacade;
    
    @EJB
    RoleFacade roleFacade;

    public AccountLogic() {
    }
    
    /**
     * アカウントのサインアップ処理を実行する
     * 
     * @param userId 登録するユーザID(メールアドレス)
     * @param password 登録するパスワード
     */
    public void signup(String userId, String password) {
        // ソルトの生成
        String salt = BCrypt.gensalt(12);
        
        final String hashedPasswordWithSalt = BCrypt.hashpw(password, salt);
        
        Account account = new Account();
        account.setUserId(userId);
        account.setPasswordHash(hashedPasswordWithSalt);
        account.setPasswordSalt(salt);
        // 一般権限の付与
        account.setRoleId(roleFacade.find(3));
        
        // アカウントの登録
        Account createdAccount = accountFacade.create(account);        
        
        logger.info("{} : USER_ID:{}, PRIMARY_KEY:{}, {}", MessageConst.SUCCESS_SIGNUP.getId() + ":" + MessageConst.SUCCESS_SIGNUP.getMessage(), createdAccount.getUserId(), createdAccount.getId(), this.getClass().getName() + "." + this.getClass());
        
    }
    
    /**
     * ログイン処理を実行する
     * 
     * @param userId ユーザID(メールアドレス)
     * @param password パスワード
     * @return 実行結果コードを返却する
     */
    public MessageConst login(String userId, String password) {
        
        MessageConst resultCode = this.authAccount(userId, password);
        
        if(MessageConst.SUCCESS_LOGIN.equals(resultCode)) {
            // ログイン認証が正常だった場合、実際にログイン処理を実行する
            Account targetAccount = accountFacade.findByUserId(userId);
            
            // セッションにログイン情報の格納
            sessionBean.login(targetAccount);

            // ロガーからログイン成功の情報を出力
            logger.info("{}:{}, USER_ID:{}, ROLE:{}, {}", MessageConst.SUCCESS_LOGIN.getId(), MessageConst.SUCCESS_LOGIN.getMessage(), sessionBean.getUserId(), sessionBean.getRole(), this.getClass().getName() + "." + this.getClass());
        }
        
        return resultCode;
        
    }
    
    /**
     * 渡されたユーザIDとパスワードが有効であるか認証する
     * 
     * @param userId ユーザID(メールアドレス)
     * @param password パスワード
     * @return 実行結果コードを返却する
     */
    public MessageConst authAccount(String userId, String password) {
        if (accountFacade.countByUserId(userId) <= 0) {
            // ログイン対象のUserIdが存在しなかった場合
            
            logger.warn("{}:{}, USER_ID:{}, PASSWORD:{}, {}", MessageConst.INVALID_USERID.getId(), MessageConst.INVALID_USERID.getMessage(), userId, password, this.getClass().getName() + "." + this.getClass());
            
            // 実行結果コードを返す
            return MessageConst.INVALID_USERID;
        }
        
        // ログイン対象のアカウント情報の取得
        Account targetAccount = accountFacade.findByUserId(userId);
        
        // ハッシュパスワードの復元と認証
        if (!targetAccount.getPasswordHash().equals(BCrypt.hashpw(password, targetAccount.getPasswordSalt()))) {
            // パスワードが誤っていた場合
            
            logger.warn("{}:{}, USER_ID:{}, PASSWORD:{}, {}", MessageConst.INVALID_PASSWORD.getId(), MessageConst.INVALID_PASSWORD.getMessage(), userId, password, this.getClass().getName() + "." + this.getClass());
            
            // 実行結果コードを返す
            return MessageConst.INVALID_PASSWORD;
        }
        
        // アカウントの論理削除チェック
        if (targetAccount.getIsDeleted()) {
            // 論理削除済だった場合
            
            logger.warn("{}:{}, USER_ID:{}, {}", MessageConst.LOGICAL_DELETE_USER.getId(), MessageConst.LOGICAL_DELETE_USER.getMessage(), userId, this.getClass().getName() + "." + this.getClass());
            
            // 実行結果コードを返す
            return MessageConst.LOGICAL_DELETE_USER;
        }
        
        // 実行結果コードを返す
        return MessageConst.SUCCESS_LOGIN;
    }
    
    /**
     * ログアウト処理を実行する
     * @return 実行結果コード
     */
    public MessageConst logout() {
        // TODO セッション情報はSessionBeanにいれてるけどこのお作法が必要か？
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        if(session!=null){
            try{
                session.invalidate();
            } catch(Exception e){
                //throwされる可能性があるのはIllegalStateException
                //セッション破棄失敗ログなど
            }
        }
        
        // ロガー出力用にユーザIDを取得する
        String logoutTargetUserId = sessionBean.getUserId();
        
        // セッション情報の破棄
        sessionBean.logout();
        
        logger.info("{}:{}, USER_ID:{}, {}.{}", MessageConst.SUCCESS_LOGOUT.getId(), MessageConst.SUCCESS_LOGOUT.getMessage(), logoutTargetUserId, this.getClass().getName(), this.getClass());
        
        return MessageConst.SUCCESS_LOGOUT;
    }
    
    /**
     * リマインド処理を実行する
     * @param userId
     * @return 実行結果コード
     */
    public MessageConst remind(String userId) {
        
        Account targetAccount = null;
        
        if (accountFacade.countByUserId(userId) <= 0) {
            // ログイン対象のUserIdが存在しなかった場合
            
            // UserIdが存在しなかった場合
            logger.warn("{}:{}, USER_ID:{}, {}", MessageConst.NOT_FOUND_USER.getId(), MessageConst.NOT_FOUND_USER.getMessage(), userId, this.getClass().getName() + "." + this.getClass());
            
            // 実行結果コードを返す
            return MessageConst.NOT_FOUND_USER;
        }
        
        // DBからuserId名で検索
        targetAccount = accountFacade.findByUserId(userId);
        
        // 新パスワードを生成する(ランダムな英数字12桁)
        String newPassword = RandomStringUtils.randomAlphanumeric(12);
        
        // パスワードの変更処理
        this.editPassword(targetAccount, newPassword);
        
        // ユーザIDへ新パスワードの送信
        mailLogic.send(userId, "新しいパスワードを発行しました", mailLogic.genPassRemindLetterBody(newPassword, false), false);
        
        logger.info("{}:{}, USER_ID:{}, {}", MessageConst.SUCCESS_REMIND_PASSWORD.getId(), MessageConst.SUCCESS_REMIND_PASSWORD.getMessage(), userId, this.getClass().getName() + "." + this.getClass());
        
        // 実行結果コードを返す
        return MessageConst.SUCCESS_REMIND_PASSWORD;
    }
    
    /**
     * ユーザID(メールアドレス)変更処理を実行する
     * @param targetAccount ユーザIDを変更する対象のアカウント
     * @param afterEditUserId 変更後のユーザID
     * @return 
     */
    public Account editUserId(Account targetAccount, String afterEditUserId) {
        
        // 変更後のUserIdをセット
        targetAccount.setUserId(afterEditUserId);
        
        Account editAccount = accountFacade.edit(targetAccount);
        
        // ユーザID変更のロガー出力
        logger.info("{}:{}, USER_ID:{}, AFTER_EDIT_USER_ID:{} {}.{}", MessageConst.SUCCESS_EDIT_PASSWORD.getId(), MessageConst.SUCCESS_EDIT_PASSWORD.getMessage(), targetAccount.getUserId(), editAccount.getUserId(),this.getClass().getName(), this.getClass());
        
        return editAccount;
    }
    
    /**
     * パスワード変更処理を実行する
     * @param targetAccount パスワードを変更する対象のアカウント
     * @param afterEditPassword 変更後のパスワード
     * @return 
     */
    public Account editPassword(Account targetAccount, String afterEditPassword) {
        // 新しいソルトの生成
        String salt = BCrypt.gensalt(12);
        
        // 新パスワードのハッシュの生成
        final String hashedPasswordWithSalt = BCrypt.hashpw(afterEditPassword, salt);
        
        // ハッシュ化した新しいパスワードと新しいソルトをエンティティに格納
        targetAccount.setPasswordHash(hashedPasswordWithSalt);
        targetAccount.setPasswordSalt(salt);
        
        Account editAccount = accountFacade.edit(targetAccount);
        
        // パスワード変更のロガー出力
        logger.info("{}:{}, USER_ID:{}, {}.{}", MessageConst.SUCCESS_EDIT_PASSWORD.getId(), MessageConst.SUCCESS_EDIT_PASSWORD.getMessage(), targetAccount.getUserId(), this.getClass().getName(), this.getClass());
        
        return editAccount;
    }
    
}
