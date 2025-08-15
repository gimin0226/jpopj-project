package com.gimin.jpopblog.domain.post.exception;

public class PostDeleteAccessDeniedException extends RuntimeException{
    public PostDeleteAccessDeniedException(String message){
        super(message);
    }
}
