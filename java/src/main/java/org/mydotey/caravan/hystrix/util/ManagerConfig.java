package org.mydotey.caravan.hystrix.util;

import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class ManagerConfig {

    private StringProperties _properties;
    private TimeSequenceCircularBufferConfig _eventCounterConfig;
    private DataBufferConfig _executionLatencyConfig;

    public ManagerConfig(StringProperties properties, TimeSequenceCircularBufferConfig eventCounterConfig,
        DataBufferConfig executionLatencyConfig) {
        ObjectExtension.requireNonNull(properties, "properties");
        ObjectExtension.requireNonNull(eventCounterConfig, "eventCounterConfig");
        ObjectExtension.requireNonNull(executionLatencyConfig, "executionLatencyConfig");

        _properties = properties;
        _eventCounterConfig = eventCounterConfig;
        _executionLatencyConfig = executionLatencyConfig;
    }

    public StringProperties properties() {
        return _properties;
    }

    public TimeSequenceCircularBufferConfig eventCounterConfig() {
        return _eventCounterConfig;
    }

    public DataBufferConfig executionLatencyConfig() {
        return _executionLatencyConfig;
    }

}
