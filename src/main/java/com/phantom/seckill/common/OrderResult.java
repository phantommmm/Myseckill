package com.phantom.seckill.common;


public enum OrderResult {
    FAILED(-1), WAITING(0);

    private long value;

    public long getValue() {
        return value;
    }

    OrderResult(long value) {
        this.value = value;
    }
}
