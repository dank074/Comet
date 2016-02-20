package com.cometproject.server.modules.events;

import com.cometproject.api.commands.CommandInfo;
import com.cometproject.api.commands.ModuleChatCommand;
import com.cometproject.api.events.Event;
import com.cometproject.api.events.EventArgs;
import com.cometproject.api.events.EventHandler;
import com.cometproject.api.networking.sessions.ISession;
import com.cometproject.server.game.commands.ChatCommand;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

public class EventHandlerService implements EventHandler {
    private final ExecutorService asyncEventExecutor;
    private final Logger log = Logger.getLogger(EventHandlerService.class);

    private final Map<Class<?>, List<Event>> listeners;

    private final Map<String, BiConsumer<ISession, String[]>> chatCommands;
    private final Map<String, CommandInfo> commandInfo;

    public EventHandlerService() {
        this.asyncEventExecutor = Executors.newCachedThreadPool();

        this.listeners = Maps.newConcurrentMap();
        this.chatCommands = Maps.newConcurrentMap();
        this.commandInfo = Maps.newConcurrentMap();
    }

    public void registerCommandInfo(String commandName, CommandInfo info) {
        this.commandInfo.put(commandName, info);
    }

    public void registerChatCommand(String commandExecutor, BiConsumer<ISession, String[]> consumer) {
        this.chatCommands.put(commandExecutor, consumer);
    }

    public void registerEvent(Event consumer) {
        if (this.listeners.containsKey(consumer.getClass())) {
            this.listeners.get(consumer.getClass()).add(consumer);
        } else {
            this.listeners.put(consumer.getClass(), Lists.newArrayList(consumer));
        }

        log.debug(String.format("Registered event listener for %s", consumer.getClass().getSimpleName()));
    }

    public <T extends EventArgs> void handleEvent(Class<? extends Event> eventClass, T args) {
        if (this.listeners.containsKey(eventClass)) {
            this.invoke(eventClass, args);
            log.debug(String.format("Event handled: %s\n", eventClass.getSimpleName()));

        } else {
            log.debug(String.format("Unhandled event: %s\n", eventClass.getSimpleName()));
        }
    }

    private <T extends EventArgs> void invoke(Class<? extends Event> eventClass, T args) {
        for (Event method : this.listeners.get(eventClass)) {
            try {
                method.consume(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}