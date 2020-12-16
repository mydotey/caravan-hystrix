package org.mydotey.caravan.hystrix.metrics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.caravan.hystrix.ExecutionEvent;
import org.mydotey.java.ObjectExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultHealthSnapshot implements HealthSnapshot {

    private final static Set<ExecutionEvent> _healthRelatedEvents = new HashSet<>(Arrays.asList(ExecutionEvent.SUCCESS,
        ExecutionEvent.FAILED, ExecutionEvent.TIMEOUT));
    private final static Set<ExecutionEvent> _errorEvents = new HashSet<>(Arrays.asList(ExecutionEvent.FAILED,
        ExecutionEvent.TIMEOUT));

    private volatile long _lastSnapshotTime;
    private volatile long _totalCount;
    private volatile int _errorPercentage;

    private ExecutionCommand _command;

    public DefaultHealthSnapshot(ExecutionCommand command) {
        ObjectExtension.requireNonNull(command, "command");
        _command = command;
    }

    @Override
    public long totalCount() {
        tryUpdateSnapshot();
        return _totalCount;
    }

    @Override
    public int errorPercentage() {
        tryUpdateSnapshot();
        return _errorPercentage;
    }

    private void tryUpdateSnapshot() {
        long now = System.currentTimeMillis();
        if (now - _lastSnapshotTime < _command.config().metricsConfig().healthSnapshotInterval())
            return;

        long totalCount = 0;
        long errorCount = 0;
        for (ExecutionEvent event : _healthRelatedEvents) {
            long eventCount = _command.metrics().getEventCount(event);
            totalCount += eventCount;
            if (_errorEvents.contains(event))
                errorCount += eventCount;
        }

        int errorPercentage = 0;
        if (totalCount > 0)
            errorPercentage = (int) (errorCount * 100 / totalCount);

        _totalCount = totalCount;
        _errorPercentage = errorPercentage;
        _lastSnapshotTime = now;
    }

}
