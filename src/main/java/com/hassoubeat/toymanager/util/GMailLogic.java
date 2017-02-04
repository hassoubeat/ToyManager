/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import com.hassoubeat.toymanager.service.exception.FailedSendMailException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;

/**
 * 送信元がGmailのメール送信ロジック
 * @author hassoubeat
 */
@ApplicationScoped
public class GMailLogic implements MailLogicInterface {

    @Inject
    Logger logger;
    
    // メール送信に必要となるパラメータ群。コンストラクタで外部ファイルから値を読み込んで初期化する
    String fromAddress;
    String fromName;
    String fromPassword;
    String smtpHost;
    String smtpPort;
    String smtpTimeout;
    String starttls;
    String charset;
    String encoding;

    
    /**
     * Creates a new instance of MailLogic
     */
    public GMailLogic() {
        // パラメータの初期化
        ResourceBundle messageProperties = ResourceBundle.getBundle("Mail");
        fromAddress = messageProperties.getString("mail.from.address");
        fromName = messageProperties.getString("mail.from.name");
        fromPassword = messageProperties.getString("mail.from.password");
        smtpHost = messageProperties.getString("mail.smtp.host");
        smtpPort = messageProperties.getString("mail.smtp.port");
        smtpTimeout = messageProperties.getString("mail.smtp.timeout");
        starttls = messageProperties.getString("mail.is.starttls");
        charset = messageProperties.getString("mail.charset");
        encoding = messageProperties.getString("mail.encoding");
    }
    
