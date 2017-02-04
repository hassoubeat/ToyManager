/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.exception;

/**
 * 不正な画面遷移例外
 * @author hassoubeat
 */
public class InvalidScreenTransitionException extends RuntimeException {

    /**
     * Creates a new instance of <code>InvalidScreenTransitionException</code>
     * without detail message.
     */
    public InvalidScreenTransitionException() {
    }

    /**
     * Constructs an instance of <code>InvalidScreenTransitionException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidScreenTransitionException(String msg) {
        super(msg);
    }
    
    public InvalidScreenTransitionException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
