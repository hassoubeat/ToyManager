/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.exception;

/**
 * パスワードリマインド時のException
 * @author hassoubeat
 */
public class RemindPasswordException extends Exception {

    /**
     * Creates a new instance of <code>RemindPasswordException</code> without
     * detail message.
     */
    public RemindPasswordException() {
    }

    /**
     * Constructs an instance of <code>RemindPasswordException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RemindPasswordException(String msg) {
        super(msg);
    }
    
    public RemindPasswordException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
