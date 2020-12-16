package org.mydotey.caravan.hystrix.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.mydotey.java.ObjectExtension;
import org.mydotey.java.collection.CollectionExtension;
import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.PropertyConfig;

public class CascadedPropertyValueGettter {

    private ConfigurationManager _configurationManager;

    public CascadedPropertyValueGettter(ConfigurationManager configurationManager) {
        ObjectExtension.requireNonNull(configurationManager, "configurationManager");
        _configurationManager = configurationManager;
    }

    @SuppressWarnings("unchecked")
    public <K, V> V getPropertyValue(PropertyConfig<K, V>... hierarchicalPropertyConfigs) {
        if (CollectionExtension.isEmpty(hierarchicalPropertyConfigs))
            return null;

        List<PropertyConfig<K, V>> list = new ArrayList<PropertyConfig<K, V>>();
        Collections.addAll(list, hierarchicalPropertyConfigs);
        return getPropertyValue(list);
    }

    @SuppressWarnings("unchecked")
    private <K, V> V getPropertyValue(List<PropertyConfig<K, V>> hierarchicalPropertyConfigs) {
        if (CollectionExtension.isEmpty(hierarchicalPropertyConfigs))
            return null;

        if (hierarchicalPropertyConfigs.size() == 1)
            return _configurationManager.getPropertyValue(hierarchicalPropertyConfigs.get(0));

        if (hierarchicalPropertyConfigs.size() == 2)
            return getPropertyValue(hierarchicalPropertyConfigs.get(0), hierarchicalPropertyConfigs.get(1));
        V genericLevelPropertyValue = getPropertyValue(hierarchicalPropertyConfigs.remove(0));
        V concreteLevelPropertyValue = getPropertyValue(hierarchicalPropertyConfigs);
        return getPropertyValue(genericLevelPropertyValue, concreteLevelPropertyValue);
    }

    private <K, V> V getPropertyValue(PropertyConfig<K, V> genericLevelPropertyConfig,
        PropertyConfig<K, V> concreteLevelPropertyConfig) {
        V genericLevelPropertyValue = _configurationManager.getPropertyValue(genericLevelPropertyConfig);
        V concreteLevelPropertyValue = _configurationManager.getPropertyValue(concreteLevelPropertyConfig);
        return getPropertyValue(genericLevelPropertyValue, concreteLevelPropertyValue);
    }

    private <T> T getPropertyValue(T genericLevelPropertyValue, T concreteLevelPropertyValue) {
        if (!ObjectExtension.isNullOrEmpty(concreteLevelPropertyValue))
            return concreteLevelPropertyValue;

        return genericLevelPropertyValue;
    }

}
