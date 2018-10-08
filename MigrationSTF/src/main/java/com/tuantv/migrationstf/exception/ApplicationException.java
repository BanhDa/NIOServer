/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuantv.migrationstf.exception;

/**
 *
 * @author tuantv
 */
public class ApplicationException extends RuntimeException{
    
    public ApplicationException(String message) {
        super(message);
    }
    
    public ApplicationException(String message, Throwable caused) {
        super(message, caused);
    }
}
