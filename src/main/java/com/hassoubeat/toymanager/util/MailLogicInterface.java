/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

/**
 *
 * @author hassoubeat
 */
public interface MailLogicInterface {
    
    /**
     * メールを送信する処理
     * @param to 送信先
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    public void send(String to) throws MessagingException, UnsupportedEncodingException;
}
