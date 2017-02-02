/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import com.hassoubeat.toymanager.service.exception.FailedSendMailException;

/**
 *
 * @author hassoubeat
 */
public interface MailLogicInterface {
    
    /**
     * メールを送信する処理
     * @param to 送信先
     * @param letterTitle メールタイトル
     * @param letterBody メール本文
     * @param isHtmlMail htmlメールかを判断するフラグ
     * @throws com.hassoubeat.toymanager.service.exception.FailedSendMailException
     */
    public void send(String to, String letterTitle, String letterBody, boolean isHtmlMail) throws FailedSendMailException;
}
