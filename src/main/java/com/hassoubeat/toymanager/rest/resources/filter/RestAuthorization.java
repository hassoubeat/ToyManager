/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.filter;

import com.hassoubeat.toymanager.annotation.RestAuth;
import com.hassoubeat.toymanager.rest.resources.logic.RestEventLogic;
import java.io.IOException;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * REST_APIをリクエストされた際に受け取ったリクエストを認証するフィルター
 * @author hassoubeat
 */
@Provider
//@RestAuth
public class RestAuthorization implements ContainerRequestFilter{
    
// TODO Injectすると何故かHK2エラーで死ぬ
//    @Inject
//    RestEventLogic restEventLogic;
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // ヘッダから認可情報の取得
        String rotNum = requestContext.getHeaderString("rotNum");
        String accessToken = requestContext.getHeaderString("authorication");
        String macAddress = requestContext.getHeaderString("macAddress");
        
//        RestEventLogic restEventLogic = new RestEventLogic();
//        restEventLogic.RestAuthorization(rotNum, accessToken, macAddress);
    }
}
