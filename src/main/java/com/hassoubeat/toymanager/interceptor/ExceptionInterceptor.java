/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.interceptor;

import com.hassoubeat.toymanager.util.MessageConst;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import java.io.Serializable;

/**
 *
 * @author hassoubeat
 */
@Interceptor
@ErrorInterceptor
public class ExceptionInterceptor implements Serializable{
    
    @Inject
//    LoggerBean loggerBean;
    
    public ExceptionInterceptor() {
    }
    
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception{
        Object ret = null;
//        String targetClassName = context.getTarget().getClass().getSuperclass().getName();
//        String targetMethodName = context.getMethod().getName();
//        
//        long beforeTime = System.nanoTime();
//        loggerBean.getLogger().log(Level.INFO, "START_RUN_METHOD:{0}", targetClassName + "." + targetMethodName);
//        
//        try {
//            // インターセプトするオブジェクトの実行
            ret = context.proceed();
//        } catch (Exception ex) {
//            loggerBean.getLogger().log(Level.WARNING, "Exception:{0}" ,targetClassName + "." + targetMethodName + ":" + ex.toString());
//            
//            // Exception発生時、トップページに戻す
//            FacesContext facesContext = FacesContext.getCurrentInstance();
//            facesContext.addMessage(null, new FacesMessage(MessageConst.EXCEPTION_MESSAGE));
//            facesContext.getExternalContext().getFlash().setKeepMessages(true);
//            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/index.xhtml");
//        } 
//        
//        loggerBean.getLogger().log(Level.INFO, "END_RUN_METHOD:{0}", targetClassName + "." + targetMethodName);
//        long afterTime = System.nanoTime();
//        
//        loggerBean.getLogger().log(Level.INFO, "RUN_TIME:{0}", targetClassName + "." + targetMethodName + ":" + (afterTime - beforeTime) + "nsecs.");
//        
        return ret;

    }
    
}
