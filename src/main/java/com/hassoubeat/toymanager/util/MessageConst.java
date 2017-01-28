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
    // エラー時のメッセージ
    public static final String EXCEPTION_MESSAGE = "エラーが発生しました";
    
    // 想定内エラーメッセージ
    public static String ALREADY_REGISTED_USER_ID_MESSAGE_ID;
    public static String ALREADY_REGISTED_USER_ID_MESSAGE;
    
    // 想定外エラーメッセージ
    public static String SYSTEM_ERROR_ID;
    public static String SYSTEM_ERROR;
    
    /**
     * メッセージ定数を初期化するstaticイニシャライザ
     */
    static {
        ResourceBundle messageProperties = ResourceBundle.getBundle("Message");
        ALREADY_REGISTED_USER_ID_MESSAGE_ID = messageProperties.getString("ALREADY_REGISTED_USER_ID_MESSAGE_ID");
        ALREADY_REGISTED_USER_ID_MESSAGE = messageProperties.getString("ALREADY_REGISTED_USER_ID_MESSAGE");
        SYSTEM_ERROR_ID = messageProperties.getString("SYSTEM_ERROR_ID");
        SYSTEM_ERROR = messageProperties.getString("SYSTEM_ERROR");
    }
    
    
}
