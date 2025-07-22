// EmailNotVerifiedException.java
package com.aisl.shop.exception.auth;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
