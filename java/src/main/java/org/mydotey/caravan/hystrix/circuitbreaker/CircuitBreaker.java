package org.mydotey.caravan.hystrix.circuitbreaker;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface CircuitBreaker {

    boolean allowExecution();

    void markSuccess();

    boolean isOpen();

}
