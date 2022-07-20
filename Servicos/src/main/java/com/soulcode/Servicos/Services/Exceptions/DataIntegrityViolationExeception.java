package com.soulcode.Servicos.Services.Exceptions;

public class DataIntegrityViolationExeception extends RuntimeException{

    public DataIntegrityViolationExeception(String msg){
        super(msg);
    }
}
