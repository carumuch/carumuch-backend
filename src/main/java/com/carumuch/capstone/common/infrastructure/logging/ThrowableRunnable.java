package com.carumuch.capstone.common.infrastructure.logging;

@FunctionalInterface
public interface ThrowableRunnable {
    void run() throws Throwable;
}
