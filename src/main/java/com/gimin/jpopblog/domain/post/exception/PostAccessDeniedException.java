package com.gimin.jpopblog.domain.post.exception;

public class PostAccessDeniedException extends RuntimeException{
    public PostAccessDeniedException(String message){
        super(message);
    }
}
