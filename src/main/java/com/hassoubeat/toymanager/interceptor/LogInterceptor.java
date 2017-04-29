/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.interceptor;

import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
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
    
    @Inject
    SessionBean sessionBean;
    
    public LogInterceptor() {
    }
        
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception{
        String targetClassName = context.getTarget().getClass().getSuperclass().getName();
        String targetMethodName = context.getMethod().getName();
        
        logger.debug("{} : {} Session:{}","START_METHOD_RUN", targetClassName + "." + targetMethodName, sessionBean.toString());
        long beforeTime = System.nanoTime();
        
        // インターセプトするオブジェクトの実行
        Object ret = context.proceed();
        
        long afterTime = System.nanoTime();
        logger.debug("{} : {} ", "METHOD_RUN_TIME", targetClassName + "." + targetMethodName + ":" + (afterTime - beforeTime) + "nsecs.");
        
        logger.debug("{} : {} Session:{}", "END_METHOD_RUN", targetClassName + "." + targetMethodName, sessionBean.toString());
        
        return ret;
    }
    
}
