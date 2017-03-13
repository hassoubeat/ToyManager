/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.exception;

/**
 * RESTAPIのアクセストークンの有効期限切れ例外
 * @author hassoubeat
 */
public class AccessTokenExpiredException extends RuntimeException {
    
    public AccessTokenExpiredException() {
    }
    
    public AccessTokenExpiredException(String msg) {
        super(msg);
    }
    
    public AccessTokenExpiredException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
    
}
