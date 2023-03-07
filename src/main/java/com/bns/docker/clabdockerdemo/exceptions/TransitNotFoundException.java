package com.bns.docker.clabdockerdemo.exceptions;

public class TransitNotFoundException extends RuntimeException{
    public TransitNotFoundException(Long id){
        super("Could not find a transit "+ id);
    }
}
