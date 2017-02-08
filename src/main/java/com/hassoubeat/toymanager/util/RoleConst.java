/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.util;

/**
 * 権限の定数定義クラス
 * @author hassoubeat
 */
public enum RoleConst {
    GENERAL("general", 10),
    MANAGER("manager", 20),
    ADMIN("admin", 30)
    ;

    private final String roleName;
    private final int authority;
    
    private RoleConst(final String roleName, final int authority) {
        this.roleName = roleName;
        this.authority = authority;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public int getAuthority() {
        return this.authority;
    }
    
    
    

    
}
