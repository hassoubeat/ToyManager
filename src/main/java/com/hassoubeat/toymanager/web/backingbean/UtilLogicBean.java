/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.web.backingbean;

import com.hassoubeat.toymanager.service.entity.Toy;
import com.hassoubeat.toymanager.service.entity.ToyWebapiAccessFilter;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

/**
 * JSFフロントから汎用的に利用するロジックを定義するビーン
 * @author hassoubeat
 */
@Named(value = "utilLogicBean")
@ApplicationScoped
public class UtilLogicBean {

    public UtilLogicBean() {
    }
    
    /**
     * ToyのWebApiに未承認のものが存在するかを確認するメソッド
     * @param toy
     * @return 未承認ありの場合True
     */
    public boolean isExistUnapprovedAccess(Toy toy) {
        boolean isExistUnapprovedAccess = false;
        for(ToyWebapiAccessFilter toyWebapiAccessFilter : toy.getToyWebapiAccessFilterList()) {
            if (!toyWebapiAccessFilter.getIsAuthenticated()) {
                // アクセス未承認データがあった場合
                isExistUnapprovedAccess = true;
            }
        }
        return isExistUnapprovedAccess;
    }
    
    /**
     * Toyのアクセストークンの期限が切れているかを確認するメソッド
     * @param toy
     * @return 有効期限切れの場合True
     */
    public boolean isExistAccessTokenExpired(Toy toy) {
        boolean isExistAccessTokenExpired = false;
        if (toy.getAccessTokenLifecycle().compareTo(new Date())  <= 0) {
            // 有効期限切れの場合
            isExistAccessTokenExpired = true;
        }
        return isExistAccessTokenExpired;
    }
    
}
