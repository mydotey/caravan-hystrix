package org.mydotey.caravan.hystrix;

import org.mydotey.caravan.hystrix.facade.CommandManagers;
import org.mydotey.caravan.hystrix.util.KeyManager;
import org.mydotey.caravan.hystrix.util.ManagerConfig;
import org.mydotey.scf.ConfigurationManager;
import org.mydotey.scf.facade.ConfigurationManagers;
import org.mydotey.scf.facade.SimpleConfigurationSources;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.scf.source.stringproperty.memorymap.MemoryMapConfigurationSource;
import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class HystrixTestUtility {

    public static final TimeSequenceCircularBufferConfig EVENT_COUNTER_CONFIG = new TimeSequenceCircularBufferConfig.Builder()
        .setTimeWindow(10 * 1000).setBucketTtl(1 * 1000).build();
    public static final DataBufferConfig LATENCY_BUFFER_CONFIG = new DataBufferConfig.Builder()
        .setTimeWindow(60 * 1000).setBucketTtl(10 * 1000).setBucketCapacity(100).build();
    public static final StringProperties PROPERTIES;
    public static final ExecutionCommandManager COMMAND_MANAGER;
    public static final ExecutionCommand TEST_COMMAND;
    public static final String TEST_MANAGER_ID = "test-manager";
    public static final String TEST_COMMAND_GROUP_ID = "test-command-group";
    public static final String TEST_COMMAND_ID = "test-command";
    private static final MemoryMapConfigurationSource COFNFIGURATION_SOURCE = SimpleConfigurationSources
        .newMemoryMapSource();

    static {
        ConfigurationManager manager = ConfigurationManagers
            .newManager(COFNFIGURATION_SOURCE);
        PROPERTIES = new StringProperties(manager);
        COMMAND_MANAGER = CommandManagers.getManager(TEST_MANAGER_ID,
            new ManagerConfig(PROPERTIES, EVENT_COUNTER_CONFIG, LATENCY_BUFFER_CONFIG));
        TEST_COMMAND = COMMAND_MANAGER.getCommand(TEST_COMMAND_ID, TEST_COMMAND_GROUP_ID);
    }

    public static void config(ExecutionCommand command, String key, String value) {
        config(command.managerId(), command.groupId(), command.commandId(), key, value);
    }

    public static void config(String managerId, String groupId, String commandId, String key, String value) {
        String propertyKey = KeyManager.getCommandLevelPropertyKey(managerId, groupId, commandId, key);
        COFNFIGURATION_SOURCE.setPropertyValue(propertyKey, value);
    }

    public static void config(String managerId, String groupId, String key, String value) {
        String propertyKey = KeyManager.getGroupLevelPropertyKey(managerId, groupId, key);
        COFNFIGURATION_SOURCE.setPropertyValue(propertyKey, value);
    }

    public static void config(String managerId, String key, String value) {
        String propertyKey = KeyManager.getManagerLevelPropertyKey(managerId, key);
        COFNFIGURATION_SOURCE.setPropertyValue(propertyKey, value);
    }

    public static void clearConfig(ExecutionCommand command, String key) {
        clearConfig(command.managerId(), command.groupId(), command.commandId(), key);
    }

    public static void clearConfig(String managerId, String groupId, String commandId, String key) {
        String propertyKey = KeyManager.getCommandLevelPropertyKey(managerId, groupId, commandId, key);
        COFNFIGURATION_SOURCE.setPropertyValue(propertyKey, null);
    }

    public static void clearConfig(String managerId, String groupId, String key) {
        String propertyKey = KeyManager.getGroupLevelPropertyKey(managerId, groupId, key);
        COFNFIGURATION_SOURCE.setPropertyValue(propertyKey, null);
    }

    public static void clearConfig(String managerId, String key) {
        String propertyKey = KeyManager.getManagerLevelPropertyKey(managerId, key);
        COFNFIGURATION_SOURCE.setPropertyValue(propertyKey, null);
    }

    private HystrixTestUtility() {

    }

}
