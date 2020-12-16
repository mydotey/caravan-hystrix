package org.mydotey.caravan.hystrix.facade;

import java.util.function.Supplier;

import org.mydotey.caravan.hystrix.ExecutionCommand;
import org.mydotey.caravan.hystrix.ExecutionCommandManager;
import org.mydotey.caravan.hystrix.ExecutionContext;
import org.mydotey.caravan.hystrix.ExecutionEvent;
import org.mydotey.caravan.hystrix.ValidationFailChecker;
import org.mydotey.java.ObjectExtension;
import org.mydotey.java.StringExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Qiang Zhao on 10/05/2016.
 */
public class CommandExecutor {

    private static final Logger _logger = LoggerFactory.getLogger(CommandExecutor.class);

    public static void execute(ExecutionCommand command, Runnable executor) {
        execute(command, executor, null);
    }

    public static void execute(ExecutionCommand command, Runnable executor,
        ValidationFailChecker validationFailChecker) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(executor, "executor");

        ExecutionContext context = command.newExecutionContext();
        context.startExecution();
        try {
            executor.run();
            context.markSuccess();
        } catch (Throwable ex) {
            if (isValidationFail(validationFailChecker, ex))
                context.markValidationFail();
            else
                context.markFail();

            throw ex;
        } finally {
            context.endExecution();
        }
    }

    public static <V> V execute(ExecutionCommand command, Supplier<V> executor) {
        return execute(command, executor, ObjectExtension.<ValidationFailChecker>NULL());
    }

    public static <V> V execute(ExecutionCommand command, Supplier<V> executor,
        ValidationFailChecker validationFailChecker) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(executor, "executor");

        ExecutionContext context = command.newExecutionContext();
        context.startExecution();
        try {
            V result = executor.get();
            context.markSuccess();
            return result;
        } catch (Throwable ex) {
            if (isValidationFail(validationFailChecker, ex))
                context.markValidationFail();
            else
                context.markFail();

            throw ex;
        } finally {
            context.endExecution();
        }
    }

    public static <V> V execute(ExecutionCommand command, Supplier<V> executor, Supplier<V> fallbackProvider) {
        return execute(command, executor, fallbackProvider, null);
    }

    public static <V> V execute(ExecutionCommand command, Supplier<V> executor, Supplier<V> fallbackProvider,
        ValidationFailChecker validationFailChecker) {
        ObjectExtension.requireNonNull(command, "command");
        ObjectExtension.requireNonNull(executor, "executor");
        ObjectExtension.requireNonNull(fallbackProvider, "fallbackProvider");

        ExecutionContext context = command.newExecutionContext();
        if (!context.tryStartExecution()) {
            if (context.executionEvent() == ExecutionEvent.SHORT_CIRCUITED)
                _logger.warn("Execution is short circuited. Use fallback instead.");
            else
                _logger.warn("Execution is isolated. Use fallback instead.");

            return fallbackProvider.get();
        }

        try {
            V result = executor.get();
            context.markSuccess();
            return result;
        } catch (Throwable ex) {
            if (isValidationFail(validationFailChecker, ex))
                context.markValidationFail();
            else
                context.markFail();

            _logger.warn("Execution failed. Use fallback instead.", ex);
            return fallbackProvider.get();
        } finally {
            context.endExecution();
        }
    }

    public static boolean isValidationFail(ValidationFailChecker validationFailChecker, Throwable ex) {
        if (validationFailChecker == null)
            return false;

        try {
            return validationFailChecker.isValidationFail(ex);
        } catch (Throwable ex2) {
            _logger.error("validationFailChecker failed to check the fail: " + validationFailChecker, ex2);
            return false;
        }
    }

    private ExecutionCommandManager _manager;
    private ValidationFailChecker _validationFailChecker;

    public CommandExecutor(ExecutionCommandManager manager) {
        this(manager, null);
    }

    public CommandExecutor(ExecutionCommandManager manager, ValidationFailChecker validationFailChecker) {
        ObjectExtension.requireNonNull(manager, "manager");
        _manager = manager;
        _validationFailChecker = validationFailChecker;
    }

    public void execute(String commandId, Runnable executor) {
        execute(commandId, StringExtension.EMPTY, executor);
    }

    public void execute(String commandId, String groupId, Runnable executor) {
        ObjectExtension.requireNonNull(commandId, "commandId");
        ObjectExtension.requireNonNull(groupId, "groupId");
        ObjectExtension.requireNonNull(executor, "executor");

        ExecutionCommand command = _manager.getCommand(commandId, groupId);
        execute(command, executor, _validationFailChecker);
    }

    public <V> V execute(String commandId, Supplier<V> executor) {
        return execute(commandId, StringExtension.EMPTY, executor);
    }

    public <V> V execute(String commandId, String groupId, Supplier<V> executor) {
        ObjectExtension.requireNonNull(commandId, "commandId");
        ObjectExtension.requireNonNull(groupId, "groupId");
        ObjectExtension.requireNonNull(executor, "executor");

        ExecutionCommand command = _manager.getCommand(commandId, groupId);
        return execute(command, executor, _validationFailChecker);
    }

    public <V> V execute(String commandId, Supplier<V> executor, Supplier<V> fallbackProvider) {
        return execute(commandId, StringExtension.EMPTY, executor, fallbackProvider);
    }

    public <V> V execute(String commandId, String groupId, Supplier<V> executor, Supplier<V> fallbackProvider) {
        ObjectExtension.requireNonNull(commandId, "commandId");
        ObjectExtension.requireNonNull(groupId, "groupId");
        ObjectExtension.requireNonNull(executor, "executor");
        ObjectExtension.requireNonNull(fallbackProvider, "fallbackProvider");

        ExecutionCommand command = _manager.getCommand(commandId, groupId);
        return execute(command, executor, fallbackProvider, _validationFailChecker);
    }

}
