package com.carumuch.capstone.common.infrastructure.logging;

@FunctionalInterface
public interface ThrowableCallable<V> {
    V call() throws Throwable;
}
