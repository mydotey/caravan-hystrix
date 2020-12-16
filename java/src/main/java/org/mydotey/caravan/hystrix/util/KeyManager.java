package org.mydotey.caravan.hystrix.util;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.java.ObjectExtension;
import org.mydotey.scf.util.PropertyKeyGenerator;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public final class KeyManager {

    private KeyManager() {

    }

    public static String getCommandKey(ExecutionCommand command) {
        ObjectExtension.requireNonNull(command, "command");
        return getCommandKey(command.managerId(), command.groupId(), command.commandId());
    }

    public static String getCommandKey(String managerId, String groupId, String commandId) {
        return PropertyKeyGenerator.generatePropertyKey(managerId, groupId, commandId);
    }

    public static String getManagerLevelPropertyKey(ExecutionCommand command, String propertyKey) {
        ObjectExtension.requireNonNull(command, "command");
        return getManagerLevelPropertyKey(command.managerId(), propertyKey);
    }

    public static String getManagerLevelPropertyKey(String managerId, String propertyKey) {
        return PropertyKeyGenerator.generatePropertyKey(managerId, propertyKey);
    }

    public static String getGroupLevelPropertyKey(ExecutionCommand command, String propertyKey) {
        ObjectExtension.requireNonNull(command, "command");
        return getGroupLevelPropertyKey(command.managerId(), command.groupId(), propertyKey);
    }

    public static String getGroupLevelPropertyKey(String managerId, String groupId, String propertyKey) {
        return PropertyKeyGenerator.generatePropertyKey(managerId, groupId, propertyKey);
    }

    public static String getCommandLevelPropertyKey(ExecutionCommand command, String propertyKey) {
        ObjectExtension.requireNonNull(command, "command");
        return getCommandLevelPropertyKey(command.managerId(), command.groupId(), command.commandId(), propertyKey);
    }

    public static String getCommandLevelPropertyKey(String managerId, String groupId, String commandId,
        String propertyKey) {
        return PropertyKeyGenerator.generatePropertyKey(managerId, groupId, commandId, propertyKey);
    }

}
