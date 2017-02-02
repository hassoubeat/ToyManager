/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hassoubeat.toymanager.service.exception;

/**
 * 論理削除が行われている時のException
 * @author hassoubeat
 */
public class LogicalDeletedException extends Exception {

    /**
     * Creates a new instance of <code>LogicalDeletedException</code> without
     * detail message.
     */
    public LogicalDeletedException() {
    }

    /**
     * Constructs an instance of <code>LogicalDeletedException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LogicalDeletedException(String msg) {
        super(msg);
    }
    
    public LogicalDeletedException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
