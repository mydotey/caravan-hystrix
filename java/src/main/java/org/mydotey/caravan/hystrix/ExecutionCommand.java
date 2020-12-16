package org.mydotey.caravan.hystrix;

import org.mydotey.caravan.hystrix.circuitbreaker.CircuitBreaker;
import org.mydotey.caravan.hystrix.config.CommandConfig;
import org.mydotey.caravan.hystrix.isolator.Isolator;
import org.mydotey.caravan.hystrix.metrics.ExecutionMetrics;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public interface ExecutionCommand {

    String managerId();

    String groupId();

    String commandId();

    String commandKey();

    CommandConfig config();

    ExecutionMetrics metrics();

    CircuitBreaker circuitBreaker();

    Isolator isolator();

    ExecutionContext newExecutionContext();

}
