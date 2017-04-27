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
        
        logger.debug("{} : {}","START_METHOD_RUN", targetClassName + "." + targetMethodName);
        long beforeTime = System.nanoTime();
        
        // インターセプトするオブジェクトの実行
        Object ret = context.proceed();
        
        long afterTime = System.nanoTime();
        logger.debug("{} : {}", "METHOD_RUN_TIME", targetClassName + "." + targetMethodName + ":" + (afterTime - beforeTime) + "nsecs.");
        
        logger.debug("{} : {}", "END_METHOD_RUN", targetClassName + "." + targetMethodName);
        
        return ret;
    }
    
}
