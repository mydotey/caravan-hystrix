package org.mydotey.caravan.hystrix.config;

import static org.mydotey.caravan.hystrix.util.KeyManager.*;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.PropertyConfig;
import org.mydotey.scf.facade.ConfigurationProperties;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.filter.RangeValueFilter;
import org.mydotey.scf.type.string.StringToBooleanConverter;
import org.mydotey.scf.type.string.StringToIntConverter;
import org.mydotey.scf.type.string.StringToLongConverter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
@SuppressWarnings("unchecked")
public class DefaultCircuitBreakerConfig implements CircuitBreakerConfig {

    private CascadedPropertyValueGettter _cascadedPropertyValueGetter;

    private PropertyConfig<String, Boolean> _managerLevelEnabledPropertyConfig;
    private PropertyConfig<String, Boolean> _managerLevelForceOpenPropertyConfig;
    private PropertyConfig<String, Boolean> _managerLevelForceClosedPropertyConfig;
    private PropertyConfig<String, Long> _managerLevelExecutionTimeoutPropertyConfig;
    private PropertyConfig<String, Long> _managerLevelExecutionCountThresholdPropertyConfig;
    private PropertyConfig<String, Integer> _managerLevelErrorPercentageThresholdPropertyConfig;
    private PropertyConfig<String, Long> _managerLevelRetryIntervalPropertyConfig;

    private PropertyConfig<String, Boolean> _groupLevelEnabledPropertyConfig;
    private PropertyConfig<String, Boolean> _groupLevelForceOpenPropertyConfig;
    private PropertyConfig<String, Boolean> _groupLevelForceClosedPropertyConfig;
    private PropertyConfig<String, Long> _groupLevelExecutionTimeoutPropertyConfig;
    private PropertyConfig<String, Long> _groupLevelExecutionCountThresholdPropertyConfig;
    private PropertyConfig<String, Integer> _groupLevelErrorPercentageThresholdPropertyConfig;
    private PropertyConfig<String, Long> _groupLevelRetryIntervalPropertyConfig;

    private PropertyConfig<String, Boolean> _enabledPropertyConfig;
    private PropertyConfig<String, Boolean> _forceOpenPropertyConfig;
    private PropertyConfig<String, Boolean> _forceClosedPropertyConfig;
    private PropertyConfig<String, Long> _executionTimeoutPropertyConfig;
    private PropertyConfig<String, Long> _executionCountThresholdPropertyConfig;
    private PropertyConfig<String, Integer> _errorPercentageThresholdPropertyConfig;
    private PropertyConfig<String, Long> _retryIntervalPropertyConfig;

    public DefaultCircuitBreakerConfig(ExecutionCommand command, StringProperties properties) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(properties, "properties");

        _cascadedPropertyValueGetter = new CascadedPropertyValueGettter(properties.getManager());

