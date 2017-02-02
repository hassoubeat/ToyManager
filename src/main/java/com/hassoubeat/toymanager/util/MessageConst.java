/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import java.util.ResourceBundle;

/**
 *
 * @author hassoubeat
 */
public class MessageConst {
    
    // インフォメーションメッセージ
    public static String GEN_USER_AUTH_CODE_ID;
    public static String GEN_USER_AUTH_CODE;
    public static String SUCCESS_USER_AUTH_CODE_ID;
    public static String SUCCESS_USER_AUTH_CODE;
    public static String SUCCESS_SIGNUP_ID;
    public static String SUCCESS_SIGNUP;
    public static String SUCCESS_SEND_AUTH_CODE_MAIL_ID;
    public static String SUCCESS_SEND_AUTH_CODE_MAIL;
    public static String SUCCESS_LOGIN_ID;
    public static String SUCCESS_LOGIN;
    public static String SUCCESS_EDIT_PASSWORD_ID;
    public static String SUCCESS_EDIT_PASSWORD;
    public static String SUCCESS_RIMIND_PASSWORD_ID;
    public static String SUCCESS_RIMIND_PASSWORD;
    
    // 想定内エラーメッセージ
    public static String ALREADY_REGISTED_USER_ID;
    public static String ALREADY_REGISTED_USER;
    public static String FAILED_USER_AUTH_CODE_ID;
    public static String FAILED_USER_AUTH_CODE;
    public static String FAILED_SIGNUP_ID;
    public static String FAILED_SIGNUP;
    public static String INVALID_SCREEN_TRANSITION_ID;
    public static String INVALID_SCREEN_TRANSITION;
    public static String INVALID_ID_OR_PASSWORD_ID;
    public static String INVALID_ID_OR_PASSWORD;
    public static String LOGICAL_DELETE_USER_ID;
    public static String LOGICAL_DELETE_USER;
    public static String NOT_FOUND_USER_ID;
    public static String NOT_FOUND_USER;

    
            
    // 想定外エラーメッセージ
    public static String SYSTEM_ERROR_ID;
    public static String SYSTEM_ERROR;
    public static String FAILED_SEND_MAIL_ID;
    public static String FAILED_SEND_MAIL;
    public static String FAILED_SEND_AUTH_CODE_MAIL_ID;
    public static String FAILED_SEND_AUTH_CODE_MAIL;
    public static String FAILED_SEND_REMIND_PASSWORD_MAIL_ID;
    public static String FAILED_SEND_REMIND_PASSWORD_MAIL;
    
    /**
     * メッセージ定数を初期化するstaticイニシャライザ
     */
    static {
        ResourceBundle messageProperties = ResourceBundle.getBundle("Message");
        GEN_USER_AUTH_CODE_ID = messageProperties.getString("GEN_USER_AUTH_CODE_ID");
        GEN_USER_AUTH_CODE = messageProperties.getString("GEN_USER_AUTH_CODE");
        SUCCESS_USER_AUTH_CODE_ID = messageProperties.getString("SUCCESS_USER_AUTH_CODE_ID");
        SUCCESS_USER_AUTH_CODE = messageProperties.getString("SUCCESS_USER_AUTH_CODE");
        SUCCESS_SIGNUP_ID = messageProperties.getString("SUCCESS_SIGNUP_ID");
        SUCCESS_SIGNUP = messageProperties.getString("SUCCESS_SIGNUP");
        SUCCESS_SEND_AUTH_CODE_MAIL_ID = messageProperties.getString("SUCCESS_SEND_AUTH_CODE_MAIL_ID");
        SUCCESS_SEND_AUTH_CODE_MAIL = messageProperties.getString("SUCCESS_SEND_AUTH_CODE_MAIL");
        SUCCESS_LOGIN_ID = messageProperties.getString("SUCCESS_LOGIN_ID");
        SUCCESS_LOGIN = messageProperties.getString("SUCCESS_LOGIN");
        SUCCESS_EDIT_PASSWORD_ID = messageProperties.getString("SUCCESS_EDIT_PASSWORD_ID");
        SUCCESS_EDIT_PASSWORD = messageProperties.getString("SUCCESS_EDIT_PASSWORD");
        SUCCESS_RIMIND_PASSWORD_ID = messageProperties.getString("SUCCESS_RIMIND_PASSWORD_ID");
        SUCCESS_RIMIND_PASSWORD = messageProperties.getString("SUCCESS_RIMIND_PASSWORD");
        
        ALREADY_REGISTED_USER_ID = messageProperties.getString("ALREADY_REGISTED_USER_ID");
        ALREADY_REGISTED_USER = messageProperties.getString("ALREADY_REGISTED_USER");
        FAILED_USER_AUTH_CODE_ID = messageProperties.getString("FAILED_USER_AUTH_CODE_ID");
        FAILED_USER_AUTH_CODE = messageProperties.getString("FAILED_USER_AUTH_CODE");
        FAILED_SIGNUP_ID = messageProperties.getString("FAILED_SIGNUP_ID");
        FAILED_SIGNUP = messageProperties.getString("FAILED_SIGNUP");
        INVALID_SCREEN_TRANSITION_ID = messageProperties.getString("INVALID_SCREEN_TRANSITION_ID");
        INVALID_SCREEN_TRANSITION = messageProperties.getString("INVALID_SCREEN_TRANSITION");
        INVALID_ID_OR_PASSWORD_ID = messageProperties.getString("INVALID_ID_OR_PASSWORD_ID");
        INVALID_ID_OR_PASSWORD = messageProperties.getString("INVALID_ID_OR_PASSWORD");
        LOGICAL_DELETE_USER_ID = messageProperties.getString("LOGICAL_DELETE_USER_ID");
        LOGICAL_DELETE_USER = messageProperties.getString("LOGICAL_DELETE_USER");
        NOT_FOUND_USER_ID = messageProperties.getString("NOT_FOUND_USER_ID");
        NOT_FOUND_USER = messageProperties.getString("NOT_FOUND_USER");
        
        SYSTEM_ERROR_ID = messageProperties.getString("SYSTEM_ERROR_ID");
        SYSTEM_ERROR = messageProperties.getString("SYSTEM_ERROR");
        FAILED_SEND_MAIL_ID = messageProperties.getString("FAILED_SEND_MAIL_ID");
        FAILED_SEND_MAIL = messageProperties.getString("FAILED_SEND_MAIL");
        FAILED_SEND_AUTH_CODE_MAIL_ID = messageProperties.getString("FAILED_SEND_AUTH_CODE_MAIL_ID");
        FAILED_SEND_AUTH_CODE_MAIL = messageProperties.getString("FAILED_SEND_AUTH_CODE_MAIL");
        FAILED_SEND_REMIND_PASSWORD_MAIL_ID = messageProperties.getString("FAILED_SEND_REMIND_PASSWORD_MAIL_ID");
        FAILED_SEND_REMIND_PASSWORD_MAIL = messageProperties.getString("FAILED_SEND_REMIND_PASSWORD_MAIL");
    }
    
    
}
