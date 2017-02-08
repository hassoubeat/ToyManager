/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.exception;

/**
 * ToyManager内で想定されない動作が行われた時
 * @author hassoubeat
 */
public class ToyManagerException extends RuntimeException {

    /**
     * Creates a new instance of <code>ToyManagerException</code> without detail
     * message.
     */
    public ToyManagerException() {
    }

    /**
     * Constructs an instance of <code>ToyManagerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ToyManagerException(String msg) {
        super(msg);
    }
    
    public ToyManagerException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
