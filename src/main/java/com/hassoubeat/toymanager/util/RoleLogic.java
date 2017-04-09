/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

import com.hassoubeat.toymanager.constant.EventRoopParamConst;
import com.hassoubeat.toymanager.constant.RoleConst;
import javax.ejb.Stateless;
import javax.inject.Inject;
import lombok.Getter;

/**
 * アカウントの権限(ロール)に関する処理
 * @author hassoubeat
 */
@Stateless
public class RoleLogic {
    
    /**
     * Manager権限以上であるかをチェックするメソッド
     * @param roleAuthority チェック対象のパラメータ
     * @return 
     */
    public boolean isManagerRole(int roleAuthority) {
        if (RoleConst.MANAGER.getAuthority() <= roleAuthority) {
            // Manager権限以上だった場合
            return true;
        } else {
            // Manager権限以下だった場合
            return false;
        }
    }
    
    /**
     * Admin権限以上であるかをチェックするメソッド
     * @param roleAuthority チェック対象のパラメータ
     * @return 
     */
    public boolean isAdminRole(int roleAuthority) {
        if (RoleConst.ADMIN.getAuthority() <= roleAuthority) {
            // Admin権限以上だった場合
            return true;
        } else {
            // Admin権限以下だった場合
            return false;
        }
    }
    
    
    
}
