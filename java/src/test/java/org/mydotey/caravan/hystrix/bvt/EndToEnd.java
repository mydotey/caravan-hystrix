package org.mydotey.caravan.hystrix.bvt;

import org.junit.Test;
import org.mydotey.caravan.hystrix.ExecutionContext;
import org.mydotey.caravan.hystrix.config.CircuitBreakerConfig;
import org.mydotey.java.ThreadExtension;

import static org.mydotey.caravan.hystrix.HystrixTestUtility.*;

import org.junit.Assert;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class EndToEnd {

    @Test
    public void circuitBreakerOpenAfterTimeouts() {
        config(TEST_COMMAND, CircuitBreakerConfig.EXECUTION_TIMEOUT_PROPERTY_KEY, "5");

        int hystrixErrorCount = 0;
        int normalCount = 0;
        for (int i = 0; i < 100; i++) {
            ExecutionContext executionContext = TEST_COMMAND.newExecutionContext();
            boolean started = executionContext.tryStartExecution();
            if (!started) {
                hystrixErrorCount++;
                continue;
            }

            normalCount++;
            ThreadExtension.sleep(10);

            executionContext.markSuccess();
            executionContext.endExecution();
        }

        clearConfig(TEST_COMMAND, CircuitBreakerConfig.EXECUTION_TIMEOUT_PROPERTY_KEY);

        System.out.println("hystrixErrorCount: " + hystrixErrorCount);
        System.out.println("normalCount: " + normalCount);
        Assert.assertTrue(hystrixErrorCount > 0);
    }

}
