package org.mydotey.caravan.hystrix.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.caravan.hystrix.ExecutionEvent;
import org.mydotey.java.ObjectExtension;
import org.mydotey.util.CounterBuffer;
import org.mydotey.util.DataBuffer;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionMetrics implements ExecutionMetrics {

    private ExecutionCommand _command;

    private CounterBuffer<ExecutionEvent> _eventCounterBuffer;
    private DataBuffer<Long> _executionLatencyBuffer;
    private HealthSnapshot _healthSnapshot;

    public DefaultExecutionMetrics(ExecutionCommand command) {
        ObjectExtension.requireNonNull(command, "command");
        _command = command;
        reset();
    }

    @Override
    public void markEvent(ExecutionEvent event) {
        _eventCounterBuffer.increment(event);
    }

    @Override
    public long getEventCount(ExecutionEvent event) {
        return _eventCounterBuffer.get(event);
    }

    @Override
    public HealthSnapshot getHealthSnapshot() {
        return _healthSnapshot;
    }

    @Override
    public void markExecutionLatency(long latency) {
        _executionLatencyBuffer.add(latency);
    }

    @Override
    public long getLatencyPercentile(double percent) {
        List<Long> snapshot = new ArrayList<>();
        _executionLatencyBuffer.consume(value -> snapshot.add(value));
        Collections.sort(snapshot);

        if (snapshot.size() <= 0)
            return 0;

        Long dataItem = null;
        if (percent <= 0.0)
            dataItem = snapshot.get(0);
        else if (percent >= 100.0)
            dataItem = snapshot.get(snapshot.size() - 1);
        else {
            int rank = (int) (percent * (snapshot.size() - 1) / 100);
            dataItem = snapshot.get(rank);
        }

        return dataItem == null ? 0 : dataItem.longValue();
    }

    @Override
    public AuditData getLatencyAuditData() {
        final AuditData auditData = new AuditData();
        auditData.setMax(Long.MIN_VALUE);
        auditData.setMin(Long.MAX_VALUE);
        _executionLatencyBuffer.consume(param -> {
            if (param == null)
                return;

            auditData.setCount(auditData.getCount() + 1);
            long value = param.longValue();
            auditData.setSum(auditData.getSum() + value);
            if (value < auditData.getMin())
                auditData.setMin(value);
            if (value > auditData.getMax())
                auditData.setMax(value);
        });

        if (auditData.getCount() == 0) {
            auditData.setMax(0);
            auditData.setMin(0);
        }

        return auditData;
    }

    @Override
    public long getLatencyCountInRange(long lowerBound, long upperBound) {
        AtomicInteger count = new AtomicInteger(0);
        _executionLatencyBuffer.consume(param -> {
            if (param == null)
                return;

            long value = param.longValue();
            if (value >= lowerBound && value < upperBound)
                count.incrementAndGet();
        });

        return count.intValue();
    }

    @Override
    public void reset() {
        _eventCounterBuffer = new CounterBuffer<>(_command.config().metricsConfig().eventCounterConfig());
        _executionLatencyBuffer = new DataBuffer<>(
            _command.config().metricsConfig().executionLatencyConfig());
        _healthSnapshot = new DefaultHealthSnapshot(_command);
    }

}
