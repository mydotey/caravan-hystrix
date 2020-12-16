package org.mydotey.caravan.hystrix.config;

import static org.mydotey.caravan.hystrix.util.KeyManager.*;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.PropertyConfig;
import org.mydotey.scf.facade.ConfigurationProperties;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.filter.RangeValueFilter;
import org.mydotey.scf.type.string.StringToLongConverter;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultIsolatorConfig implements IsolatorConfig {

    private CascadedPropertyValueGettter _cascadedPropertyValueGetter;

    private PropertyConfig<String, Long> _managerLevelMaxConcurrentCountPropertyConfig;
    private PropertyConfig<String, Long> _groupLevelMaxConcurrentCountPropertyConfig;
    private PropertyConfig<String, Long> _maxConcurrentCountPropertyConfig;

    public DefaultIsolatorConfig(ExecutionCommand command, StringProperties properties) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(properties, "properties");

        _cascadedPropertyValueGetter = new CascadedPropertyValueGettter(properties.getManager());

        _managerLevelMaxConcurrentCountPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getManagerLevelPropertyKey(command, MAX_CONCURRENT_COUNT_PROPERTY_KEY)).setDefaultValue(
                200L)
            .setValueType(Long.class).addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _groupLevelMaxConcurrentCountPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getGroupLevelPropertyKey(command, MAX_CONCURRENT_COUNT_PROPERTY_KEY)).setValueType(Long.class)
            .addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
        _maxConcurrentCountPropertyConfig = ConfigurationProperties.<String, Long>newConfigBuilder().setKey(
            getCommandLevelPropertyKey(command, MAX_CONCURRENT_COUNT_PROPERTY_KEY)).setValueType(Long.class)
            .addValueConverter(StringToLongConverter.DEFAULT)
            .setValueFilter(new RangeValueFilter<>(1L, Long.MAX_VALUE)).build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public long maxConcurrentCount() {
        return _cascadedPropertyValueGetter.getPropertyValue(_managerLevelMaxConcurrentCountPropertyConfig,
            _groupLevelMaxConcurrentCountPropertyConfig,
            _maxConcurrentCountPropertyConfig);
    }

}
