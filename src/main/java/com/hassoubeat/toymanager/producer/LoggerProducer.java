/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.producer;


import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loggerを取得するためのプロデューサーメソッド
 * @author hassoubeat
 */
@Dependent
public class LoggerProducer {

    @Produces
    public Logger getLogger(InjectionPoint ip){
        
        Logger logger = LoggerFactory.getLogger(ip.getMember().getDeclaringClass().getName());
        ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger)logger;
        
//        log.setLevel(Level.ALL);
//        ResourceBundle loggingPropeties = ResourceBundle.getBundle(LOGGING_PROPERTIES);

        return logger;
    }
}
