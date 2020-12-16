package org.mydotey.caravan.hystrix.facade;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.mydotey.caravan.hystrix.DefaultExecutionCommandManager;
import org.mydotey.caravan.hystrix.ExecutionCommandManager;
import org.mydotey.caravan.hystrix.util.ManagerConfig;
import org.mydotey.java.ObjectExtension;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class CommandManagers {

    private static ConcurrentHashMap<String, ExecutionCommandManager> _managers = new ConcurrentHashMap<>();

    private CommandManagers() {

    }

    public static Collection<ExecutionCommandManager> managers() {
        return Collections.unmodifiableCollection(_managers.values());
    }

    public static ExecutionCommandManager getManager(final String managerId, final ManagerConfig managerConfig) {
        ObjectExtension.requireNonNull(managerId, "managerId");
        ObjectExtension.requireNonNull(managerConfig, "managerConfig");

        return _managers.computeIfAbsent(managerId,
            k -> new DefaultExecutionCommandManager(managerId, managerConfig.properties(),
                managerConfig.eventCounterConfig(),
                managerConfig.executionLatencyConfig()));
    }

}
