package org.app.instagram_be.exception;

public class UserNameNotFoundException extends Exception{

    public UserNameNotFoundException(String errorMsg) {
        super(errorMsg);
    }
}
