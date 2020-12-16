package org.mydotey.caravan.hystrix.facade;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.caravan.hystrix.ExecutionEvent;
import org.mydotey.caravan.hystrix.exception.IsolationException;
import org.mydotey.caravan.hystrix.exception.ShortCircuitException;
import org.mydotey.java.ObjectExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class ExecutionExceptons {

    private ExecutionExceptons() {

    }

    public static RuntimeException newException(ExecutionCommand command, ExecutionEvent failEvent) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(failEvent, "failEvent");

        String errorMessage;
        switch (failEvent) {
            case SHORT_CIRCUITED:
                errorMessage = String.format("Execution is short-circuited by circuit-breaker. Command: %s",
                    command.commandKey());
                return new ShortCircuitException(errorMessage);
            case REJECTED:
                errorMessage = String.format("Execution is rejected by execution isolator. Command: %s",
                    command.commandKey());
                return new IsolationException(errorMessage);
            default:
                errorMessage = String.format("Execution failed. Command: %s, Fail event: %s", command.commandKey(),
                    failEvent);
                return new RuntimeException(errorMessage);
        }
    }

}
