package org.mydotey.caravan.hystrix.config;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionConfig implements CommandConfig {

    private MetricsConfig _metricsConfig;
    private CircuitBreakerConfig _circuitBreakerConfig;
    private IsolatorConfig _isolatorConfig;

    public DefaultExecutionConfig(ExecutionCommand command, StringProperties properties,
        TimeSequenceCircularBufferConfig eventCounterConfig, DataBufferConfig executionLatencyConfig) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(properties, "properties");

        _metricsConfig = new DefaultMetricsConfig(command, properties, eventCounterConfig, executionLatencyConfig);
        _circuitBreakerConfig = new DefaultCircuitBreakerConfig(command, properties);
        _isolatorConfig = new DefaultIsolatorConfig(command, properties);
    }

    @Override
    public MetricsConfig metricsConfig() {
        return _metricsConfig;
    }

    @Override
    public CircuitBreakerConfig circuitBreakerConfig() {
        return _circuitBreakerConfig;
    }

    @Override
    public IsolatorConfig isolatorConfig() {
        return _isolatorConfig;
    }

}
