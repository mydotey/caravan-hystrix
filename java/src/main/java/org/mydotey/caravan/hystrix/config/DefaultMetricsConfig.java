package org.mydotey.caravan.hystrix.config;

import static org.mydotey.caravan.hystrix.util.KeyManager.*;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.PropertyConfig;
import org.mydotey.scf.facade.ConfigurationProperties;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.filter.RangeValueFilter;
import org.mydotey.scf.type.string.StringToLongConverter;
import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultMetricsConfig implements MetricsConfig {

    private CascadedPropertyValueGettter _cascadedPropertyValueGetter;

    private PropertyConfig<String, Long> _managerLevelHealthSnapshotIntervalPropertyConfig;
    private PropertyConfig<String, Long> _groupLevelHealthSnapshotIntervalPropertyConfig;
    private PropertyConfig<String, Long> _healthSnapshotIntervalPropertyConfig;

    private TimeSequenceCircularBufferConfig _eventCounterConfig;
    private DataBufferConfig _executionLatencyConfig;

    public DefaultMetricsConfig(ExecutionCommand command, StringProperties properties,
        TimeSequenceCircularBufferConfig eventCounterConfig,
        DataBufferConfig executionLatencyConfig) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(properties, "properties");
        ObjectExtension.requireNonNull(eventCounterConfig, "eventCounterConfig");
        ObjectExtension.requireNonNull(executionLatencyConfig, "executionLatencyConfig");

        _cascadedPropertyValueGetter = new CascadedPropertyValueGettter(properties.getManager());

        _managerLevelHealthSnapshotIntervalPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder()
            .setKey(
                getManagerLevelPropertyKey(command, HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY))
            .setDefaultValue(
                100L)
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(0L, Long.MAX_VALUE)).build();
        _groupLevelHealthSnapshotIntervalPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder()
            .setKey(
                getGroupLevelPropertyKey(command, HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY))
            .setDefaultValue(
                100L)
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(0L, Long.MAX_VALUE)).build();

        _healthSnapshotIntervalPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder()
            .setKey(
                getCommandLevelPropertyKey(command, HEALTH_SNAPSHOT_INTERVAL_PROPERTY_KEY))
            .setDefaultValue(
                100L)
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(0L, Long.MAX_VALUE)).build();

        _eventCounterConfig = eventCounterConfig;
        _executionLatencyConfig = executionLatencyConfig;
    }

    @Override
    public TimeSequenceCircularBufferConfig eventCounterConfig() {
        return _eventCounterConfig;
    }

    @SuppressWarnings("unchecked")
    @Override
    public long healthSnapshotInterval() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelHealthSnapshotIntervalPropertyConfig,
            _groupLevelHealthSnapshotIntervalPropertyConfig,
            _healthSnapshotIntervalPropertyConfig);
    }

    @Override
    public DataBufferConfig executionLatencyConfig() {
        return _executionLatencyConfig;
    }

}
