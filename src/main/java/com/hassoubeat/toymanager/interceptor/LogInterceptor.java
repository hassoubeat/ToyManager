/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.interceptor;

import java.io.Serializable;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Interceptor
@com.hassoubeat.toymanager.annotation.LogInterceptor
public class LogInterceptor implements Serializable{
    
    @Inject
    Logger logger;
    
    public LogInterceptor() {
    }
    
    // TODO セッション情報があれば加えて出力
    
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception{
        String targetClassName = context.getTarget().getClass().getSuperclass().getName();
        String targetMethodName = context.getMethod().getName();
        
        long beforeTime = System.nanoTime();
        logger.info("{} : {}","START_RUN_METHOD", targetClassName + "." + targetMethodName);
        
        // インターセプトするオブジェクトの実行
        Object ret = context.proceed();
        
        logger.info("{} : {}", "END_RUN_METHOD", targetClassName + "." + targetMethodName);
        long afterTime = System.nanoTime();
        
        logger.info("{} : {}", "RUN_TIME", targetClassName + "." + targetMethodName + ":" + (afterTime - beforeTime) + "nsecs.");
//        logger.trace("trace");
//        logger.debug("debug");
//        logger.info("info");
//        logger.warn("warn");
//        logger.error("error");
        
        return ret;
    }
    
}
