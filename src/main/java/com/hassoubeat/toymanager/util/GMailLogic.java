/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.Data;
import org.slf4j.Logger;

/**
 * 送信元がGmailのメール送信ロジック
 * @author hassoubeat
 */
@ApplicationScoped
@Data
public class GMailLogic implements MailLogicInterface {

    @Inject
    Logger logger;
    
    // TODO プロパティファイルから読み込むように変更
    String from = "hassoubeat.work@gmail.com";
    String fromPassword = "Arialove0";
    String host = "smtp.gmail.com";
    String port = "587";
    String starttls = "true";
    
    
    String charset = "UTF-8";
    String encoding = "base64";

    
    /**
     * Creates a new instance of MailLogic
     */
    public GMailLogic() {
    }
    
     /**
     * 引数の情報でメールを送信する
     * @param to メールの宛先
     * @throws javax.mail.MessagingException
     * @throws java.io.UnsupportedEncodingException
     */
    @Override
    public void send(String to) throws MessagingException, UnsupportedEncodingException{
        Properties props = new Properties();
        props.put("mail.smtp.host", this.getHost());
        props.put("mail.smtp.port", this.getPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", this.getStarttls());
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.debug", "true");
        
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getFrom(), getFromPassword());
            }
        });
        
        MimeMessage message = new MimeMessage(session);

        // Set From:
        message.setFrom(new InternetAddress(this.getFrom(), "Rekki Katou"));
        // Set ReplyTo:
        message.setReplyTo(new Address[]{new InternetAddress(this.getFrom())});
        // Set To:
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        
        // メールタイトル
        message.setSubject("サンプル", this.getCharset());
        // メール本文
        message.setText("サンプル本文", this.getCharset());

        message.setHeader("Content-Transfer-Encoding", this.getEncoding());

        Transport.send(message);
    }
    
}
