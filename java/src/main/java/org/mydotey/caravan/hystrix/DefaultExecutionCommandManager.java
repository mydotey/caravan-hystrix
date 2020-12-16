package org.mydotey.caravan.hystrix;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.mydotey.caravan.hystrix.util.KeyManager;
import org.mydotey.java.ObjectExtension;
import org.mydotey.java.StringExtension;
import org.mydotey.scf.facade.StringProperties;
import org.mydotey.util.DataBufferConfig;
import org.mydotey.util.TimeSequenceCircularBufferConfig;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class DefaultExecutionCommandManager implements ExecutionCommandManager {

    private String _managerId;
    private StringProperties _properties;
    private TimeSequenceCircularBufferConfig _eventCounterConfig;
    private DataBufferConfig _executionLatencyConfig;

    private volatile ConcurrentHashMap<String, ExecutionCommand> _commands = new ConcurrentHashMap<>();

    public DefaultExecutionCommandManager(String managerId, StringProperties properties,
        TimeSequenceCircularBufferConfig eventCounterConfig,
        DataBufferConfig executionLatencyConfig) {
        ObjectExtension.requireNonNull(managerId, "managerId");
        ObjectExtension.requireNonNull(properties, "properties");
        ObjectExtension.requireNonNull(eventCounterConfig, "eventCounterConfig");
        ObjectExtension.requireNonNull(executionLatencyConfig, "executionLatencyConfig");

        _managerId = StringExtension.trim(managerId);
        _properties = properties;
        _eventCounterConfig = eventCounterConfig;
        _executionLatencyConfig = executionLatencyConfig;
    }

    @Override
    public String managerId() {
        return _managerId;
    }

    @Override
    public Collection<ExecutionCommand> commands() {
        return Collections.unmodifiableCollection(_commands.values());
    }

    @Override
    public ExecutionCommand getCommand(final String commandId) {
        return getCommand(commandId, StringExtension.EMPTY);
    }

    @Override
    public ExecutionCommand getCommand(final String commandId, final String groupId) {
        String key = KeyManager.getCommandKey(_managerId, groupId, commandId);
        return _commands.computeIfAbsent(key,
            k -> new DefaultExecutionCommand(_managerId, groupId, commandId, _properties, _eventCounterConfig,
                _executionLatencyConfig));
    }

    @Override
    public void reset() {
        _commands = new ConcurrentHashMap<>();
    }

}
