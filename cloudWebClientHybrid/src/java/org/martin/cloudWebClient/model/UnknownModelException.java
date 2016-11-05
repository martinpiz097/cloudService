/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.model;

/**
 *
 * @author martin
 */
public class UnknownModelException extends Exception {

    /**
     * Creates a new instance of <code>UnknownModelException</code> without
     * detail message.
     */
    public UnknownModelException() {
        super("Tabla sin modelo definido");
    }

    /**
     * Constructs an instance of <code>UnknownModelException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UnknownModelException(String msg) {
        super(msg);
    }
}
