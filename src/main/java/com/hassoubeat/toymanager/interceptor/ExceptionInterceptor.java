/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.interceptor;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import com.hassoubeat.toymanager.annotation.ErrorInterceptor;
import com.hassoubeat.toymanager.service.exception.FailedSendMailException;
import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import com.hassoubeat.toymanager.util.MessageConst;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Interceptor
@ErrorInterceptor
public class ExceptionInterceptor implements Serializable{
    
    @Inject
    Logger logger;
    
    public ExceptionInterceptor() {
    }
    
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception{
        Object ret = null;
        String targetClassName = context.getTarget().getClass().getSuperclass().getName();
        String targetMethodName = context.getMethod().getName();
        
        
        logger.info("{} {}", "START_EXCEPTION_CHECK", targetClassName + "." + targetMethodName);
        
        long beforeTime = System.nanoTime();
        
        try {
            // インターセプトするオブジェクトの実行
            ret = context.proceed();
            // TODO 今後発生しうるExceptionは以下に追加していく
        } catch (InvalidScreenTransitionException ex) {
            // 不正な画面遷移例外の発生時処理
            logger.warn("{} {}", MessageConst.INVALID_SCREEN_TRANSITION_ID + ":" + MessageConst.INVALID_SCREEN_TRANSITION, targetClassName + "." + targetMethodName, ex);
            
            // 例外発生時、エラーページにリダイレクト遷移する
            FacesContext facesContext = FacesContext.getCurrentInstance();
            
            facesContext.addMessage(null, new FacesMessage(MessageConst.INVALID_SCREEN_TRANSITION));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/error.xhtml");
        } catch (Exception ex) {
            // 想定外例外の発生時処理
            logger.error("{} {}", MessageConst.SYSTEM_ERROR_ID + ":" +MessageConst.SYSTEM_ERROR, targetClassName + "." + targetMethodName, ex);
            
            // 例外発生時、エラーページにリダイレクト遷移する
            FacesContext facesContext = FacesContext.getCurrentInstance();
            
            facesContext.addMessage(null, new FacesMessage(MessageConst.SYSTEM_ERROR));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/error.xhtml");
        }

        long afterTime = System.nanoTime();
        logger.info("{} : {}", "EXCEPTION_CHECK_TIME", targetClassName + "." + targetMethodName + ":" + (afterTime - beforeTime) + "nsecs.");
        
        logger.info("{} {}", "END_EXCEPTION_CHECK", targetClassName + "." + targetMethodName);
  
        return ret;

    }
    
}
