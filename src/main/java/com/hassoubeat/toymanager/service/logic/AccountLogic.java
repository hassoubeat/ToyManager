/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.logic;

import com.hassoubeat.toymanager.service.dao.AccountFacade;
import com.hassoubeat.toymanager.service.dao.RoleFacade;
import com.hassoubeat.toymanager.service.entity.Account;
import com.hassoubeat.toymanager.service.exception.FailedSendMailException;
import com.hassoubeat.toymanager.service.exception.LogicalDeletedException;
import com.hassoubeat.toymanager.service.exception.LoginException;
import com.hassoubeat.toymanager.service.exception.RemindPasswordException;
import com.hassoubeat.toymanager.util.GMailLogic;
import com.hassoubeat.toymanager.util.MailLogicInterface;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.sql.SQLException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
        
        logger.info("{} : USER_ID:{}, PRIMARY_KEY:{}, {}", MessageConst.SUCCESS_SIGNUP_ID + ":" + MessageConst.SUCCESS_SIGNUP, createdAccount.getUserId(), createdAccount.getId(), this.getClass().getName() + "." + this.getClass());
        
        // TODO TryCatchでExceptionを捕まえて独自のユーザ登録失敗例外にラップして投げる
        
    }
    
    /**
     * ログイン処理を実行する
     * 
     * @param userId ユーザID(メールアドレス)
     * @param password パスワード
     * @throws com.hassoubeat.toymanager.service.exception.LoginException
     * @throws com.hassoubeat.toymanager.service.exception.LogicalDeletedException
     */
    public void login(String userId, String password) throws LoginException, LogicalDeletedException {
        
        Account targetAccount = null;
        
        try {
            // DBからuserId名で検索 一件も引っかからなければ、ID/パスワードどちらかが誤っています
            targetAccount = accountFacade.findByUserId(userId);
        } catch (SQLException ex) {
            // UserIdが存在しなかった場合
            logger.warn("{}:{}, USER_ID:{}, PASSWORD:{}, {}", MessageConst.INVALID_ID_OR_PASSWORD_ID, MessageConst.INVALID_ID_OR_PASSWORD, userId, password, this.getClass().getName() + "." + this.getClass());
            
            // ログイン失敗例外をスローする
            throw new LoginException(MessageConst.INVALID_ID_OR_PASSWORD, ex);
        }
        
        // ハッシュパスワードの復元と認証
        if (!targetAccount.getPasswordHash().equals(BCrypt.hashpw(password, targetAccount.getPasswordSalt()))) {
            // パスワードが誤っていた場合
            
            logger.warn("{}:{}, USER_ID:{}, PASSWORD:{}, {}", MessageConst.INVALID_ID_OR_PASSWORD_ID, MessageConst.INVALID_ID_OR_PASSWORD, userId, password, this.getClass().getName() + "." + this.getClass());
            
            // ログイン失敗例外をスローする
            throw new LoginException(MessageConst.INVALID_ID_OR_PASSWORD);            
        }
        
        // アカウントの論理削除チェック
        if (targetAccount.getIsDeleted()) {
            // 論理削除済だった場合
            
            logger.warn("{}:{}, USER_ID:{}, {}", MessageConst.LOGICAL_DELETE_USER_ID, MessageConst.LOGICAL_DELETE_USER, userId, this.getClass().getName() + "." + this.getClass());
            
            // TODO 論理削除例外をスローする (表示するメッセージをどうするかは後日検討)
            throw new LogicalDeletedException();
        }
        
        // セッションにログイン情報の格納
        sessionBean.login(targetAccount);
        
        // ロガーからログイン成功の情報を出力
        logger.info("{}:{}, USER_ID:{}, ROLE:{}, {}", MessageConst.SUCCESS_LOGIN_ID, MessageConst.SUCCESS_LOGIN, sessionBean.getUserId(), sessionBean.getRole(), this.getClass().getName() + "." + this.getClass());
    }
    
    /**
     * ログアウト処理を実行する
     */
    public void logout() {
        // セッション情報を削除 失敗した場合は、ログイン情報が無い旨を逆に通知する
        // ロガーからログアウト成功の情報を出力
        // ログイン画面へリダイレクト遷移
    }
    
    /**
     * リマインド処理を実行する
     * @param userId
     * @throws com.hassoubeat.toymanager.service.exception.FailedSendMailException
     * @throws com.hassoubeat.toymanager.service.exception.RemindPasswordException
     */
    public void remind(String userId) throws FailedSendMailException, RemindPasswordException {
        
        Account targetAccount = null;
        
        try {
            // DBからuserId名で検索 一件も引っかからなければ、ID/パスワードどちらかが誤っています
            targetAccount = accountFacade.findByUserId(userId);
        } catch (SQLException ex) {
            // UserIdが存在しなかった場合
            logger.warn("{}:{}, USER_ID:{}, {}", MessageConst.NOT_FOUND_USER_ID, MessageConst.NOT_FOUND_USER, userId, this.getClass().getName() + "." + this.getClass());
            
            // リマインド例外をスローする
            throw new RemindPasswordException(MessageConst.NOT_FOUND_USER, ex);
        }
        
        // 新パスワードを生成する(ランダムな英数字12桁)
        String newPassword = RandomStringUtils.randomAlphanumeric(12);
        
        // パスワードの変更処理
        this.editPassword(targetAccount, newPassword);
        
        // ユーザIDへ新パスワードの送信
        try {
            mailLogic.send(userId, "新しいパスワードを発行しました", mailLogic.genPassRemindLetterBody(newPassword, false), false);
        } catch (FailedSendMailException ex) {
            // リマインドメール送信失敗時
            
            logger.error("{}:{}, USER_ID:{}, {}", MessageConst.FAILED_SEND_REMIND_PASSWORD_MAIL_ID, MessageConst.FAILED_SEND_REMIND_PASSWORD_MAIL, userId, this.getClass().getName() + "." + this.getClass());
            throw new FailedSendMailException("", ex);
        }
        
        logger.info("{}:{}, USER_ID:{}, {}", MessageConst.SUCCESS_RIMIND_PASSWORD_ID, MessageConst.SUCCESS_RIMIND_PASSWORD, userId, this.getClass().getName() + "." + this.getClass());
        
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
        
        // TODO なんかException起きるなら捕まえる
        Account editAccount = accountFacade.edit(targetAccount);
        
        // パスワード変更のロガー出力
        logger.info("{}:{}, USER_ID:{}, {}", MessageConst.SUCCESS_EDIT_PASSWORD_ID, MessageConst.SUCCESS_EDIT_PASSWORD, targetAccount.getUserId(), this.getClass().getName() + "." + this.getClass());
        
        return editAccount;
    }
    
}
