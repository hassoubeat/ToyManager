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
import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
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
    
    @Inject
    SessionBean sessionBean;
    
    public ExceptionInterceptor() {
    }
    
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception{
        Object ret = null;
        String targetClassName = context.getTarget().getClass().getSuperclass().getName();
        String targetMethodName = context.getMethod().getName();
        
        
        logger.info("{} USER_ID:{} {}.{}", "START_EXCEPTION_CHECK", sessionBean.getUserId(),targetClassName, targetMethodName);
        
        long beforeTime = System.nanoTime();
        
        try {
            // インターセプトするオブジェクトの実行
            ret = context.proceed();
            // TODO 今後発生しうるExceptionは以下に追加していく
        } catch (InvalidScreenTransitionException ex) {
            // 不正な画面遷移例外の発生時処理
            logger.warn("{}:{} USER_ID:{} {}.{}", MessageConst.INVALID_SCREEN_TRANSITION.getId(), MessageConst.INVALID_SCREEN_TRANSITION.getMessage(), sessionBean.getUserId(),targetClassName, targetMethodName, ex);
            
            // 例外発生時、エラーページにリダイレクト遷移する
            FacesContext facesContext = FacesContext.getCurrentInstance();
            
            facesContext.addMessage(null, new FacesMessage(MessageConst.INVALID_SCREEN_TRANSITION.getMessage()));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/error.xhtml");
        } catch (Exception ex) {
            // 想定外例外の発生時処理
            logger.error("{}:{} USER_ID:{} {}.{}", MessageConst.SYSTEM_ERROR.getId(), MessageConst.SYSTEM_ERROR.getMessage(), sessionBean.getUserId(), targetClassName, targetMethodName, ex);
            
            // 例外発生時、エラーページにリダイレクト遷移する
            FacesContext facesContext = FacesContext.getCurrentInstance();
            
            facesContext.addMessage(null, new FacesMessage(MessageConst.SYSTEM_ERROR.getMessage()));
            facesContext.getExternalContext().getFlash().setKeepMessages(true);
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/error.xhtml");
        }

        long afterTime = System.nanoTime();
        logger.info("{} USER_ID:{} {}.{} TIME:{}nsecs.", "EXCEPTION_CHECK_TIME", sessionBean.getUserId(),targetClassName, targetMethodName, (afterTime - beforeTime));
        
        logger.info("{} USER_ID:{} {}.{}", "END_EXCEPTION_CHECK", sessionBean.getUserId(),targetClassName, targetMethodName);
  
        return ret;

    }
    
}