     /**
     * 引数の情報でメールを送信する
     * @param to メールの宛先
     * @param letterTitle メールタイトル
     * @param letterBody メール本文
     * @param isHtmlMail htmlメールかを判断するフラグ
     * @throws FailedSendMailException
     */
    @Override
    public void send(String to, String letterTitle ,String letterBody, boolean isHtmlMail) throws FailedSendMailException{
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.connectiontimeout", smtpTimeout);
        props.put("mail.smtp.timeout", smtpTimeout);
        props.put("mail.debug", "true");
        
        Session session = Session.getInstance(props,
            new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, fromPassword);
            }
        });
        
        try {
            MimeMessage message = new MimeMessage(session);

            // 送信元のセット
            message.setFrom(new InternetAddress(fromAddress, fromName));
            // 送信先のセット
            message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
            // 返信先のセット
    //        message.setReplyTo(new Address[]{new InternetAddress(this.getFrom())});

            // メールタイトルのセット
            message.setSubject(letterTitle, charset);

            if (isHtmlMail) {
                // HTMLメールだった場合
                message.setHeader("Content-Type", "text/html; charset=" + charset);
                // TODO リッチにする
                message.setContent("<html><body>" + letterBody + "</body></html>", "text/html; charset=" + charset);
            } else {
                // テキストメールだった場合
                message.setHeader("Content-Transfer-Encoding", encoding);
                // TODO リッチにする
                message.setText(letterBody , charset);
            }

            Transport.send(message);
            
            logger.info("{}:{} , FROM_ADDRESS:{}, FROM_NAME:{}, SMTP_HOST:{}, SMTP_PORT:{}, SMTP_TIMEOUT:{}, STARTTLS:{}, CHARSET:{}, ENCODING:{}, {}",
                    Message.SUCCESS_SEND_AUTH_CODE_MAIL.getId(),
                    Message.SUCCESS_SEND_AUTH_CODE_MAIL.getMessage(),
                    fromAddress,
                    fromName,
                    smtpHost,
                    smtpPort,
                    smtpTimeout,
                    starttls,
                    charset,
                    encoding,
                    this.getClass().getName() + "." + this.getClass());
            
        } catch (MessagingException | UnsupportedEncodingException ex) {
            logger.error("{}:{} , FROM_ADDRESS:{}, FROM_NAME:{}, SMTP_HOST:{}, SMTP_PORT:{}, SMTP_TIMEOUT:{}, STARTTLS:{}, CHARSET:{}, ENCODING:{}, {}",
                    Message.FAILED_SEND_MAIL.getId(),
                    Message.FAILED_SEND_MAIL.getMessage(),
                    fromAddress,
                    fromName,
                    smtpHost,
                    smtpPort,
                    smtpTimeout,
                    starttls,
                    charset,
                    encoding,
                    this.getClass().getName() + "." + this.getClass());
            
            // メール送信失敗時の例外
            // TODO 例外設計するときに表示するメッセージを検討する
            throw new FailedSendMailException("", ex);
        }
    }
    
    /**
     * 認証コード送信メール本文の整形を行う
     * @param content 含めたい文字
     * @param isHtmlMail htmlメール形式に整形を行うか判断するフラグ
     * @return letterBody 
     */
    public String genAuthCodeLetterBody(String content, boolean isHtmlMail) {
        
        String letterBody = "";
        
        if (isHtmlMail) {
            // HTMLメール形式の場合

            // 認証コードのセット
            letterBody += "<div style=\"font-size:12px;line-height:14px;color:#555555;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px\"><span style=\"font-size: 22px; line-height: 26px;\">" + content + "</span></p></div>";
            letterBody += "</div></td></tr></tbody></table>";
            letterBody += "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\">";
            letterBody += "<tbody><tr style=\"vertical-align: top\">";
            letterBody += "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-top: 15px;padding-right: 10px;padding-bottom: 10px;padding-left: 10px\">";
            letterBody += "<div style=\"color:#aaaaaa;line-height:120%;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;\">";
            letterBody += "<div style=\"font-size:12px;line-height:14px;color:#aaaaaa;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 17px\">上記のコードをブラウザの認証コード入力フォームに入力し、サインアップを完了させてください。</p></div>";
            letterBody += "</div></td></tr></tbody></table>";
            letterBody += "</td></tr></tbody></table></div><!--[if (gte mso 9)|(IE)]></td><![endif]--><!--[if (gte mso 9)|(IE)]></td></tr></table><![endif]--></td></tr></tbody></table></td></tr></tbody></table>";
            letterBody += "</td></tr></tbody></table>";
            letterBody += "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" border=\"0\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\">";
            letterBody += "<tbody><tr style=\"vertical-align: top\">";
            letterBody += "<td width=\"100%\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;background-color: #444444\">";
            letterBody += "<table id=\"outlookholder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>";
            letterBody += "<table width=\"500\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">";
            letterBody += "<tr><td>";
            letterBody += "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" border=\"0\" class=\"container\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top;max-width: 500px;margin: 0 auto;text-align: inherit\"><tbody><tr style=\"vertical-align: top\"><td width=\"100%\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"transparent\" class=\"block-grid \" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top;width: 100%;max-width: 500px;color: #333;background-color: transparent\"><tbody><tr style=\"vertical-align: top\"><td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;background-color: transparent;text-align: center;font-size: 0\"><!--[if (gte mso 9)|(IE)]><table width=\"100%\" align=\"center\" bgcolor=\"transparent\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><![endif]--><!--[if (gte mso 9)|(IE)]><td valign=\"top\" width=\"500\" style=\"width:500px;\"><![endif]--><div class=\"col num12\" style=\"display: inline-block;vertical-align: top;width: 100%\"><table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" border=\"0\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\"><tbody><tr style=\"vertical-align: top\"><td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;background-color: transparent;padding-top: 25px;padding-right: 0px;padding-bottom: 25px;padding-left: 0px;border-top: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-left: 0px solid transparent\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\">";
            letterBody += "<tbody><tr style=\"vertical-align: top\">";
            letterBody += "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-top: 10px;padding-right: 10px;padding-bottom: 10px;padding-left: 10px\">";
            letterBody += "<div style=\"color:#bbbbbb;line-height:120%;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;\">";
            letterBody += "<div style=\"font-size:12px;line-height:14px;text-align:center;color:#bbbbbb;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px;text-align: center\"><span style=\"color: rgb(255, 255, 255); font-size: 12px; line-height: 14px;\">@ToyManager</span></p></div>";
            letterBody += "</div></td></tr></tbody></table>";        
            letterBody += "</td></tr></tbody></table></div><!--[if (gte mso 9)|(IE)]></td><![endif]--><!--[if (gte mso 9)|(IE)]></td></tr></table><![endif]--></td></tr></tbody></table></td></tr></tbody></table>";
            letterBody += "</td></tr></tbody></table></td></tr></tr></tbody></table>";
            
        } else {
            // テキストメール形式の場合
            
            // 改行コードを取得
            String separator = System.getProperty("line.separator");
            letterBody += "認証コード：" + content + separator;
            letterBody += separator;
            letterBody += "上記の認証コードをブラウザより入力し、サインアップを完了させてください。" + separator;
            letterBody += separator;
            letterBody += "@ToyManager";
        
        }
        
        return letterBody;
    }
    
    /**
     * パスワードリマインド送信メール本文の整形を行う
     * @param content 含めたい文字
     * @param isHtmlMail htmlメール形式に整形を行うか判断するフラグ
     * @return letterBody 
     */
    public String genPassRemindLetterBody(String content, boolean isHtmlMail) {
        String letterBody = "";
        
        if (isHtmlMail) {
            // HTMLメール形式の場合

            // 認証コードのセット
            letterBody += "<div style=\"font-size:12px;line-height:14px;color:#555555;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px\"><span style=\"font-size: 22px; line-height: 26px;\">" + content + "</span></p></div>";
            letterBody += "</div></td></tr></tbody></table>";
            letterBody += "<table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\">";
            letterBody += "<tbody><tr style=\"vertical-align: top\">";
            letterBody += "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-top: 15px;padding-right: 10px;padding-bottom: 10px;padding-left: 10px\">";
            letterBody += "<div style=\"color:#aaaaaa;line-height:120%;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;\">";
            letterBody += "<div style=\"font-size:12px;line-height:14px;color:#aaaaaa;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 17px\">新たなパスワード発行後は、ToyManagerにログインしてパスワードを変更を行って下さい。</p></div>";
            letterBody += "</div></td></tr></tbody></table>";
            letterBody += "</td></tr></tbody></table></div><!--[if (gte mso 9)|(IE)]></td><![endif]--><!--[if (gte mso 9)|(IE)]></td></tr></table><![endif]--></td></tr></tbody></table></td></tr></tbody></table>";
            letterBody += "</td></tr></tbody></table>";
            letterBody += "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" border=\"0\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\">";
            letterBody += "<tbody><tr style=\"vertical-align: top\">";
            letterBody += "<td width=\"100%\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;background-color: #444444\">";
            letterBody += "<table id=\"outlookholder\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"><tr><td>";
            letterBody += "<table width=\"500\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">";
            letterBody += "<tr><td>";
            letterBody += "<table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" border=\"0\" class=\"container\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top;max-width: 500px;margin: 0 auto;text-align: inherit\"><tbody><tr style=\"vertical-align: top\"><td width=\"100%\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" bgcolor=\"transparent\" class=\"block-grid \" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top;width: 100%;max-width: 500px;color: #333;background-color: transparent\"><tbody><tr style=\"vertical-align: top\"><td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;background-color: transparent;text-align: center;font-size: 0\"><!--[if (gte mso 9)|(IE)]><table width=\"100%\" align=\"center\" bgcolor=\"transparent\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><![endif]--><!--[if (gte mso 9)|(IE)]><td valign=\"top\" width=\"500\" style=\"width:500px;\"><![endif]--><div class=\"col num12\" style=\"display: inline-block;vertical-align: top;width: 100%\"><table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"100%\" border=\"0\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\"><tbody><tr style=\"vertical-align: top\"><td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;background-color: transparent;padding-top: 25px;padding-right: 0px;padding-bottom: 25px;padding-left: 0px;border-top: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-left: 0px solid transparent\"><table cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-spacing: 0;border-collapse: collapse;vertical-align: top\">";
            letterBody += "<tbody><tr style=\"vertical-align: top\">";
            letterBody += "<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-top: 10px;padding-right: 10px;padding-bottom: 10px;padding-left: 10px\">";
            letterBody += "<div style=\"color:#bbbbbb;line-height:120%;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;\">";
            letterBody += "<div style=\"font-size:12px;line-height:14px;text-align:center;color:#bbbbbb;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px;text-align: center\"><span style=\"color: rgb(255, 255, 255); font-size: 12px; line-height: 14px;\">@ToyManager</span></p></div>";
            letterBody += "</div></td></tr></tbody></table>";        
            letterBody += "</td></tr></tbody></table></div><!--[if (gte mso 9)|(IE)]></td><![endif]--><!--[if (gte mso 9)|(IE)]></td></tr></table><![endif]--></td></tr></tbody></table></td></tr></tbody></table>";
            letterBody += "</td></tr></tbody></table></td></tr></tr></tbody></table>";
            
        } else {
            // テキストメール形式の場合
            
            // 改行コードを取得
            String separator = System.getProperty("line.separator");
            letterBody += "新しいパスワード：" + content + separator;
            letterBody += separator;
            letterBody += "新たなパスワード発行後は、ToyManagerにログインしてパスワードを変更を行って下さい。" + separator;
            letterBody += separator;
            letterBody += "@ToyManager";
        
        }
        
        return letterBody;
    }
    
}