        _managerLevelEnabledPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getManagerLevelPropertyKey(command, ENABLED_PROPERTY_KEY)).setValueType(Boolean.class)
            .setDefaultValue(true).addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _managerLevelForceOpenPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getManagerLevelPropertyKey(command, FORCE_OPEN_PROPERTY_KEY)).setValueType(Boolean.class)
            .setDefaultValue(false).addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _managerLevelForceClosedPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getManagerLevelPropertyKey(command, FORCE_CLOSED_PROPERTY_KEY)).setValueType(Boolean.class)
            .setDefaultValue(false).addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _managerLevelExecutionTimeoutPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getManagerLevelPropertyKey(command, EXECUTION_TIMEOUT_PROPERTY_KEY)).setDefaultValue(
                20 * 1000L)
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _managerLevelExecutionCountThresholdPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder()
            .setKey(
                getManagerLevelPropertyKey(command, EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY))
            .setDefaultValue(20L).setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _managerLevelErrorPercentageThresholdPropertyConfig = ConfigurationProperties
            .<String, Integer>newConfigBuilder().setKey(
                getManagerLevelPropertyKey(command, ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY))
            .setDefaultValue(50).setValueType(Integer.class).setValueFilter(
                new RangeValueFilter<>(1, 100))
            .addValueConverter(StringToIntConverter.DEFAULT)
            .build();
        _managerLevelRetryIntervalPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getManagerLevelPropertyKey(command, RETRY_INTERVAL_PROPERTY_KEY)).setDefaultValue(5 * 1000L)
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();

        _groupLevelEnabledPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getGroupLevelPropertyKey(command, ENABLED_PROPERTY_KEY)).setValueType(Boolean.class)
            .addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _groupLevelForceOpenPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getGroupLevelPropertyKey(command, FORCE_OPEN_PROPERTY_KEY)).setValueType(Boolean.class)
            .addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _groupLevelForceClosedPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getGroupLevelPropertyKey(command, FORCE_CLOSED_PROPERTY_KEY)).setValueType(Boolean.class)
            .addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _groupLevelExecutionTimeoutPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getGroupLevelPropertyKey(command, EXECUTION_TIMEOUT_PROPERTY_KEY)).setValueType(Long.class)
            .addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _groupLevelExecutionCountThresholdPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder()
            .setKey(
                getGroupLevelPropertyKey(command, EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY))
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _groupLevelErrorPercentageThresholdPropertyConfig = ConfigurationProperties
            .<String, Integer>newConfigBuilder().setKey(
                getGroupLevelPropertyKey(command, ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY))
            .setValueType(Integer.class).setValueFilter(
                new RangeValueFilter<>(1, 100))
            .addValueConverter(StringToIntConverter.DEFAULT)
            .build();
        _groupLevelRetryIntervalPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getGroupLevelPropertyKey(command, RETRY_INTERVAL_PROPERTY_KEY))
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();

        _enabledPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getCommandLevelPropertyKey(command, ENABLED_PROPERTY_KEY)).setValueType(Boolean.class)
            .addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _forceOpenPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getCommandLevelPropertyKey(command, FORCE_OPEN_PROPERTY_KEY)).setValueType(Boolean.class)
            .addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _forceClosedPropertyConfig = ConfigurationProperties.<String, Boolean>newConfigBuilder()
            .setKey(getCommandLevelPropertyKey(command, FORCE_CLOSED_PROPERTY_KEY)).setValueType(Boolean.class)
            .addValueConverter(StringToBooleanConverter.DEFAULT).build();
        _executionTimeoutPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getCommandLevelPropertyKey(command, EXECUTION_TIMEOUT_PROPERTY_KEY)).setValueType(Long.class)
            .addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _executionCountThresholdPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder()
            .setKey(
                getCommandLevelPropertyKey(command, EXECUTION_COUNT_THRESHOLD_PROPERTY_KEY))
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _errorPercentageThresholdPropertyConfig = ConfigurationProperties
            .<String, Integer>newConfigBuilder().setKey(
                getCommandLevelPropertyKey(command, ERROR_PERCENTAGE_THRESHOLD_PROPERTY_KEY))
            .setValueType(Integer.class).setValueFilter(
                new RangeValueFilter<>(1, 100))
            .addValueConverter(StringToIntConverter.DEFAULT)
            .build();
        _retryIntervalPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getCommandLevelPropertyKey(command, RETRY_INTERVAL_PROPERTY_KEY))
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
    }

    @Override
    public boolean enabled() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelEnabledPropertyConfig,
            _groupLevelEnabledPropertyConfig,
            _enabledPropertyConfig);
    }

    @Override
    public boolean forceOpen() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelForceOpenPropertyConfig,
            _groupLevelForceOpenPropertyConfig,
            _forceOpenPropertyConfig);
    }

    @Override
    public boolean forceClosed() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelForceClosedPropertyConfig,
            _groupLevelForceClosedPropertyConfig,
            _forceClosedPropertyConfig);
    }

    @Override
    public long executionTimeout() {
        long r = _cascadedPropertyValueGetter.getPropertyValue(_managerLevelExecutionTimeoutPropertyConfig,
            _groupLevelExecutionTimeoutPropertyConfig,
            _executionTimeoutPropertyConfig);
        return r;
    }

    @Override
    public long executionCountThreshold() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelExecutionCountThresholdPropertyConfig,
            _groupLevelExecutionCountThresholdPropertyConfig,
            _executionCountThresholdPropertyConfig);
    }

    @Override
    public int errorPercentageThreshold() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelErrorPercentageThresholdPropertyConfig,
            _groupLevelErrorPercentageThresholdPropertyConfig,
            _errorPercentageThresholdPropertyConfig);
    }

    @Override
    public long retryInterval() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelRetryIntervalPropertyConfig,
            _groupLevelRetryIntervalPropertyConfig,
            _retryIntervalPropertyConfig);
    }

}
