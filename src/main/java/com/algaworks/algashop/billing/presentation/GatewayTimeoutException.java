package com.algaworks.algashop.billing.presentation;

public class GatewayTimeoutException extends RuntimeException {
    public GatewayTimeoutException() {
    }

    public GatewayTimeoutException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
