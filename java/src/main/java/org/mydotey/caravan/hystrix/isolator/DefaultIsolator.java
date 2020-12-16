package org.mydotey.caravan.hystrix.isolator;

import java.util.concurrent.atomic.AtomicInteger;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.java.ObjectExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultIsolator implements Isolator {

    private ExecutionCommand _command;
    private AtomicInteger _concurrentCount = new AtomicInteger();

    public DefaultIsolator(ExecutionCommand command) {
        ObjectExtension.requireNonNull(command, "command");
        _command = command;
    }

    @Override
    public boolean allowExecution() {
        int concurrentCount = _concurrentCount.incrementAndGet();
        if (concurrentCount <= _command.config().isolatorConfig().maxConcurrentCount())
            return true;

        markComplete();
        return false;
    }

    @Override
    public void markComplete() {
        _concurrentCount.decrementAndGet();
    }

    @Override
    public long concurrentCount() {
        return _concurrentCount.get();
    }

}
