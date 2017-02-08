/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.interceptor;

import com.hassoubeat.toymanager.service.exception.InvalidScreenTransitionException;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import com.hassoubeat.toymanager.util.MessageConst;
import com.hassoubeat.toymanager.util.RoleConst;
import com.hassoubeat.toymanager.web.backingbean.session.SessionBean;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
@Interceptor
@com.hassoubeat.toymanager.annotation.AuthAdminInterceptor
public class AuthAdminInterceptor implements Serializable{
    
    @Inject
    Logger logger;
    
    @Inject
    SessionBean sessionBean;
    
    public AuthAdminInterceptor() {
    }
    
    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        Object ret = null;
        String targetClassName = context.getTarget().getClass().getSuperclass().getName();
        String targetMethodName = context.getMethod().getName();
        
        
        logger.info("{} {}.{}", "START_ROLE_CHECK({})", targetClassName, targetMethodName, RoleConst.ADMIN.getRoleName());
        
        long beforeTime = System.nanoTime();
        
        if (!sessionBean.getIsAuth()) {
            // 未ログインの場合(ログインページに誘導する)
            
            logger.warn("{}:{}, {}.{}", MessageConst.NOT_LOGIN.getId(), MessageConst.NOT_LOGIN.getMessage(), targetClassName, targetMethodName);
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/faces/login.xhtml");
            
            return null;
        } else {
            // 権限の比較. 一般権限(ADMIN)以上のアカウントを許可する
            if (!(sessionBean.getRoleAuthority() >= RoleConst.ADMIN.getAuthority())) {
            
               // 一般権限以下の場合
               logger.warn("{}:{} USER_ID:{}, USER_ROLE:{}, REQUIRED_ROLE:{}, {}.{}", MessageConst.UN_AUTHORITY.getId(), MessageConst.UN_AUTHORITY.getMessage(), sessionBean.getUserId(), sessionBean.getRole(), RoleConst.ADMIN.getRoleName(), targetClassName, targetMethodName);

               // ユーザには不正遷移という体で通知する
               throw new InvalidScreenTransitionException();
            }
        }
        
        // インターセプトするオブジェクトの実行
        ret = context.proceed();
        
        long afterTime = System.nanoTime();
        
        logger.info("{}:{} USER_ID:{}, USER_ROLE:{}, REQUIRED_ROLE:{}, {}.{}", MessageConst.SUCCESS_AUTHORITY.getId(), MessageConst.SUCCESS_AUTHORITY.getMessage(), sessionBean.getUserId(), sessionBean.getRole(), RoleConst.ADMIN.getRoleName(), targetClassName, targetMethodName);
        
        logger.info("{} {}.{} {}", "ROLE_CHECK_TIME", targetClassName, targetMethodName + ":" + (afterTime - beforeTime) + "nsecs.");
        
        logger.info("{} {}.{}", "END_ROLE_CHECK({})", targetClassName, targetMethodName, RoleConst.ADMIN.getRoleName());
  
        return ret;

    }
    
}
