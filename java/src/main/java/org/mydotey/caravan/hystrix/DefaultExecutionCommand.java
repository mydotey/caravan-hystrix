package org.mydotey.caravan.hystrix;

import org.mydotey.caravan.hystrix.circuitbreaker.CircuitBreaker;
import org.mydotey.caravan.hystrix.circuitbreaker.DefaultCircuitBreaker;
import org.mydotey.caravan.hystrix.config.CommandConfig;
import org.mydotey.caravan.hystrix.config.DefaultExecutionConfig;
import org.mydotey.caravan.hystrix.isolator.DefaultIsolator;
import org.mydotey.caravan.hystrix.isolator.Isolator;
import org.mydotey.caravan.hystrix.metrics.DefaultExecutionMetrics;
import org.mydotey.caravan.hystrix.metrics.ExecutionMetrics;
import org.mydotey.caravan.hystrix.util.KeyManager;
import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionCommand implements ExecutionCommand {

    private String _managerId;
    private String _groupId;
    private String _commandId;
    private String _commandKey;
    private CommandConfig _config;
    private ExecutionMetrics _metrics;
    private CircuitBreaker _circuitBreaker;
    private Isolator _isolator;

    public DefaultExecutionCommand(String managerId, String groupId, String commandId,
        final StringProperties properties,
        final TimeSequenceCircularBufferConfig eventCounterConfig,
        final DataBufferConfig executionLatencyConfig) {
        ObjectExtension.requireNonNull(managerId, "managerId");
        ObjectExtension.requireNonNull(groupId, "groupId");
        ObjectExtension.requireNonBlank(commandId, "commandId");
        ObjectExtension.requireNonNull(properties, "properties");
        ObjectExtension.requireNonNull(eventCounterConfig, "eventCounterConfig");
        ObjectExtension.requireNonNull(executionLatencyConfig, "executionLatencyConfig");

        _managerId = managerId.trim();
        _groupId = groupId.trim();
        _commandId = commandId.trim();
        _commandKey = KeyManager.getCommandKey(this);

        _config = new DefaultExecutionConfig(this, properties, eventCounterConfig, executionLatencyConfig);
        _metrics = new DefaultExecutionMetrics(this);
        _circuitBreaker = new DefaultCircuitBreaker(this);
        _isolator = new DefaultIsolator(this);
    }

    @Override
    public String groupId() {
        return _groupId;
    }

    @Override
    public String managerId() {
        return _managerId;
    }

    @Override
    public String commandId() {
        return _commandId;
    }

    @Override
    public String commandKey() {
        return _commandKey;
    }

    @Override
    public CommandConfig config() {
        return _config;
    }

    @Override
    public ExecutionMetrics metrics() {
        return _metrics;
    }

    @Override
    public CircuitBreaker circuitBreaker() {
        return _circuitBreaker;
    }

    @Override
    public Isolator isolator() {
        return _isolator;
    }

    @Override
    public ExecutionContext newExecutionContext() {
        return new DefaultExecutionContext(this);
    }

}
