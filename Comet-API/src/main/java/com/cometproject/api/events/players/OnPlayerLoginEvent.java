package com.cometproject.api.events.players;

import com.cometproject.api.events.Event;
import com.cometproject.api.events.players.args.OnPlayerLoginEventArgs;
import com.cometproject.api.game.players.IPlayer;

import java.util.function.Consumer;

public class OnPlayerLoginEvent extends Event<OnPlayerLoginEventArgs> {
    public OnPlayerLoginEvent(Consumer<OnPlayerLoginEventArgs> eventConsumer) {
        super(eventConsumer);
    }
}
