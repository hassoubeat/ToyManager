/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.rest.resources.exception;

import javax.ejb.ApplicationException;

/**
 * アクセスフィルターの未承認例外
 * ※ アクセスフィルターななかった場合に登録 → 例外発行の流れで呼び出すため、ロールバックは行わない
 * @author hassoubeat
 */
@ApplicationException(rollback = false)
public class AccessFilterUnApprovalException extends RuntimeException {
    
    public AccessFilterUnApprovalException() {
    }
    
    public AccessFilterUnApprovalException(String msg) {
        super(msg);
    }
    
    public AccessFilterUnApprovalException(String msg, Throwable ex) {
        super(msg, ex);
    }
    
    
}
