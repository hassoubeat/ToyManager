/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.exception;

/**
 *
 * @author hassoubeat
 */
public class FailedSendMailException extends Exception {

    /**
     * Creates a new instance of <code>FailedSendMailException</code> without
     * detail message.
     */
    public FailedSendMailException() {
    }

    /**
     * Constructs an instance of <code>FailedSendMailException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FailedSendMailException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>FailedSendMailException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     * @param cause
     */
    public FailedSendMailException(String msg, Throwable cause) {
        super(msg,cause);
    }
}
