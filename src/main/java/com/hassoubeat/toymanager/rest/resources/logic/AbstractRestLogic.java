/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.logic;

import com.hassoubeat.toymanager.annotation.LogInterceptor;
import com.hassoubeat.toymanager.constant.MessageConst;
import com.hassoubeat.toymanager.rest.resources.exception.AccessFilterUnApprovalException;
import com.hassoubeat.toymanager.service.dao.ToyFacade;
import com.hassoubeat.toymanager.service.dao.ToyWebapiAccessFilterFacade;
import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import org.slf4j.Logger;

/**
 *
 * @author hassoubeat
 */
public abstract class AbstractRestLogic {
    
    @Inject
    Logger logger;
    
    @EJB
    ToyFacade toyFacade;
    
    @EJB
    ToyWebapiAccessFilterFacade toyWebapiAccessFilterFacade;
    
    /**
     * RestAPI実行時にアクセス認証を実施するメソッド 
     * @param rotNumStr
     * @param accessToken
     * @param macAddress
     */
    @LogInterceptor
    public void RestAuthorization(String rotNumStr, String accessToken, String macAddress) {
        
        Toy targetToy = new Toy();
        
        // TODO Toyのバージョンアップデート
        
        try {
            int rotNum = Integer.parseInt(rotNumStr);
            targetToy = toyFacade.findByRotNumber(rotNum);
        } catch (NullPointerException | NumberFormatException | EJBException ex) {
            // リクエスト不備エラーを返す
            // TODO 本来起こり得ない状況のため、扱いをどうするか検討する
            logger.warn("{}:{}, ROT_NUMBER:{}, ACCESS_TOKEN:{}, MAC_ADDRESS:{} {}.{}", MessageConst.REST_INVALID_PARAM.getId(), MessageConst.REST_INVALID_PARAM.getMessage(), rotNumStr, accessToken, macAddress,this.getClass().getName(), this.getClass());
            throw new BadRequestException(MessageConst.REST_INVALID_PARAM.getMessage(), ex);
        }
        
        ToyWebapiAccessFilter targetToyWebapiAccessFilter = null;
        
        // アクセスフィルターに既に存在するかをチェック
        for(ToyWebapiAccessFilter toyWebapiAccessFilter :targetToy.getToyWebapiAccessFilterList()) {
            if (toyWebapiAccessFilter.getMacAddress().equals(macAddress) ) {
                targetToyWebapiAccessFilter = toyWebapiAccessFilter;
            }
        }
        
        if (targetToyWebapiAccessFilter == null) {
            // アクセスフィルターに存在しなかった場合
            
            // 新たにアクセスフィルターに登録し、未認証エラーを返却する
            ToyWebapiAccessFilter toyWebapiAccessFilter = new ToyWebapiAccessFilter();
            toyWebapiAccessFilter.setMacAddress(macAddress);
            toyWebapiAccessFilter.setToyId(targetToy);
            ToyWebapiAccessFilter createToyWebapiAccessFilter = toyWebapiAccessFilterFacade.create(toyWebapiAccessFilter);
            
            // アクセスフィルター登録ログ出力
            logger.info("{}:{}, TOY_WEB_API_ACCESS_FILTER_ID:{}, MAC_ADDRESS:{} {}.{}", MessageConst.REST_SUCCESS_WEB_API_ACCESS_FILTER_CREATE.getId(), MessageConst.REST_SUCCESS_WEB_API_ACCESS_FILTER_CREATE.getMessage(), createToyWebapiAccessFilter.getId().toString(), createToyWebapiAccessFilter.getMacAddress(),this.getClass().getName(), this.getClass());
            // アクセスフィルター未承認例外の発行
            throw new AccessFilterUnApprovalException(MessageConst.REST_ACCESS_FILTER_UN_APPROVAL.getMessage());
        } else {
            // アクセスフィルターに存在した場合
            
            System.out.println("testaaa");
            
            if (!targetToyWebapiAccessFilter.getIsAuthenticated()) {
                // 未承認の場合
                
                // アクセスフィルター未承認ログ出力
                logger.warn("{}:{}, ROT_NUMBER:{}, TOY_WEB_API_ACCESS_FILTER_ID:{} {}.{}", MessageConst.REST_ACCESS_FILTER_UN_APPROVAL.getId(), MessageConst.REST_ACCESS_FILTER_UN_APPROVAL.getMessage(), rotNumStr, targetToyWebapiAccessFilter.getId().toString(), this.getClass().getName(), this.getClass());
                // アクセスフィルター未承認例外の発行
                throw new AccessFilterUnApprovalException(MessageConst.REST_ACCESS_FILTER_UN_APPROVAL.getMessage());
                
            }
        }
        
        if((targetToy.getAccessTokenLifecycle().compareTo(new Date())) < 0) {
            // アクセストークンの有効期限が切れている場合
            
            // アクセスフィルター有効期限切れログを出力
            logger.warn("{}:{}, ROT_NUMBER:{}, ACCESS_TOKEN:{}, ACCESS_TOKEN_LIFE_CYCLE:{} {}.{}", MessageConst.REST_ACCESS_TOKEN_EXPIRED.getId(), MessageConst.REST_ACCESS_TOKEN_EXPIRED.getMessage(), rotNumStr, targetToy.getAccessToken(), targetToy.getAccessTokenLifecycle().toString(), this.getClass().getName(), this.getClass());
            
            // TODO アクセストークン期限切れExceptionの発行(独自実装する必要があるかも？)
        }
        
        if(!targetToy.getAccessToken().equals(accessToken)) {
            // アクセストークンの認証が失敗した場合
            
            logger.warn("{}:{}, AUTH_TARGET_TOY_ID:{}, AUTH_TARGET_TOY_ROT_NUMBER:{}, AUTH_TARGET_TOY_ACCESS_TOKEN:{}, ROT_NUMBER:{}, ACCESS_TOKEN:{} {}.{}", MessageConst.REST_FAILED_AUTHORIZATION.getId(), MessageConst.REST_FAILED_AUTHORIZATION.getMessage(), targetToy.getId().toString(), targetToy.getRotNum(), targetToy.getAccessToken(), rotNumStr, accessToken, this.getClass().getName(), this.getClass());
            // 認証失敗例外の発行
            throw new NotAuthorizedException(MessageConst.REST_FAILED_AUTHORIZATION.getMessage());
        } 
    }
    
}
